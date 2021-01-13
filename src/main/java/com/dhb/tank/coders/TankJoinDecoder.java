package com.dhb.tank.coders;

import com.dhb.tank.mode.TankJoinMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TankJoinDecoder  extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		//需要确认每个消息的长度 现在是33个字节
		if(in.readableBytes() < 33) {
			return;
		}
		TankJoinMsg msg = new TankJoinMsg();
		msg.parse(in.array());
		out.add(msg);
	}
}
