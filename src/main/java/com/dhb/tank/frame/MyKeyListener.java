package com.dhb.tank.frame;

import com.dhb.tank.client.Client;
import com.dhb.tank.comms.Dir;
import com.dhb.tank.mode.Tank;
import com.dhb.tank.mode.TankStartMovingMsg;
import com.dhb.tank.mode.TankStopMsg;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyKeyListener extends KeyAdapter {

	boolean BL = false;
	boolean BR = false;
	boolean BU = false;
	boolean BD = false;

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getExtendedKeyCode();
		switch (key) {
			case KeyEvent.VK_LEFT:
				BL = true;
				break;
			case KeyEvent.VK_UP:
				BU = true;
				break;
			case KeyEvent.VK_DOWN:
				BD = true;
				break;
			case KeyEvent.VK_RIGHT:
				BR = true;
				break;
//			case KeyEvent.VK_S:
//				GameModel.getInstance().save();
//			case KeyEvent.VK_L:
//				GameModel.getInstance().load();
			default:
				break;
		}
		setMainTankDir();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getExtendedKeyCode();
		switch (key) {
			case KeyEvent.VK_LEFT:
				BL = false;
				break;
			case KeyEvent.VK_UP:
				BU = false;
				break;
			case KeyEvent.VK_DOWN:
				BD = false;
				break;
			case KeyEvent.VK_RIGHT:
				BR = false;
				break;

			case KeyEvent.VK_CONTROL:
				TankFrame.INSTANCE.getMainTank().fire();
				break;
			default:
				break;
		}
		setMainTankDir();
	}

	private void setMainTankDir() {
		Tank myTank = TankFrame.INSTANCE.getMainTank();
		if (!BL && !BR && !BU && !BD) {
			myTank.setMoving(false);
			Client.INSTANCE.send(new TankStopMsg(myTank));
		} else {
			myTank.setMoving(true);
			if (BL) {
				myTank.setDir(Dir.LEFT);
			}
			if (BR) {
				myTank.setDir(Dir.RIGHT);
			}
			if (BU) {
				myTank.setDir(Dir.UP);
			}
			if (BD) {
				myTank.setDir(Dir.DOWN);
			}
			Client.INSTANCE.send(new TankStartMovingMsg(myTank));
		}
	}
}

