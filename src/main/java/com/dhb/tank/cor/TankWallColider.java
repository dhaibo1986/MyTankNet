package com.dhb.tank.cor;

import com.dhb.tank.mode.GameObject;
import com.dhb.tank.mode.Tank;
import com.dhb.tank.mode.Wall;

public class TankWallColider implements Colider {

	@Override
	public boolean colide(GameObject o1, GameObject o2) {
		if (o1 instanceof Tank && o2 instanceof Wall) {
			Tank tank = (Tank) o1;
			Wall wall = (Wall) o2;
			if (tank.getRect().intersects(wall.getRect())) {
				tank.back();
			}
		} else if (o2 instanceof Tank && o1 instanceof Wall) {
			colide(o2, o1);
		}
		return true;
	}
}
