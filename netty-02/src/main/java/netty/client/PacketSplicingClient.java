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
public class PacketSplicingClient extends ChannelInboundHandlerAdapter { // (1){

    private Logger logger = LoggerFactory.getLogger(getClass());
    private int counter;
    private byte[] req;

    public PacketSplicingClient() {
        req = "Hello from client".getBytes();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("client channel active");
        ByteBuf message = null;
        // Send the message to Server
        for (int i = 0; i < 100; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
        logger.info("client send req...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        try {
            String body = new String(req, "UTF-8");
            logger.info("client channel read msg:{}", body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        logger.error("client caught exception", cause);
        ctx.close();
    }
} 