package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author 华安  mashuai_bj@si-tech.com.cn
 * @Title:
 * @Date: Create in 10:09 2018/4/3
 * @Description:
 */
public class LineBasedFrameDecoderClient {

    public void connect() throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LineBasedFrameDecoder(1024));
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());

                            p.addLast(new LineBasedFrameDecoderClientHandler());
                        }
                    });

            ChannelFuture future = b.connect("127.0.0.1", 8080).sync();

            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

        new LineBasedFrameDecoderClient().connect();
    }
} 