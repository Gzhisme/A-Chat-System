package com.gzh.client.service;

import com.gzh.common.Message;
import com.gzh.common.MessageType;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author 高梓航
 * @version 1.0.0
 * @createTime 2021年10月03日 19:17:00
 * 客户端持有Socket并与服务端连接的线程类
 */
public class ClientConnectServerThread extends Thread {
    private Socket socket;

    //通过Socket对象构造类对象
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    //重写run方法
    //Thread需要在后台和服务器保持通信状态
    @Override
    public void run() {
        while (true) {
//            System.out.println("客户端线程，等待服务端发送数据...");
            try {
                ObjectInputStream ois = new ObjectInputStream((socket.getInputStream()));
                Message message = (Message) ois.readObject();//若服务端未发送Message对象，则线程阻塞

                /*根据message对象类型进行对应的处理*/
                //返回在线用户列表
                if(message.getMesType().equals(MessageType.MESSAGE_RETURN_ONLINE_USER)) {

                    //取出在线用户列表信息
                    String[] onlineUserList = message.getContent().split(" ");
                    System.out.println("========当前在线用户列表========");
                    for (int i = 0; i < onlineUserList.length; i++) {
                        System.out.println("用户：" + onlineUserList[i]);
                    }
                }

                //私聊
                else if(message.getMesType().equals(MessageType.MESSAGE_PRIVATE_MES)) {

                    //取出message的content并输出
                    System.out.println(message.getSender() + "在" + message.getSendTime() + "对你说：");
                    System.out.println(message.getContent());
                }

                //群发
                else if(message.getMesType().equals(MessageType.MESSAGE_PUBLIC_MES)) {

                    System.out.println(message.getSender() + "在" + message.getSendTime() + "群发说：");
                    System.out.println(message.getContent());
                }

                //文件IO
                else if(message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {

                    System.out.println(message.getSender() + "在" + message.getSendTime() +
                            "给你发了" + message.getSrc() + "文件 到你的电脑" + message.getDest());

                    //将message的字节数组取出，通过文件输出流写到磁盘
                    FileOutputStream fos = new FileOutputStream(message.getDest());
                    fos.write(message.getFileBytes());
                    fos.close();
                    System.out.println("\n文件保存成功~");
                }

                //Server推送 or 离线消息通报
                else if(message.getMesType().equals(MessageType.MESSAGE_TO_ALL_ONLINE_CLIENT)
                        || message.getMesType().equals(MessageType.MESSAGE_SERVER_TELL_OFFLINE_MES)) {

                    System.out.println("Server推送了一条消息：" + message.getContent());
                }

                else {

                    System.out.println("无法处理请求业务...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //get Socket对象的方法
    public Socket getSocket() {

        return this.socket;
    }

}
