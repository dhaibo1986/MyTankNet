package com.dhb.tank.coders;

import com.dhb.tank.mode.MsgType;
import com.dhb.tank.mode.TankJoinMsg;
import com.dhb.tank.mode.TankStartMovingMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TankMsgDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		//需要确认每个消息的长度
		if (in.readableBytes() < 8) {
			return;
		}
		in.markReaderIndex();
		MsgType msgType = MsgType.values()[in.readInt()];
		int length = in.readInt();
		if (in.readableBytes() < length) {
			in.resetReaderIndex();
			return;
		}
		byte[] bytes = new byte[length];
		in.readBytes(bytes);

		switch (msgType) {
			case TankJoin:
				TankJoinMsg joinMsg = new TankJoinMsg();
				joinMsg.parse(bytes);
				out.add(joinMsg);
				break;
			case TankStartMoving:
				TankStartMovingMsg startMovingMsg = new TankStartMovingMsg();
				startMovingMsg.parse(bytes);
				out.add(startMovingMsg);
				break;
			default:
				break;
		}

	}
}
