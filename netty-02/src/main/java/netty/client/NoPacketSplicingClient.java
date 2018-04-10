package netty.client;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author 华安  mashuai_bj@si-tech.com.cn
 * @Title:
 * @Date: Create in 16:35 2018/4/2
 * @Description:
 */
public class NoPacketSplicingClient extends ChannelInboundHandlerAdapter { // (1){

    private Logger logger = LoggerFactory.getLogger(getClass());
    private int count =0;
    private byte[] req;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send the message to Server
        for (int i = 0; i < 100; i++) {
            String msg = "hello from client " + i;
            logger.info("client send message:{}", msg);
            ctx.writeAndFlush(msg + System.getProperty("line.separator"));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        String body = (String) msg;
        count++;
        logger.info("client read msg:{}, count:{}", body, count);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        logger.error("client caught exception", cause);
        ctx.close();
    }
} 