package com.my.nio.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 华安  mashuai_bj@si-tech.com.cn
 * @Title:
 * @Date: Create in 14:11 2018/4/2
 * @Description:
 */
public class ServerHandler implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean isStop;

    public ServerHandler(int port) {
        try {
            //用于监听客户端的请求,是所有客户端连接的父管道
            serverSocketChannel = ServerSocketChannel.open();
            //绑定监听端口,设置连接为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(8080), 1024);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("the server start at port : " + 8080);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void stop() {
        this.isStop = true;
    }

    @Override
    public void run() {
        SelectionKey key = null;
        while (!isStop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeySet.iterator();
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handlerKey(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (null != selector) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handlerKey(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int readByte = socketChannel.read(byteBuffer);
                if (readByte > 0) {
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println(body);
                } else if (readByte < 0) {
                    key.cancel();
                    socketChannel.close();
                }
            }


        }
    }


}