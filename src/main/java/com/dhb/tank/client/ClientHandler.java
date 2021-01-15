package com.dhb.tank.client;

import com.dhb.tank.frame.TankFrame;
import com.dhb.tank.mode.Tank;
import com.dhb.tank.mode.TankJoinMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ClientHandler extends SimpleChannelInboundHandler<TankJoinMsg> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TankJoinMsg msg) throws Exception {
		if(msg.getId().equals(TankFrame.INSTANCE.getMainTank().getId())||
		TankFrame.INSTANCE.findByUUID(msg.getId()) != null) {
			return;
		}
		System.out.println(msg);
		Tank t = new Tank(msg);
		TankFrame.INSTANCE.addTank(t);

		ctx.writeAndFlush(new TankJoinMsg(TankFrame.INSTANCE.getMainTank()));
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(new TankJoinMsg(TankFrame.INSTANCE.getMainTank()));
	}
}
