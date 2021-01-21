import com.dhb.tank.coders.TankMsgDecoder;
import com.dhb.tank.coders.TankMsgEncoder;
import com.dhb.tank.mode.MsgType;
import com.dhb.tank.mode.TankDieMsg;
import com.dhb.tank.mode.TankExitMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class TankExitMsgTest {


	@Test
	void testEncoder() {
		EmbeddedChannel ch = new EmbeddedChannel();
		UUID id = UUID.randomUUID();
		TankExitMsg msg = new TankExitMsg(id);
		ch.pipeline()
				.addLast(new TankMsgEncoder());
		ch.writeOutbound(msg);


		ByteBuf buf = (ByteBuf) ch.readOutbound();
		MsgType msgType = MsgType.values()[buf.readInt()];
		Assertions.assertEquals(MsgType.TankExit, msgType);

		int length = buf.readInt();
		Assertions.assertEquals(16, length);

		UUID uuid = new UUID(buf.readLong(), buf.readLong());
		Assertions.assertEquals(id, uuid);
	}

	@Test
	void testDecoder() {
		EmbeddedChannel ch = new EmbeddedChannel();
		UUID id = UUID.randomUUID();
		TankExitMsg msg = new TankExitMsg(id);
		ch.pipeline()
				.addLast(new TankMsgDecoder());
		ByteBuf buf = Unpooled.buffer();
		buf.writeInt(MsgType.TankExit.ordinal());
		byte[] bytes = msg.toBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);

		ByteBuf dbuf = buf.duplicate();
		ch.writeInbound(dbuf);
		ch.flush();
		TankExitMsg msgR = (TankExitMsg) ch.readInbound();
		Assertions.assertEquals(msg.getId(), msgR.getId());
	}

}
