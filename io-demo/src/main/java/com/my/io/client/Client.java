package com.my.io.client;

import java.io.*;
import java.net.Socket;

/**
 * @author 华安  mashua199086@163.com
 * @Title:
 * @Date: Create in 9:52 2018/4/1
 * @Description:
 */
public class Client {

    public static void main(String[] args) {
        // 要连接的服务端IP地址和端口
        String host = "127.0.0.1";
        int port = 55533;
        // 与服务端建立连接
        InputStream inputStream =null;
        OutputStream outputStream =null;
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            outputStream = socket.getOutputStream();
            // 建立连接后获得输出流
            String message = "你好yiwangzhibujian";
            //首先需要计算得知消息的长度
            byte[] sendBytes = message.getBytes("UTF-8");
            //然后将消息的长度优先发送出去
            outputStream.write(sendBytes.length >>8);  //计算消息长度 类似协议
            outputStream.write(sendBytes.length);
            //然后将消息再次发送出去
            outputStream.write(sendBytes);
            outputStream.flush();
            //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
            socket.shutdownOutput();

            inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                sb.append(new String(bytes, 0, len, "UTF-8"));
            }
            System.out.println("get message from server: " + sb);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}