package netty.Http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created by qin on 17-8-29.
 */
public class HttpServer {

    //setStatus
    //URI uri = new URI("/setStatus");
    // String msg = "{'borg':'AAAA','devicestatus':'00','adn':'2','bgroup':'20','bfloor':'1','bus':'1'}";



    //    private static Logger logger = Logger.getLogger(HttpServer.class);
    private int port;

    public HttpServer(int port){
        this.port = port;
    }

    public void start() {
//        logger.debug("http:"+port+"startup===========");
        EventLoopGroup workerGroup = new NioEventLoopGroup();//线程组　请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();//
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(workerGroup, bossGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG,4096)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new HttpResponseEncoder());//加密
                            channel.pipeline().addLast(new HttpRequestDecoder());//解密
                            channel.pipeline().addLast(new HttpClientCodec());//聚合无所谓
                            channel.pipeline().addLast(new HttpObjectAggregator(65536));//最大发包量
                            channel.pipeline().addLast(new HttpOutboundHandler());//发数据
                            channel.pipeline().addLast(new HttpInboundHandler());//读数据
                        }
                    });

            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
//            logger.error(port+"\t",e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
//            logger.info("http server is over...............");
        }

    }
}

