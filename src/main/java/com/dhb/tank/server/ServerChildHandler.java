package com.dhb.tank.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerChildHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Server.clients.add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel ["+ctx.name()+"] 连接关闭，被移除。");
		ServerFrame.INSTANCE.updateServerMsg("channel ["+ctx.name()+"] 连接关闭，被移除。");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		Server.clients.remove(ctx.channel());
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = null;
		if(msg instanceof ByteBuf) {
			try {
				buf = (ByteBuf) msg;
				System.out.println("==============================");
				byte[] bytes = new byte[buf.readableBytes()];
				buf.getBytes(buf.readerIndex(), bytes);
				String s = new String(bytes);
				ServerFrame.INSTANCE.updateClientMsg(s);
				if("_bye_".equals(s)){
					System.out.println("客户端"+ctx.name()+"要求退出!");
					ServerFrame.INSTANCE.updateServerMsg("客户端"+ctx.name()+"要求退出!");
					Server.clients.remove(ctx.channel());
					ctx.close();
				}else {
					System.out.println("server receive:"+new String(bytes));
					ServerFrame.INSTANCE.updateServerMsg("server receive:"+new String(bytes));
					Server.clients.writeAndFlush(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}
		}
	}
}
