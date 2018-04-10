package com.my.nio.client;

import java.io.IOException;
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
 * @Date: Create in 14:05 2018/4/2
 * @Description:
 */
public class Client {

    private static final String host="127.0.0.1";
    private static final int port =8080;

    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            if(socketChannel.connect(new InetSocketAddress(host,port))){
                socketChannel.register(selector, SelectionKey.OP_READ);
                doWrite(socketChannel);
            }else{
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
            SelectionKey key =null;
            while(true){
                selector.select(1000);
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeySet.iterator();
                while(it.hasNext()){
                    key = it.next();
                    it.remove();
                    handlerKey(selector,key);
                    try {
                    }catch (Exception e){
                        if(key!=null){
                            key.cancel();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void doWrite(SocketChannel sc) throws IOException {
        byte[] req = "sssssss".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(req.length);
        byteBuffer.put(req);
        byteBuffer.flip();
        sc.write(byteBuffer);
        if(!byteBuffer.hasRemaining()){
            System.out.println("Send order 2 server success");
        }
    }

    private static void handlerKey(Selector selector,SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isValid()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                if (key.isConnectable()) {
                    if (socketChannel.finishConnect()) {
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        doWrite(socketChannel);
                    }
                }else{
                    System.exit(1);
                }
            }
        }
    }

} 