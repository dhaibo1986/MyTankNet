package com.dhb.tank.mode;

import com.dhb.tank.comms.*;
import com.dhb.tank.frame.TankFrame;

import java.awt.*;
import java.util.Random;
import java.util.UUID;

public class Tank extends GameObject{

	private static final int SPEED = 2;
	public static final int WIDTH = ResourseMgr.getInstance().getGoodTankU().getWidth();
	public static final int HEIGHT = ResourseMgr.getInstance().getGoodTankU().getHeight();
	Rectangle rect = new Rectangle();
	private Random random = new Random();


	private int oldX;
	private int oldY;

	//坐标 x
	private int x;
	//坐标 y
	private int y;
	//方向
	private Dir dir = Dir.DOWN;
	//静止or移动状态
	private boolean moving = false;
	//敌我分组
	private Group group;
	//标识 uuid
	private UUID id = UUID.randomUUID();
	//存活状态
	private boolean living = true;

	private TankFrame tf = null;

	public Tank(int x, int y, Dir dir, boolean moving, Group group,TankFrame tf) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.moving = moving;
		this.group = group;
		this.tf = tf;
	}

	public Tank(TankJoinMsg msg) {
		this.x = msg.getX();
		this.y = msg.getY();
		this.dir = msg.getDir();
		this.moving = msg.isMoving();
		this.group = msg.getGroup();
		this.id = msg.getId();
	}

	public Tank() {

	}

	public static int getSPEED() {
		return SPEED;
	}

	public void die() {
		this.living = false;
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}


	@Override
	public String toString() {
		return "Tank{" +
				"x=" + x +
				", y=" + y +
				", dir=" + dir +
				", moving=" + moving +
				", group=" + group +
				", id=" + id +
				'}';
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Dir getDir() {
		return dir;
	}

	public void setDir(Dir dir) {
		this.dir = dir;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public void paint(Graphics g) {
		if (!living) {
			TankFrame.INSTANCE.removeTank(this);
			return;
		}
		//画出坦克的uuid
		Color c = g.getColor();
		g.setColor(Color.YELLOW);
		g.drawString(id.toString(),this.x,this.y-10);
		g.setColor(c);

		switch (dir) {
			case LEFT:
				g.drawImage(this.group == Group.GOOD ? ResourseMgr.getInstance().getGoodTankL() : ResourseMgr.getInstance().getBadTankL(), x, y, null);
				break;
			case RIGHT:
				g.drawImage(this.group == Group.GOOD ? ResourseMgr.getInstance().getGoodTankR() : ResourseMgr.getInstance().getBadTankR(), x, y, null);
				break;
			case DOWN:
				g.drawImage(this.group == Group.GOOD ? ResourseMgr.getInstance().getGoodTankD() : ResourseMgr.getInstance().getBadTankD(), x, y, null);
				break;
			case UP:
				g.drawImage(this.group == Group.GOOD ? ResourseMgr.getInstance().getGoodTankU() : ResourseMgr.getInstance().getBadTankU(), x, y, null);
				break;

			default:
				break;
		}
		this.move();
	}

	public void fire() {
		int bX = this.getX() + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
		int bY = this.getY() + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
//		new RectDecorator(new TailDecorator(new Bullet(bX, bY, t.getDir(), t.getGroup())));
		new Bullet(bX, bY, this.getDir(), this.getGroup());
		if (this.getGroup() == Group.GOOD) {
			new Thread(() -> {
				new Audio("audio/tank_fire.wav");
			}).start();
		}
	}


	public static int getWIDTH() {
		return WIDTH;
	}

	public static int getHEIGHT() {
		return HEIGHT;
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public int getOldX() {
		return oldX;
	}

	public void setOldX(int oldX) {
		this.oldX = oldX;
	}

	public int getOldY() {
		return oldY;
	}

	public void setOldY(int oldY) {
		this.oldY = oldY;
	}

	public boolean isLiving() {
		return living;
	}

	public void setLiving(boolean living) {
		this.living = living;
	}

	public TankFrame getTf() {
		return tf;
	}

	public void setTf(TankFrame tf) {
		this.tf = tf;
	}

	private void move() {
		if (isMoving()) {
			switch (dir) {
				case LEFT:
					x -= SPEED;
					break;
				case RIGHT:
					x += SPEED;
					break;
				case UP:
					y -= SPEED;
					break;
				case DOWN:
					y += SPEED;
					break;
				default:
					break;
			}
		}
		oldX = x;
		oldY = y;
		rect.x = x;
		rect.y = y;
		if (this.group == Group.BAD && random.nextInt(20) > 18) {
			this.fire();
		}
		if (this.group == Group.BAD && random.nextInt(20) > 18) {
			randomDir();
		}

		boundsCheck();
	}


	private void boundsCheck() {
		if (this.x < 2) {
			x = 0;
		}
		if (this.y < 28) {
			y = 28;
		}
		if (this.x > (TankFrame.GAME_WIDTH - Tank.WIDTH - 2)) {
			x = TankFrame.GAME_WIDTH - Tank.WIDTH - 2;
		}

		if (this.y > TankFrame.GAME_HEIGHT - Tank.HEIGHT - 2) {
			y = TankFrame.GAME_HEIGHT - Tank.HEIGHT - 2;
		}
	}

	private void randomDir() {
		this.dir = Dir.values()[random.nextInt(4)];
	}

}
