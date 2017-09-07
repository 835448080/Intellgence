package netty.Http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Created by qin on 17-8-22.
 */
public class HelloClientIntHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf result=(ByteBuf) msg;
        byte[] result1 =new byte[result.readableBytes()];
        result.readBytes(result1);
        result.release();
        System.out.println("Server said:"+new String(result1));
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String response="I am ok!";
        ByteBuf encoded=ctx.alloc().buffer(4*response.length());
        encoded.writeBytes(response.getBytes());
        ctx.write(encoded);
        ctx.flush();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String msg="Are you ok?";
        ByteBuf encoded=ctx.alloc().buffer(4*msg.length());
        encoded.writeBytes(msg.getBytes());
        ctx.writeAndFlush(encoded);
    }

}
