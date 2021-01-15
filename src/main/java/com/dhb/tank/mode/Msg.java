package com.dhb.tank.mode;

public abstract class Msg {
	public abstract void handle();
	public abstract byte[] toBytes();

}
