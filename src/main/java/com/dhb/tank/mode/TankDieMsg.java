package com.dhb.tank.mode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class TankDieMsg extends Msg {

	private UUID id;
	//被谁击中
	private UUID bulledId;

	public TankDieMsg(UUID id, UUID bulledId) {
		this.id = id;
		this.bulledId = bulledId;
	}


	public TankDieMsg() {

	}

	public UUID getBulledId() {
		return bulledId;
	}

	public void setBulledId(UUID bulledId) {
		this.bulledId = bulledId;
	}


	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "TankDieMsg{" +
				"id=" + id +
				", bulledId=" + bulledId +
				'}';
	}

	@Override
	public void handle() {
		if (this.id.equals(GameModel.getInstance().getMainTank().getId())) {
			return;
		}
		Tank t = GameModel.getInstance().findTankByUUID(this.id);
		//如果坦克存在，则将当前坦克移除，并产生爆炸效果对象Explode
		if (t != null) {
			GameModel.getInstance().remove(t);
			GameModel.getInstance().add(new Explode(t));
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
			dos.writeLong(id.getMostSignificantBits());
			dos.writeLong(id.getLeastSignificantBits());
			dos.writeLong(bulledId.getMostSignificantBits());
			dos.writeLong(bulledId.getLeastSignificantBits());
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
			this.id = new UUID(dis.readLong(), dis.readLong());
			this.bulledId = new UUID(dis.readLong(), dis.readLong());
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
		return MsgType.TankDie;
	}
}
