import com.dhb.tank.coders.TankJoinMsgDecoder;
import com.dhb.tank.coders.TankJoinMsgEncoder;
import com.dhb.tank.comms.Dir;
import com.dhb.tank.comms.Group;
import com.dhb.tank.mode.MsgType;
import com.dhb.tank.mode.TankJoinMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class TankJoinMsgTest {

	@Test
	void testEncoder() {
		EmbeddedChannel ch = new EmbeddedChannel();
		UUID id = UUID.randomUUID();
		TankJoinMsg msg = new TankJoinMsg(5,10, Dir.DOWN,true, Group.GOOD,id);
		ch.pipeline()
				.addLast(new TankJoinMsgEncoder());
		ch.writeOutbound(msg);

		ByteBuf buf = (ByteBuf) ch.readOutbound();
		MsgType msgType = MsgType.values()[buf.readInt()];
		Assertions.assertEquals(MsgType.TankJoin,msgType);

		int length = buf.readInt();
		Assertions.assertEquals(33,length);

		int x = buf.readInt();
		int y = buf.readInt();
		int dirOrdinal = buf.readInt();
		Dir dir = Dir.values()[dirOrdinal];
		boolean moving = buf.readBoolean();
		int groupOrdinal = buf.readInt();
		Group group = Group.values()[groupOrdinal];
		UUID uuid = new UUID(buf.readLong(),buf.readLong());

		Assertions.assertEquals(5,x);
		Assertions.assertEquals(10,y);
		Assertions.assertEquals(Dir.DOWN,dir);
		Assertions.assertEquals(true,moving);
		Assertions.assertEquals(Group.GOOD,group);
		Assertions.assertEquals(id,uuid);
	}

	@Test
	void testDecoder() {
		EmbeddedChannel ch = new EmbeddedChannel();
		UUID id = UUID.randomUUID();
		TankJoinMsg msg = new TankJoinMsg(5,10, Dir.DOWN,true, Group.GOOD,id);
		ch.pipeline()
				.addLast(new TankJoinMsgDecoder());
		ByteBuf buf = Unpooled.buffer();
		buf.writeInt(MsgType.TankJoin.ordinal());
		byte[] bytes = msg.toBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);

		ByteBuf dbuf = buf.duplicate();
		ch.writeInbound(dbuf);
		ch.flush();
		TankJoinMsg msgR = (TankJoinMsg) ch.readInbound();
		Assertions.assertEquals(msg.getX(),msgR.getX());
		Assertions.assertEquals(msg.getY(),msgR.getY());
		Assertions.assertEquals(msg.getDir(),msgR.getDir());
		Assertions.assertEquals(msg.isMoving(),msgR.isMoving());
		Assertions.assertEquals(msg.getGroup(),msgR.getGroup());
		Assertions.assertEquals(msg.getId(),msgR.getId());
	}

}
