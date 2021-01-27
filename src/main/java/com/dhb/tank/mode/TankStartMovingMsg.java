package com.dhb.tank.mode;

import com.dhb.tank.comms.Dir;
import com.dhb.tank.frame.TankFrame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class TankStartMovingMsg extends Msg {

	private UUID id;
	private int x;
	private int y;
	private Dir dir;

	public TankStartMovingMsg(int x, int y, Dir dir, UUID id) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public TankStartMovingMsg(Tank t) {
		this.id = t.getId();
		this.x = t.getX();
		this.y = t.getY();
		this.dir = t.getDir();
	}

	public TankStartMovingMsg() {

	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "TankStartMovingMsg{" +
				"id=" + id +
				", x=" + x +
				", y=" + y +
				", dir=" + dir +
				'}';
	}

	@Override
	public void handle() {
		if (this.id.equals(GameModel.getInstance().getMainTank().getId())) {
			return;
		}
		Tank t = GameModel.getInstance().findTankByUUID(this.id);
		if (t != null) {
			t.setMoving(true);
			t.setX(this.x);
			t.setY(this.y);
			t.setDir(this.dir);
		}
	}

	@Override
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
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			if (dos != null) {
				dos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	@Override
	public void parse(byte[] bytes) {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
		try {
			this.x = dis.readInt();
			this.y = dis.readInt();
			this.dir = Dir.values()[dis.readInt()];
			this.id = new UUID(dis.readLong(), dis.readLong());
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

	@Override
	public MsgType getMsgType() {
		return MsgType.TankStartMoving;
	}
}
