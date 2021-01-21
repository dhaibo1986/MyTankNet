import com.dhb.tank.coders.TankMsgDecoder;
import com.dhb.tank.coders.TankMsgEncoder;
import com.dhb.tank.mode.MsgType;
import com.dhb.tank.mode.TankDieMsg;
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
		UUID bulledId = UUID.randomUUID();
		TankDieMsg msg = new TankDieMsg(id, bulledId);
		ch.pipeline()
				.addLast(new TankMsgEncoder());
		ch.writeOutbound(msg);


		ByteBuf buf = (ByteBuf) ch.readOutbound();
		MsgType msgType = MsgType.values()[buf.readInt()];
		Assertions.assertEquals(MsgType.TankDie, msgType);

		int length = buf.readInt();
		Assertions.assertEquals(32, length);

		UUID uuid = new UUID(buf.readLong(), buf.readLong());
		UUID bulledUuid = new UUID(buf.readLong(), buf.readLong());
		Assertions.assertEquals(id, uuid);
		Assertions.assertEquals(bulledId, bulledUuid);
	}

	@Test
	void testDecoder() {
		EmbeddedChannel ch = new EmbeddedChannel();
		UUID id = UUID.randomUUID();
		UUID bulledId = UUID.randomUUID();
		TankDieMsg msg = new TankDieMsg(id, bulledId);
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
		Assertions.assertEquals(msg.getId(), msgR.getId());
		Assertions.assertEquals(msg.getBulledId(), msgR.getBulledId());
	}

}
