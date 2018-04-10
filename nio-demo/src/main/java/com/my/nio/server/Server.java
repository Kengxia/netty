package com.my.nio.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @author 华安  mashuai_bj@si-tech.com.cn
 * @Title:
 * @Date: Create in 14:05 2018/4/2
 * @Description:
 */
public class Server {

    public static void main(String[] args) {
        ServerHandler serverHandler = new ServerHandler(8080);
        new Thread(serverHandler).start();

    }
}