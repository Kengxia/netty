package com.my.io.server;

import java.io.*;
import java.net.Socket;

/**
 * @author 华安  mashuai_bj@si-tech.com.cn
 * @Title:
 * @Date: Create in 10:08 2018/4/1
 * @Description:
 */
public class ServerHandler implements Runnable {
    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = this.socket.getInputStream();
            int first = inputStream.read();
            byte[] bytes =null;
            //如果读取的值为-1 说明到了流的末尾，Socket已经被关闭了，此时将不能再去读取
            if(first!=-1){
                int second = inputStream.read();
                int length = (first << 8) + second;
                // 然后构造一个指定长的byte数组
                bytes = new byte[length];
                // 然后读取指定长度的消息即可
                inputStream.read(bytes);
                System.out.println("get message from client: " + new String(bytes, "UTF-8"));
            }
//            byte[] bytes = new byte[1024];
//            int len;
//            StringBuilder sb = new StringBuilder();
//            //只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
//            while ((len = inputStream.read(bytes)) != -1) {
//                // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
//                sb.append(new String(bytes, 0, len, "UTF-8"));
//            }
            outputStream = socket.getOutputStream();
            outputStream.write("Hello Client,I get the message.".getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.socket = null;
            }
        }
    }
}