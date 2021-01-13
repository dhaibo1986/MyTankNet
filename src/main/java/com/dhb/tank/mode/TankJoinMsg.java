package com.dhb.tank.mode;

import com.dhb.tank.comms.Dir;
import com.dhb.tank.comms.Group;

import java.io.*;
import java.util.UUID;

public class TankJoinMsg {

	//坐标 x
	private int x;
	//坐标 y
	private int y;
	//方向
	private Dir dir;
	//静止or移动状态
	private boolean moving;
	//敌我分组
	private Group group;
	//标识 uuid
	private UUID id;

	public TankJoinMsg(int x, int y, Dir dir, boolean moving, Group group, UUID id) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.moving = moving;
		this.group = group;
		this.id = id;
	}

	public TankJoinMsg(Tank tank) {
		this.x = tank.getX();
		this.y = tank.getY();
		this.dir = tank.getDir();
		this.moving = tank.isMoving();
		this.group = tank.getGroup();
		this.id = tank.getId();
	}

	public TankJoinMsg() {

	}

	public void parse(byte[] bytes) {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
		try {
			this.x = dis.readInt();
			this.y = dis.readInt();
			this.dir = Dir.values()[dis.readInt()];
			this.moving = dis.readBoolean();
			this.group = Group.values()[dis.readInt()];
			this.id = new UUID(dis.readLong(),dis.readLong());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public byte[] toBytes() {
		ByteArrayOutputStream baos = null;
		DataOutputStream dos = null;
		byte[] bytes = null;

		try {
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(dir.ordinal());
			dos.writeLong(id.getMostSignificantBits());
			dos.writeLong(id.getLeastSignificantBits());
			dos.flush();
			bytes = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			if(dos != null) {
				dos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	@Override
	public String toString() {
		return "TankJoinMsg{" +
				"x=" + x +
				", y=" + y +
				", dir=" + dir +
				", moving=" + moving +
				", group=" + group +
				", id=" + id +
				'}';
	}
}
