package com.dhb.tank.comms;

/**
*@author dhaibo1986
*@description 标识方向的枚举类
*@date  2021/1/13 16:22
*/
public enum Dir {
	//左
	LEFT,
	//右
	RIGHT,
	//下
	DOWN,
	//上
	UP;

	/**
	 * 获得对应dir的反方向
	 * @param dir
	 * @return
	 */
	public static Dir getOppositeDir(Dir dir) {
		if(dir == LEFT) {
			return RIGHT;
		} else if (dir == RIGHT) {
			return LEFT;
		} else if (dir == UP) {
			return DOWN;
		}else if(dir == DOWN) {
			return UP;
		}else {
			return DOWN;
		}
	}
}
