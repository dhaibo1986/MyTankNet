package com.dhb.tank.mode;

import com.dhb.tank.comms.Dir;
import com.dhb.tank.comms.Group;
import com.dhb.tank.frame.TankFrame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class BulletNewMsg extends Msg{

	private int x;
	private int y;
	private Dir dir;
	private Group group;
	private UUID tankId;
	private UUID id;

	public BulletNewMsg(int x, int y, Dir dir, Group group, UUID tankId,UUID id) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.group = group;
		this.tankId = tankId;
		this.id = id;
	}

	public BulletNewMsg(Bullet b) {
		this.x = b.getX();
		this.y = b.getY();
		this.dir = b.getDir();
		this.group = b.getGroup();
		this.tankId = b.getTankId();
		this.id = b.getId();
	}


	public BulletNewMsg() {

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

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public UUID getTankId() {
		return tankId;
	}

	public void setTankId(UUID tankId) {
		this.tankId = tankId;
	}

	@Override
	public String toString() {
		return "BulletNewMsg{" +
				"x=" + x +
				", y=" + y +
				", dir=" + dir +
				", group=" + group +
				", tankId=" + tankId +
				", id=" + id +
				'}';
	}

	@Override
	public void handle() {
		if (this.tankId.equals(TankFrame.INSTANCE.getMainTank().getId())) {
			return;
		}
		Bullet b =new Bullet(this.x,this.y,this.dir,this.group,this.tankId,TankFrame.INSTANCE);
		b.setId(this.id);
		TankFrame.INSTANCE.addBullet(b);
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
			dos.writeInt(group.ordinal());
			dos.writeLong(tankId.getMostSignificantBits());
			dos.writeLong(tankId.getLeastSignificantBits());
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
			this.group = Group.values()[dis.readInt()];
			this.tankId = new UUID(dis.readLong(), dis.readLong());
			this.id =  new UUID(dis.readLong(), dis.readLong());
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
		return MsgType.BulletNew;
	}
}
