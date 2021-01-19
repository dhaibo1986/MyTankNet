package com.dhb.tank.client;

import com.dhb.tank.coders.TankMsgDecoder;
import com.dhb.tank.coders.TankMsgEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline()
				.addLast(new TankMsgEncoder())
				.addLast(new TankMsgDecoder())
				.addLast(new ClientHandler());
	}
}
