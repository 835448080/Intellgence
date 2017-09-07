package netty.Http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by qin on 17-8-21.
 */
public class Nio {
    public void start(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        public void initChannel(NioSocketChannel ch) throws Exception {
//                             //server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
//                            ch.pipeline().addLast(new HttpResponseEncoder());
//                            // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
//                            ch.pipeline().addLast(new HttpRequestDecoder());
//                            ch.pipeline().addLast(new HttpObjectAggregator(65535));
                            ch.pipeline().addLast(new ChunkedWriteHandler());//
//                            ch.pipeline().addLast((io.netty.channel.ChannelHandler) new ChannelHandler());
//                            ch.pipeline().addLast(new InboundHandler1());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            Channel channel = b.bind(port).sync().channel();
            //绑定端口（实际上是创建serversocketchannnel，并注册到eventloop上），同步等待完成，返回相应channel
//            ChannelFuture f = b.bind(port).sync();

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Nio server=new Nio();
        server.start(30000);
    }
}
