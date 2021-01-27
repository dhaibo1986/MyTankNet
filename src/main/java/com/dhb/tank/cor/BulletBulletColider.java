package com.dhb.tank.cor;

import com.dhb.tank.mode.Bullet;
import com.dhb.tank.mode.Explode;
import com.dhb.tank.mode.GameObject;
import com.dhb.tank.mode.Tank;

public class BulletBulletColider implements Colider {

	@Override
	public boolean colide(GameObject o1, GameObject o2) {
		if (o1 instanceof Bullet && o2 instanceof Bullet) {
			Bullet bullet1 = (Bullet) o1;
			Bullet bullet2 = (Bullet) o2;
			if (bullet1.getGroup() == bullet2.getGroup()) {
				return true;
			}
			if (bullet1.getRect().intersects(bullet2.getRect())) {
				bullet1.die();
				bullet2.die();
				int eX = bullet1.getX() + Tank.WIDTH / 2 - Explode.WIDTH / 2;
				int eY = bullet1.getY() + Tank.HEIGHT / 2 - Explode.HEIGHT / 2;
				new Explode(eX, eY, true);
			}
			return false;
		} else if (o2 instanceof Bullet && o1 instanceof Tank) {
			colide(o2, o1);
		}
		return true;
	}
}
