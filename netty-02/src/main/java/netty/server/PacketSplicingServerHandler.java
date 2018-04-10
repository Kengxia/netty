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
public class PacketSplicingServerHandler extends ChannelInboundHandlerAdapter {

    private int counter;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)

        logger.info("server channel read...");

        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        try {
            String body = new String(req, "UTF-8");
            logger.info("server channel read msg:{}", body);
            logger.info("The time receive order : "+body+ "the counter is "+ ++counter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String response = "hello from server";
        ByteBuf resp = Unpooled.copiedBuffer(response.getBytes());
        ctx.write(resp);
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