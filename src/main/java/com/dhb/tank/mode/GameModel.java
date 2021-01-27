package com.dhb.tank.mode;

import com.dhb.tank.comms.Dir;
import com.dhb.tank.comms.Group;
import com.dhb.tank.cor.ColiderChain;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class GameModel {

	static {
		GameModel.getInstance().init();
	}

	ColiderChain chain = new ColiderChain();
	Random r = new Random();
	private Tank myTank;
	private List<GameObject> objects = new ArrayList<>();
	private Map<UUID, Tank> tanks = new HashMap<>();
	private List<Bullet> bullets = new ArrayList<>();
	private List<Explode> explodes = new ArrayList<>();
	private GameModel() {

	}

	public static GameModel getInstance() {
		return Sigleton.INSTANCE.getInstance();
	}

	public Tank getMainTank() {
		return myTank;
	}

	private void init() {
		myTank = new Tank(r.nextInt(1000), r.nextInt(800), Dir.DOWN, false, Group.GOOD);
		//初始化墙
		new Wall(150, 150, 200, 50);
		new Wall(550, 150, 200, 50);
		new Wall(300, 300, 50, 200);
		new Wall(650, 300, 50, 200);
	}

	public void add(GameObject go) {
		this.objects.add(go);
		//坦克对象还需要添加到另外一个map数据结构，便于检索
		if (go instanceof Tank) {
			Tank tank = (Tank) go;
			this.tanks.put(tank.getId(), tank);
		} else if (go instanceof Bullet) {
			Bullet bullet = (Bullet) go;
			this.bullets.add(bullet);
		} else if (go instanceof Explode) {
			Explode explode = (Explode) go;
			this.explodes.add(explode);
		}
	}

	public Bullet findBulletByUUID(UUID id) {
		for (int i = 0; i < bullets.size(); i++) {
			if (bullets.get(i).getId().equals(id)) {
				return bullets.get(i);
			}
		}
		return null;
	}

	public Tank findTankByUUID(UUID id) {
		return this.tanks.get(id);
	}

	public void remove(GameObject go) {
		this.objects.remove(go);
		if (go instanceof Tank) {
			Tank tank = (Tank) go;
			this.tanks.remove(tank.getId());
		} else if (go instanceof Bullet) {
			this.bullets.remove(go);
		} else if (go instanceof Explode) {
			this.explodes.remove(go);
		}
	}

	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		g.drawString("子弹的数量：" + bullets.size(), 10, 60);
		g.drawString("敌方坦克数量：" + tanks.size(), 10, 75);
		g.drawString("爆炸数量：" + explodes.size(), 10, 90);
		g.setColor(c);

		//调用每个对象的paint的方法
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).paint(g);
		}
		//碰撞逻辑
		for (int i = 0; i < objects.size(); i++) {
			for (int j = i + 1; j < objects.size(); j++) {
				GameObject o1 = objects.get(i);
				GameObject o2 = objects.get(j);
				chain.colide(o1, o2);
			}
		}

	}


	private enum Sigleton {
		INSTANCE;

		private GameModel instance;


		Sigleton() {
			instance = new GameModel();
		}

		public GameModel getInstance() {
			return instance;
		}

	}
}
