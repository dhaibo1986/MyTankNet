package com.dhb.tank.coders;

import com.dhb.tank.mode.TankJoinMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TankJoinMsgEncoder extends MessageToByteEncoder<TankJoinMsg> {

	@Override
	protected void encode(ChannelHandlerContext ctx, TankJoinMsg msg, ByteBuf out) throws Exception {
		out.writeBytes(msg.toBytes());
	}
}
