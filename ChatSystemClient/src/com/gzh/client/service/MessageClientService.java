package com.gzh.client.service;

import com.gzh.common.Message;
import com.gzh.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * @author 高梓航
 * @version 1.0.0
 * @createTime 2021年10月05日 16:03:00
 * 客户端与消息有关的需求
 */
public class MessageClientService {

    //私聊
    public void privateChat(String senderID, String getterID, String content) {
        //创建Message对象
        Message message = new Message();
        message.setSender(senderID);
        message.setGetter(getterID);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        message.setMesType(MessageType.MESSAGE_PRIVATE_MES);
        //创建对象流
        try {
            Socket socket = ManageClientConnectServerThread.getClientThread(senderID).getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println("你在" + message.getSendTime() + "对" + getterID + "说：");
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //群发
    public void publicChat(String senderID, String content) {
        //创建Message对象
        Message message = new Message();
        message.setSender(senderID);
        message.setSendTime(new Date().toString());
        message.setContent(content);
        message.setMesType(MessageType.MESSAGE_PUBLIC_MES);
        //创建输出流
        try {
            Socket socket = ManageClientConnectServerThread.getClientThread(senderID).getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println("你在" + message.getSendTime() + "群发说：");
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
