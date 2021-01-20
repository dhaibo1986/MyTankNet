import com.dhb.tank.coders.TankMsgDecoder;
import com.dhb.tank.coders.TankMsgEncoder;
import com.dhb.tank.comms.Dir;
import com.dhb.tank.comms.Group;
import com.dhb.tank.mode.BulletNewMsg;
import com.dhb.tank.mode.MsgType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class BulletNewMsgTest {

	@Test
	void testEncoder() {
		EmbeddedChannel ch = new EmbeddedChannel();
		UUID id = UUID.randomUUID();
		BulletNewMsg msg = new BulletNewMsg(5, 10, Dir.DOWN, Group.GOOD, id);
		ch.pipeline()
				.addLast(new TankMsgEncoder());
		ch.writeOutbound(msg);


		ByteBuf buf = (ByteBuf) ch.readOutbound();
		MsgType msgType = MsgType.values()[buf.readInt()];
		Assertions.assertEquals(MsgType.BulletNew, msgType);

		int length = buf.readInt();
		Assertions.assertEquals(32, length);

		int x = buf.readInt();
		int y = buf.readInt();
		int dirOrdinal = buf.readInt();
		Dir dir = Dir.values()[dirOrdinal];
		int groupOrdinal = buf.readInt();
		Group group = Group.values()[groupOrdinal];
		UUID uuid = new UUID(buf.readLong(), buf.readLong());

		Assertions.assertEquals(5, x);
		Assertions.assertEquals(10, y);
		Assertions.assertEquals(Dir.DOWN, dir);
		Assertions.assertEquals(Group.GOOD, group);
		Assertions.assertEquals(id, uuid);
	}

	@Test
	void testDecoder() {
		EmbeddedChannel ch = new EmbeddedChannel();
		UUID id = UUID.randomUUID();
		BulletNewMsg msg = new BulletNewMsg(5, 10, Dir.DOWN, Group.GOOD, id);
		ch.pipeline()
				.addLast(new TankMsgDecoder());
		ByteBuf buf = Unpooled.buffer();
		buf.writeInt(MsgType.BulletNew.ordinal());
		byte[] bytes = msg.toBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);

		ByteBuf dbuf = buf.duplicate();
		ch.writeInbound(dbuf);
		ch.flush();
		BulletNewMsg msgR = (BulletNewMsg) ch.readInbound();
		Assertions.assertEquals(msg.getX(), msgR.getX());
		Assertions.assertEquals(msg.getY(), msgR.getY());
		Assertions.assertEquals(msg.getDir(), msgR.getDir());
		Assertions.assertEquals(msg.getGroup(), msgR.getGroup());
		Assertions.assertEquals(msg.getId(), msgR.getId());
	}
}
