package com.dhb.tank.frame;

import com.dhb.tank.comms.*;
import com.dhb.tank.mode.Bullet;
import com.dhb.tank.mode.Explode;
import com.dhb.tank.mode.Tank;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;

public class TankFrame extends Frame {

	public static final int SPEED = 10;

	public static final TankFrame INSTANCE = new TankFrame();
	public static final int GAME_WIDTH = 1000;
	public static final int GAME_HEIGHT = 800;
	Random r = new Random();
	List<Bullet> bullets = new ArrayList<>();
	Map<UUID, Tank> tanks = new HashMap<>();
	List<Explode> explodes = new ArrayList<>();
	Image offScreenImage = null;
	private Tank myTank = new Tank(r.nextInt(1000), r.nextInt(800), Dir.DOWN, false, Group.GOOD, this);

	private TankFrame() {
		setSize(GAME_WIDTH, GAME_HEIGHT);
		setResizable(false);
		setTitle("tank war");
		this.addKeyListener(new MyKeyListener());
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public void addTank(Tank t) {
		tanks.put(t.getId(), t);
	}

	public void removeTank(Tank t) {
		tanks.remove(t.getId());
	}

	public void addBullet(Bullet b) {
		bullets.add(b);
	}

	public void removeBullet(Bullet b){
		bullets.remove(b);
	}

	public void addExplode(Explode e) {
		explodes.add(e);
	}

	public void removeExplode(Explode e) {
		explodes.remove(e);
	}

	public Tank findByUUID(UUID id) {
		return tanks.get(id);
	}

	public Tank getMainTank() {
		return myTank;
	}

	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.BLACK);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		g.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		g.drawString("bullets:" + bullets.size(), 10, 60);
		g.drawString("tanks:" + tanks.size(), 10, 80);
		g.drawString("explodes" + explodes.size(), 10, 100);
		g.setColor(c);

		myTank.paint(g);
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).paint(g);
		}

		tanks.values().stream().forEach((e) -> e.paint(g));

		for (int i = 0; i < explodes.size(); i++) {
			explodes.get(i).paint(g);
		}

		for (int i = 0; i < bullets.size(); i++) {
			for (int j = 0; j < tanks.size(); j++) {
				bullets.get(i).collideWith(tanks.get(j));
			}
		}
	}


}
