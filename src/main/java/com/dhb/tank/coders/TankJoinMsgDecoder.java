package com.dhb.tank.coders;

import com.dhb.tank.comms.Dir;
import com.dhb.tank.comms.Group;
import com.dhb.tank.mode.TankJoinMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.UUID;

public class TankJoinMsgDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		//需要确认每个消息的长度 现在是33个字节
		if(in.readableBytes() < 33) {
			return;
		}
		TankJoinMsg msg = new TankJoinMsg();
		msg.setX(in.readInt());
		msg.setY(in.readInt());
		msg.setDir(Dir.values()[in.readInt()]);
		msg.setMoving(in.readBoolean());
		msg.setGroup(Group.values()[in.readInt()]);
		msg.setId(new UUID(in.readLong(),in.readLong()));
		out.add(msg);
	}
}
