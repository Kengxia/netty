package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty.server.NoPacketSplicingServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 华安  mashuai_bj@si-tech.com.cn
 * @Title:
 * @Date: Create in 16:23 2018/4/2
 * @Description:
 */
public class Client {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String host;
    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void send() throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //没有考虑tcp拆包\粘包的情况
//                            ChannelPipeline p = ch.pipeline();
//                            p.addLast(new PacketSplicingClient());
                            //考虑tcp拆包\粘包的情况
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LineBasedFrameDecoder(1024));
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(new NoPacketSplicingClient());
                        }
                    });
            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            logger.info("client connect to host:{}, port:{}", host, port);

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

        new Client("127.0.0.1", 8379).send();
    }

} 