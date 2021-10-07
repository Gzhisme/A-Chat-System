package com.gzh.server.service;

import com.gzh.common.Message;
import com.gzh.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Scanner;

/**
 * @author 高梓航
 * @version 1.0.0
 * @createTime 2021年10月05日 20:28:00
 */
public class SendNewsToAllClient implements Runnable {

    Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {

        while (true) {

            String command = scanner.next();
            if(command.equals("push")) {
                while(true) {
                    System.out.println("请输入服务端要推送的消息[输入exit停止推送]");
                    String news = scanner.next();
                    if (news.equals("exit")) break;
                    //构建消息对象
                    Message message = new Message();
                    message.setSender("Server");
                    message.setSendTime(new Date().toString());
                    message.setContent(news);
                    message.setMesType(MessageType.MESSAGE_TO_ALL_ONLINE_CLIENT);
                    System.out.println("Server推送：" + news + " 给所有Client");

                    //遍历Manage发送news
                    String[] onlineUserList = ManageServerConnectClientThread.getOnlineUserList().split(" ");
                    for (int i = 0; i < onlineUserList.length; i++) {
                        ServerConnectClientThread thread = ManageServerConnectClientThread.getServerThread(onlineUserList[i]);
                        try {
                            ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                            oos.writeObject(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
