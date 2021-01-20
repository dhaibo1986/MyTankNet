package com.dhb.tank.mode;

import com.dhb.tank.comms.Dir;
import com.dhb.tank.frame.TankFrame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class TankDirChangedMsg extends Msg{

	private int x;
	private int y;
	private Dir dir;
	private UUID id;

	public TankDirChangedMsg(int x, int y, Dir dir, UUID id) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.id = id;
	}

	public TankDirChangedMsg(Tank t) {
		this.x = t.getX();
		this.y = t.getY();
		this.dir = t.getDir();
		this.id = t.getId();
	}

	public TankDirChangedMsg() {

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

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "TankDirChangedMsg{" +
				"x=" + x +
				", y=" + y +
				", dir=" + dir +
				", id=" + id +
				'}';
	}

	@Override
	public void handle() {
		if (this.id.equals(TankFrame.INSTANCE.getMainTank().getId())) {
			return;
		}
		Tank t = TankFrame.INSTANCE.findByUUID(this.id);
		if (t != null) {
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
		return MsgType.TankDirChanged;
	}
}
