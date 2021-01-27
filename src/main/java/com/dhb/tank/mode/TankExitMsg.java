package com.dhb.tank.mode;

import com.dhb.tank.frame.TankFrame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
*@author dhaibo1986
*@description  坦克退出消息体
*@date  2021/1/21 13:42
*/
public class TankExitMsg  extends Msg{
    //关闭退出的坦克ID
	private UUID id;

	public TankExitMsg(UUID id) {
		this.id = id;
	}

	public TankExitMsg(Tank t) {
		this.id = t.getId();
	}

	public TankExitMsg() {

	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "TankExitMsg{" +
				"id=" + id +
				'}';
	}

	@Override
	public void handle() {
		if (this.id.equals(GameModel.getInstance().getMainTank().getId())) {
			return;
		}
		Tank t = GameModel.getInstance().findTankByUUID(this.id);
		//如果坦克存在，则将当前坦克移除
		if (t != null) {
			GameModel.getInstance().remove(t);
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
		return MsgType.TankExit;
	}
}
