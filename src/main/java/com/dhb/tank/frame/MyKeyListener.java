package com.dhb.tank.frame;

import com.dhb.tank.client.Client;
import com.dhb.tank.comms.Dir;
import com.dhb.tank.mode.Tank;
import com.dhb.tank.mode.TankDirChangedMsg;
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
				setMainTankDir();
				break;
			case KeyEvent.VK_UP:
				BU = true;
				setMainTankDir();
				break;
			case KeyEvent.VK_DOWN:
				BD = true;
				setMainTankDir();
				break;
			case KeyEvent.VK_RIGHT:
				BR = true;
				setMainTankDir();
				break;
//			case KeyEvent.VK_S:
//				GameModel.getInstance().save();
//			case KeyEvent.VK_L:
//				GameModel.getInstance().load();
			default:
				break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		Tank myTank = TankFrame.INSTANCE.getMainTank();
		if (!myTank.isLiving()) {
			return;
		}
		int key = e.getExtendedKeyCode();
		switch (key) {
			case KeyEvent.VK_LEFT:
				BL = false;
				setMainTankDir();
				break;
			case KeyEvent.VK_UP:
				BU = false;
				setMainTankDir();
				break;
			case KeyEvent.VK_DOWN:
				BD = false;
				setMainTankDir();
				break;
			case KeyEvent.VK_RIGHT:
				BR = false;
				setMainTankDir();
				break;

			case KeyEvent.VK_CONTROL:
				TankFrame.INSTANCE.getMainTank().fire();
				break;
			default:
				break;
		}
	}

	private void setMainTankDir() {
		Tank myTank = TankFrame.INSTANCE.getMainTank();
		Dir dir = myTank.getDir();

		if (!myTank.isLiving()) {
			return;
		}

		if (!BL && !BR && !BU && !BD) {
			myTank.setMoving(false);
			Client.INSTANCE.send(new TankStopMsg(myTank));
		} else {
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
			//发出坦克移动的消息
			if (!myTank.isMoving()) {
				Client.INSTANCE.send(new TankStartMovingMsg(myTank));
			}
			myTank.setMoving(true);
			//发出坦克改变方向的消息
			if (dir != myTank.getDir()) {
				Client.INSTANCE.send(new TankDirChangedMsg(myTank));
			}
		}
	}
}

