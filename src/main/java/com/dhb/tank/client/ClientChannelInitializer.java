package com.dhb.tank.client;

import com.dhb.tank.coders.TankJoinMsgDecoder;
import com.dhb.tank.coders.TankJoinMsgEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline()
				.addLast(new TankJoinMsgEncoder())
				.addLast(new TankJoinMsgDecoder())
				.addLast(new ClientHandler());
	}
}
