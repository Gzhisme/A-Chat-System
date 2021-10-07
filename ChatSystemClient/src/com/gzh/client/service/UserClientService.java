package com.gzh.client.service;

import com.gzh.common.Message;
import com.gzh.common.MessageType;
import com.gzh.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author 高梓航
 * @version 1.0.0
 * @createTime 2021年10月03日 16:34:00
 * 客户端与用户有关的需求
 */
public class UserClientService {

    private User user = new User(); //user在其他地方也要使用，因此作为属性
    private Socket socket;

    //根据userID和passWord到服务端验证用户是否合法
    public boolean checkUser(String userID, String passWord) throws Exception {
        boolean res = false;
        //创建User对象
        user.setUserID(userID);
        user.setPassWord(passWord);
        //连接到服务端，并发送user对象
        socket = new Socket(InetAddress.getLocalHost(), 9999);
        //获得socket的输出对象流
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(user);
        //接收服务端返回的Message对象
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Message ms = (Message) ois.readObject();
        //判断是否登录成功
        if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
            res = true;
            //启动线程，持有socket
            ClientConnectServerThread ccsThread = new ClientConnectServerThread(socket);
            ccsThread.start();
            //通过HashMap集合管理线程，便于后续扩展
            ManageClientConnectServerThread.addClientThread(userID, ccsThread);
        } else if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_FAIL)) {
            System.out.println("用户名或密码错误");
            socket.close();
        } else {
            System.out.println("该用户重复登录");
            socket.close();
        }
        return res;
    }

    //获得在线用户列表
    public void getOnlineUserList() {

        //创建一个类型为获得在线用户列表类型的Message对象
        Message message = new Message();
        message.setSender(user.getUserID());
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_USER);
        try {
            //拿到userID对应线程的socket
            ClientConnectServerThread clientThread = ManageClientConnectServerThread.getClientThread(user.getUserID());
            Socket socket = clientThread.getSocket();
            //创建对象输出流，并将Message对象写入
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //客户端无异常退出
    public void logout() {
        //通知server
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserID());
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println(user.getUserID() + "已退出系统");
            System.exit(0);//关闭进程
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
