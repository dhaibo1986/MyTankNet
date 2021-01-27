package com.dhb.tank.frame;

import com.dhb.tank.client.Client;
import com.dhb.tank.mode.GameModel;
import com.dhb.tank.mode.TankExitMsg;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankFrame extends Frame {

	public static final TankFrame INSTANCE = new TankFrame();
	public static final int GAME_WIDTH = 1000;
	public static final int GAME_HEIGHT = 800;
	Image offScreenImage = null;

	private TankFrame() {
		setSize(GAME_WIDTH, GAME_HEIGHT);
		setResizable(false);
		setTitle("tank war");
		this.addKeyListener(new MyKeyListener());
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//发退出消息
				Client.INSTANCE.send(new TankExitMsg(GameModel.getInstance().getMainTank()));
				//断开连接并回收客户端资源
				Client.INSTANCE.stop();
				System.exit(0);
			}
		});
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
		GameModel.getInstance().paint(g);
	}


}
