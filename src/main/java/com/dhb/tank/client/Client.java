package com.dhb.tank.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

	private Channel channel = null;

	private Client client = null;

	private EventLoopGroup group = null;

	public static void main(String[] args) {

	}

	public void connect() {
		group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		try {
			ChannelFuture f = b.group(group).channel(NioSocketChannel.class)
					.handler(new ClientChannelInitializer())
					.connect("localhost", 8888);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

}
