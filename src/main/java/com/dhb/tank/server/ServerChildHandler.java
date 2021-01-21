package com.dhb.tank.server;

import com.dhb.tank.mode.Msg;
import com.dhb.tank.mode.TankExitMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerChildHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Server.clients.add(ctx.channel());
		ServerFrame.INSTANCE.updateServerMsg("channel [" + ctx.name() + "] 连接到服务端。");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel [" + ctx.name() + "] 连接关闭，被移除。");
		ServerFrame.INSTANCE.updateServerMsg("channel [" + ctx.name() + "] 连接关闭，被移除。");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ServerFrame.INSTANCE.updateServerMsg("channel [" + ctx.name() + "] 互相异常。" + cause.getMessage());
		Server.clients.remove(ctx.channel());
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ServerFrame.INSTANCE.updateClientMsg("channel [" + ctx.name() + "] 收到消息。" + msg.toString());
		//发现有客户端退出
		if(msg instanceof TankExitMsg) {
			TankExitMsg extMsg = (TankExitMsg) msg;
			Server.clients.remove(ctx.channel());
			ctx.close();
			ServerFrame.INSTANCE.updateServerMsg("服务端关闭需要退出的连接：channel:["+ctx.name()+"] id:["+extMsg.getId()+"]");
		}
		//转发消息到其他客户端
		Server.clients.writeAndFlush(msg);


/*		ByteBuf buf = null;
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
		}*/
	}
}
