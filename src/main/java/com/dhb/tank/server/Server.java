package com.dhb.tank.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
*@author
*@description
*@date  2021/1/13 15:36
*/
public class Server {

	public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;


	/**
	 * 关闭服务端
	 */
	public void serverStop() {
		for (Channel c : clients) {
			if (c.isActive()) {
				c.flush();
				c.close();
			}
		}
		if (null != workerGroup) {
			workerGroup.shutdownGracefully();
		}
		if (null != bossGroup) {
			bossGroup.shutdownGracefully();
		}
	}

	/**
	 * 启动服务端
	 */
	public void serverStart() {
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup(2);
		ServerBootstrap b = new ServerBootstrap();
		try {
			ChannelFuture f = b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new ServerChildHandler());
						}
					})
					.bind(8888)
					.sync();
			System.out.println("server started!");
			ServerFrame.INSTANCE.updateServerMsg("server started!");
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
