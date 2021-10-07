package com.gzh.server.service;

import com.gzh.common.Message;
import com.gzh.common.MessageType;
import com.gzh.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 高梓航
 * @version 1.0.0
 * @createTime 2021年10月03日 21:48:00
 * 服务端，监听9999端口，等待Client连接，并保持通信
 */
public class Server {

    private ServerSocket serverSocket = null;
    //创建类似数据库的存放多个用户信息的集合
    public static HashMap<String, User> validUsers = new HashMap<>();

    //通过静态代码块加载HashMap的信息
    //静态代码块在创建实例对象时会加载一次
    static {
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("至尊宝", new User("至尊宝", "123456"));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "123456"));
        validUsers.put("菩提老祖", new User("菩提老祖", "123456"));
    }

    //验证用户是否合法
    private boolean checkUser(String userID, String passWord) {
        User testUser = new User(userID, passWord);
        if (validUsers.get(testUser.getUserID()) == null) {
            return false;
        } else {
            return validUsers.get(testUser.getUserID()).getPassWord().equals(passWord);
        }
    }

    //验证用户是否重复登录
    private boolean checkUserLoginTwice(String userID) {

        return ManageServerConnectClientThread.getServerThread(userID) == null;
    }

    public Server() {
        //监听的端口号可以写在配置文件中
        try {
            serverSocket = new ServerSocket(9999);
            System.out.println("服务端在9999端口监听...[输入push推送消息]");
            //开启Server推送消息线程
            new Thread(new SendNewsToAllClient()).start();

            //因为会有多个客户端连接，因此把接收连接的逻辑写在while循环中
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User u = (User) ois.readObject();//获得客户端发来的User对象
                Message message = new Message();//创建发给客户端的Message对象
                ObjectOutputStream oos =
                        new ObjectOutputStream(socket.getOutputStream());//创建服务端输出流对象

                //用户登录判断逻辑
                if (checkUser(u.getUserID(), u.getPassWord()) && checkUserLoginTwice(u.getUserID())) {
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);//写入Message对象
                    //创建服务端持有socket的线程
                    ServerConnectClientThread sccThread = new ServerConnectClientThread(socket, u.getUserID());
                    sccThread.start();
                    //把线程对象放入服务端管理线程的集合中
                    ManageServerConnectClientThread.addServerThread(u.getUserID(), sccThread);

                    //判断用户是否有离线消息
                    if(ManageOffLineMessage.numOfOffLineMessage(u.getUserID()) != 0) {

                        ArrayList<Message> messageArrayList = ManageOffLineMessage.getMessageArrayList(u.getUserID());

                        //通报Client离线消息数量
                        Message serverToClientMes = new Message();
                        serverToClientMes.setMesType(MessageType.MESSAGE_SERVER_TELL_OFFLINE_MES);
                        serverToClientMes.setContent("你有" + messageArrayList.size() + "条离线消息");
                        ObjectOutputStream num_oos =
                                new ObjectOutputStream(ManageServerConnectClientThread.getServerThread(u.getUserID()).getSocket().getOutputStream());
                        num_oos.writeObject(serverToClientMes);

                        //给Client发送离线消息/文件
                        for (int i = 0; i < messageArrayList.size(); i++) {
                            ObjectOutputStream mes_oos = new ObjectOutputStream(ManageServerConnectClientThread.getServerThread(u.getUserID()).getSocket().getOutputStream());
                            Message offLineMessage = messageArrayList.get(i);
                            mes_oos.writeObject(offLineMessage);
                        }
                    } else {
                        Message serverToClientMes = new Message();
                        serverToClientMes.setMesType(MessageType.MESSAGE_SERVER_TELL_OFFLINE_MES);
                        serverToClientMes.setContent("你没有离线消息");
                        ObjectOutputStream offLine_oos =
                                new ObjectOutputStream(ManageServerConnectClientThread.getServerThread(u.getUserID()).getSocket().getOutputStream());
                        offLine_oos.writeObject(serverToClientMes);
                    }
                } else if (!checkUser(u.getUserID(), u.getPassWord())) {
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);//写入Message对象
                    socket.close();//登录失败要关闭socket
                } else {
                    message.setMesType(MessageType.MESSAGE_LOGIN_TWICE);
                    oos.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //若服务端退出while循环，说明后台退出，不再监听，关闭serverSocket
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
