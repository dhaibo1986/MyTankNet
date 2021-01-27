package com.dhb.tank.comms;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

public class ResourseMgr {

	private ResourseMgr() {
		try {
			badTankU = ImageIO.read(ResourseMgr.class.getClassLoader().getResourceAsStream("images/BadTank1.png"));
			badTankL = ImageUtil.rotateImage(badTankU, -90);
			badTankR = ImageUtil.rotateImage(badTankU, 90);
			badTankD = ImageUtil.rotateImage(badTankU, 180);

			goodTankU = ImageIO.read(ResourseMgr.class.getClassLoader().getResourceAsStream("images/GoodTank1.png"));
			goodTankL = ImageUtil.rotateImage(goodTankU, -90);
			goodTankR = ImageUtil.rotateImage(goodTankU, 90);
			goodTankD = ImageUtil.rotateImage(goodTankU, 180);


			bulletU = ImageIO.read(ResourseMgr.class.getClassLoader().getResourceAsStream("images/bulletU.png"));
			bulletL = ImageUtil.rotateImage(bulletU, -90);
			bulletR = ImageUtil.rotateImage(bulletU, 90);
			bulletD = ImageUtil.rotateImage(bulletU, 180);

			for (int i = 0; i < 16; i++) {
				explodes[i] = ImageIO.read(ResourseMgr.class.getClassLoader().getResourceAsStream("images/e" + (i + 1) + ".gif"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static enum  ResourseMgrEnum {
		INSTANCE;

		private final ResourseMgr instance;

		ResourseMgrEnum() {
			instance = new ResourseMgr();
		}

		private ResourseMgr getInstance() {
			return instance;
		}
	}

	public static ResourseMgr getInstance() {
		return ResourseMgrEnum.INSTANCE.getInstance();
	}

	private  BufferedImage goodTankU, goodTankL, goodTankR, goodTankD;
	private  BufferedImage badTankU, badTankL, badTankR, badTankD;

	private  BufferedImage bulletL, bulletU, bulletR, bulletD;

	private  BufferedImage[] explodes = new BufferedImage[16];


	public BufferedImage getGoodTankU() {
		return goodTankU;
	}

	public void setGoodTankU(BufferedImage goodTankU) {
		this.goodTankU = goodTankU;
	}

	public BufferedImage getGoodTankL() {
		return goodTankL;
	}

	public void setGoodTankL(BufferedImage goodTankL) {
		this.goodTankL = goodTankL;
	}

	public BufferedImage getGoodTankR() {
		return goodTankR;
	}

	public void setGoodTankR(BufferedImage goodTankR) {
		this.goodTankR = goodTankR;
	}

	public BufferedImage getGoodTankD() {
		return goodTankD;
	}

	public void setGoodTankD(BufferedImage goodTankD) {
		this.goodTankD = goodTankD;
	}

	public BufferedImage getBadTankU() {
		return badTankU;
	}

	public void setBadTankU(BufferedImage badTankU) {
		this.badTankU = badTankU;
	}

	public BufferedImage getBadTankL() {
		return badTankL;
	}

	public void setBadTankL(BufferedImage badTankL) {
		this.badTankL = badTankL;
	}

	public BufferedImage getBadTankR() {
		return badTankR;
	}

	public void setBadTankR(BufferedImage badTankR) {
		this.badTankR = badTankR;
	}

	public BufferedImage getBadTankD() {
		return badTankD;
	}

	public void setBadTankD(BufferedImage badTankD) {
		this.badTankD = badTankD;
	}

	public BufferedImage getBulletL() {
		return bulletL;
	}

	public void setBulletL(BufferedImage bulletL) {
		this.bulletL = bulletL;
	}

	public BufferedImage getBulletU() {
		return bulletU;
	}

	public void setBulletU(BufferedImage bulletU) {
		this.bulletU = bulletU;
	}

	public BufferedImage getBulletR() {
		return bulletR;
	}

	public void setBulletR(BufferedImage bulletR) {
		this.bulletR = bulletR;
	}

	public BufferedImage getBulletD() {
		return bulletD;
	}

	public void setBulletD(BufferedImage bulletD) {
		this.bulletD = bulletD;
	}

	public BufferedImage[] getExplodes() {
		return explodes;
	}

	public void setExplodes(BufferedImage[] explodes) {
		this.explodes = explodes;
	}
}
