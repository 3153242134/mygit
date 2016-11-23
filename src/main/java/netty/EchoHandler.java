package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // Discard the received data silently.
    	
    	
    	byte[] msgBytes = new byte[((ByteBuf) msg).readableBytes()];
    	((ByteBuf) msg).readBytes(msgBytes);
    	System.out.println("read date" + new String(msgBytes));
    	
    	
    	ByteBuf buf = Unpooled.buffer(("recieve msg : " + msg).getBytes().length);
		buf.writeBytes(("recieve msg : " + msg).getBytes());
    	ctx.writeAndFlush(buf);
        ((ByteBuf) msg).release(); // (3)
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
