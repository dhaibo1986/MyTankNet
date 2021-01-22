package com.dhb.tank.coders;

import com.dhb.tank.mode.BulletNewMsg;
import com.dhb.tank.mode.Msg;
import com.dhb.tank.mode.MsgType;
import com.dhb.tank.mode.TankDieMsg;
import com.dhb.tank.mode.TankDirChangedMsg;
import com.dhb.tank.mode.TankExitMsg;
import com.dhb.tank.mode.TankJoinMsg;
import com.dhb.tank.mode.TankStartMovingMsg;
import com.dhb.tank.mode.TankStopMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TankMsgDecoder extends ByteToMessageDecoder {

	private static final Map<MsgType,Msg> msgMap = new HashMap<>();
	static{
		msgMap.put(MsgType.TankJoin,new TankJoinMsg());
		msgMap.put(MsgType.TankStartMoving,new TankStartMovingMsg());
		msgMap.put(MsgType.TankStop,new TankStopMsg());
		msgMap.put(MsgType.TankDirChanged,new TankDirChangedMsg());
		msgMap.put(MsgType.BulletNew,new BulletNewMsg());
		msgMap.put(MsgType.TankDie,new TankDieMsg());
		msgMap.put(MsgType.TankExit,new TankExitMsg());
	}

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
//		Class localClass = Class.forName("com.dhb.tank.mode."+msgType.toString()+"Msg");
//		msg = (Msg)localClass.newInstance();

		Msg msg = null;
		//此处可用反射替换
		msg = msgMap.get(msgType);
		msg.parse(bytes);
		out.add(msg);



		/*switch (msgType) {
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
			case TankExit:
				msg = new TankExitMsg();
				msg.parse(bytes);
				out.add(msg);
				break;
			default:
				break;
		}*/

	}
}
