package netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 华安  mashuai_bj@si-tech.com.cn
 * @Title:
 * @Date: Create in 10:11 2018/4/3
 * @Description:
 */
public class LineBasedFrameDecoderClientHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private int count =0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send the message to Server
        for(int i=0; i<100; i++){

            String msg = "hello from client "+i;
            logger.info("client send message:{}", msg);

            ctx.writeAndFlush(msg+System.getProperty("line.separator"));
        }

    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        String body = (String) msg;
        count++;
        logger.info("client read msg:{}, count:{}", body, count);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("client caught exception", cause);
        ctx.close();
    }
} 