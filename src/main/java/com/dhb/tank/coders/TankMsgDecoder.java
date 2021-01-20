package com.dhb.tank.coders;

import com.dhb.tank.mode.BulletNewMsg;
import com.dhb.tank.mode.Msg;
import com.dhb.tank.mode.MsgType;
import com.dhb.tank.mode.TankDieMsg;
import com.dhb.tank.mode.TankDirChangedMsg;
import com.dhb.tank.mode.TankJoinMsg;
import com.dhb.tank.mode.TankStartMovingMsg;
import com.dhb.tank.mode.TankStopMsg;
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

		Msg msg = null;
		//此处可用反射替换
//			msg = (Msg) Class.forName(msgType.toString()+"Msg").newInstance();


		switch (msgType) {
			case TankJoin:
				msg = new TankJoinMsg();
				msg.parse(bytes);
				out.add(msg);
				break;
			case TankStartMoving:
				msg = new TankStartMovingMsg();
				msg.parse(bytes);
				out.add(msg);
				break;
			case TankStop:
				msg = new TankStopMsg();
				msg.parse(bytes);
				out.add(msg);
				break;
			case TankDirChanged:
				msg = new TankDirChangedMsg();
				msg.parse(bytes);
				out.add(msg);
				break;
			case BulletNew:
				msg = new BulletNewMsg();
				msg.parse(bytes);
				out.add(msg);
				break;
			case TankDie:
				msg = new TankDieMsg();
				msg.parse(bytes);
				out.add(msg);
				break;
			default:
				break;
		}

	}
}
