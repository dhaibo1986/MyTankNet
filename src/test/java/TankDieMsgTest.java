import com.dhb.tank.coders.TankMsgDecoder;
import com.dhb.tank.coders.TankMsgEncoder;
import com.dhb.tank.comms.Dir;
import com.dhb.tank.mode.MsgType;
import com.dhb.tank.mode.TankDieMsg;
import com.dhb.tank.mode.TankDirChangedMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class TankDieMsgTest {


	@Test
	void testEncoder() {
		EmbeddedChannel ch = new EmbeddedChannel();
		UUID id = UUID.randomUUID();
		TankDieMsg msg = new TankDieMsg(5, 10, id);
		ch.pipeline()
				.addLast(new TankMsgEncoder());
		ch.writeOutbound(msg);


		ByteBuf buf = (ByteBuf) ch.readOutbound();
		MsgType msgType = MsgType.values()[buf.readInt()];
		Assertions.assertEquals(MsgType.TankDie, msgType);

		int length = buf.readInt();
		Assertions.assertEquals(24, length);

		int x = buf.readInt();
		int y = buf.readInt();
		UUID uuid = new UUID(buf.readLong(), buf.readLong());

		Assertions.assertEquals(5, x);
		Assertions.assertEquals(10, y);
		Assertions.assertEquals(id, uuid);
	}

	@Test
	void testDecoder() {
		EmbeddedChannel ch = new EmbeddedChannel();
		UUID id = UUID.randomUUID();
		TankDieMsg msg = new TankDieMsg(5, 10,  id);
		ch.pipeline()
				.addLast(new TankMsgDecoder());
		ByteBuf buf = Unpooled.buffer();
		buf.writeInt(MsgType.TankDie.ordinal());
		byte[] bytes = msg.toBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);

		ByteBuf dbuf = buf.duplicate();
		ch.writeInbound(dbuf);
		ch.flush();
		TankDieMsg msgR = (TankDieMsg) ch.readInbound();
		Assertions.assertEquals(msg.getX(), msgR.getX());
		Assertions.assertEquals(msg.getY(), msgR.getY());
		Assertions.assertEquals(msg.getId(), msgR.getId());
	}

}
