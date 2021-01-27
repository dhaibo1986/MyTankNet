package com.dhb.tank.client;

import com.dhb.tank.mode.GameModel;
import com.dhb.tank.mode.Msg;
import com.dhb.tank.mode.TankJoinMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ClientHandler extends SimpleChannelInboundHandler<Msg> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Msg msg) throws Exception {
		System.out.println(msg);
		msg.handle();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(new TankJoinMsg(GameModel.getInstance().getMainTank()));
	}
}
