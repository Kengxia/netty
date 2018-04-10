package netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 华安  mashuai_bj@si-tech.com.cn
 * @Title:
 * @Date: Create in 16:35 2018/4/2
 * @Description:
 */
public class NoPacketSplicingServerHandler extends ChannelInboundHandlerAdapter {

    private int count = 0;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)

        count++;
        String body = (String) msg;
        logger.info("server read msg:{}, count:{}", body, count);
        String response = "hello from server"+System.getProperty("line.separator");
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("server channel read complete");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        logger.error("server caught exception", cause);
        ctx.close();
    }


}