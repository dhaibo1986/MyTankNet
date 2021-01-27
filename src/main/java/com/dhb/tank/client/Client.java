package com.dhb.tank.client;

import com.dhb.tank.mode.Msg;
import com.dhb.tank.mode.TankJoinMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
*@author dhaibo1986
*@description
*@date  2021/1/21 13:57
*/
public class Client {

	public static final Client INSTANCE = new Client();

	private Client() {

	}

	private Channel channel = null;

	private Client client = null;

	private EventLoopGroup group = null;


	public void connect() {
		group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		try {
			ChannelFuture f = b.group(group).channel(NioSocketChannel.class)
					.handler(new ClientChannelInitializer())
					.connect("172.16.20.156", 8888);
			channel = f.channel();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

	public void stop() {
		//关闭连接
		if(null != channel) {
			if(channel.isActive()){
				channel.flush();
				channel.close();
			}
		}
		//关闭客户端线程池
		if(null != group) {
			group.shutdownGracefully();
		}
	}


	public void send(Msg msg) {
		channel.writeAndFlush(msg);
	}
}
