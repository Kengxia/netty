package com.my.io.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 华安  mashua199086@163.com
 * @Title:
 * @Date: Create in 9:52 2018/4/1
 * @Description:
 */
public class Server {

    private static final int PORT = 8080;
    static  Logger logger  = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args)  {

        // 监听指定的端口
        int port = 55533;
        ServerSocket server = null;
        InputStream inputStream =null;
        OutputStream outputStream =null;
        Socket socket = null;
        try {
            server = new ServerSocket(port);
            // server将一直等待连接的到来
            System.out.println("server将一直等待连接的到来");
            while (true){
                socket = server.accept();
                new Thread(new ServerHandler(socket)).start();
            }
//            socket = server.accept();
//            // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
//            inputStream = socket.getInputStream();
//            byte[] bytes = new byte[1024];
//            int len;
//            StringBuilder sb = new StringBuilder();
//            //只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
//            while ((len = inputStream.read(bytes)) != -1) {
//                // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
//                sb.append(new String(bytes, 0, len, "UTF-8"));
//            }
//            System.out.println("get message from client: " + sb);
//
//            outputStream = socket.getOutputStream();
//            outputStream.write("Hello Client,I get the message.".getBytes("UTF-8"));

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(null!=inputStream){
                    inputStream.close();
                }
                if(null!=outputStream){
                    outputStream.close();
                }
                if(null!=socket){
                    socket.close();
                }
                if(server!=null){
                    server.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
} 