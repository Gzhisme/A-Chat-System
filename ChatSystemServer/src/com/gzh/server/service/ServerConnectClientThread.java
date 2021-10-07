package com.gzh.server.service;

import com.gzh.common.Message;
import com.gzh.common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

import static com.gzh.server.service.Server.validUsers;

/**
 * @author 高梓航
 * @version 1.0.0
 * @createTime 2021年10月03日 22:28:00
 * 服务端持有socket并与客户端连接的线程类
 */
public class ServerConnectClientThread extends Thread {

    private Socket socket;
    private String userID;//识别与哪个Client连接

    public ServerConnectClientThread(Socket socket, String userID) {
        this.socket = socket;
        this.userID = userID;
    }

    public Socket getSocket() {

        return socket;
    }

    public String getUserID() {

        return userID;
    }

    @Override
    public void run() { //发送or接收Client消息

        while (true) {
            try {
//                System.out.println("服务端与客户端" + userID + "保持通信，读取数据...");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message ms_get = (Message) ois.readObject();

                /*根据message对象类型进行对应的处理*/
                //返回在线用户列表
                if (ms_get.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_USER)) {
                    //处理客户端get在线用户列表的请求
                    System.out.println(ms_get.getSender() + "请求在线用户列表");
                    //通过遍历HashMap获取在线用户列表
                    String onlineUserList = ManageServerConnectClientThread.getOnlineUserList();
                    //返回给客户端
                    Message ms_return = new Message();
                    ms_return.setMesType(MessageType.MESSAGE_RETURN_ONLINE_USER);
                    ms_return.setContent(onlineUserList);
                    ms_return.setGetter(ms_get.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(ms_return);
                }

                //Client无异常退出，Server要remove与其保持通信的thread
                else if (ms_get.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {

                    System.out.println(ms_get.getSender() + "退出");
                    //将线程从Hashmap中remove
                    ManageServerConnectClientThread.removeServerThread(ms_get.getSender());
                    //关闭当前线程的socket
                    socket.close();
                    //退出当前循环，线程不再执行
                    break;
                }

                //私聊
                else if(ms_get.getMesType().equals(MessageType.MESSAGE_PRIVATE_MES)) {

                    if(ManageServerConnectClientThread.getServerThread(ms_get.getGetter()) != null) {
                        System.out.println(ms_get.getSender() + "请求与" + ms_get.getGetter() + "私聊");
                        //根据ms_get的getterID从Manage中取出对应线程
                        Socket socket = ManageServerConnectClientThread.getServerThread(ms_get.getGetter()).getSocket();
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(ms_get);
                    } else {
                        System.out.println(ms_get.getSender() + "请求与" + ms_get.getGetter() + "私聊");
                        ManageOffLineMessage.addMessage(ms_get.getGetter(), ms_get);
                    }
                }

                //群发
                else if(ms_get.getMesType().equals(MessageType.MESSAGE_PUBLIC_MES)) {

                    System.out.println(ms_get.getSender() + "请求群发");
                    for (String key : validUsers.keySet()) {
                        ServerConnectClientThread thread = ManageServerConnectClientThread.getServerThread(key);
                        if(thread != null) {
                            if(thread.getUserID().equals(ms_get.getSender())) continue;
                            ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                            oos.writeObject(ms_get);
                        } else {
                            ManageOffLineMessage.addMessage(key, ms_get);
                        }
                    }
                }

                //文件IO
                else if(ms_get.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {

                    if(ManageServerConnectClientThread.getServerThread(ms_get.getGetter()) != null) {
                        System.out.println(ms_get.getSender() + "要发送文件给" + ms_get.getGetter());
                        ServerConnectClientThread thread = ManageServerConnectClientThread.getServerThread(ms_get.getGetter());
                        ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                        oos.writeObject(ms_get);
                    } else {
                        System.out.println(ms_get.getSender() + "要发送文件给" + ms_get.getGetter());
                        ManageOffLineMessage.addMessage(ms_get.getGetter(), ms_get);
                    }
                }

                else {

                    System.out.println("无法处理请求业务...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
