package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 华安  mashua199086@163.com
 * @Title:
 * @Date: Create in 19:11 2018/3/31
 * @Description:
 */
public class Server {

    private int port;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public Server(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            //没有考虑tcp拆包\粘包的情况
//                          ChannelPipeline p = ch.pipeline();
//                          p.addLast(new PacketSplicingServerHandler());
                            //考虑tcp拆包\粘包的情况
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LineBasedFrameDecoder(1024));
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(new NoPacketSplicingServerHandler());
                        }
                    });
            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)
            logger.info("server bind port:{}", port);
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new Server(8379).run();
    }

}