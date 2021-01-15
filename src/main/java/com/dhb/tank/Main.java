package com.dhb.tank;

import com.dhb.tank.client.Client;
import com.dhb.tank.comms.Audio;
import com.dhb.tank.frame.TankFrame;

import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {
		TankFrame frame = TankFrame.INSTANCE;
		frame.setVisible(true);
		new Thread(() -> new Audio("audio/war1.wav").loop()).start();
		new Thread(() -> {
			while (true) {
				try {
					TimeUnit.MILLISECONDS.sleep(100);
					frame.repaint();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		Client c = new Client();
		c.connect();
	}

}
