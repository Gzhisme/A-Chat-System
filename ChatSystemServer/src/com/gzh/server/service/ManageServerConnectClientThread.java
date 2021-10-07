package com.gzh.server.service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author 高梓航
 * @version 1.0.0
 * @createTime 2021年10月03日 22:44:00
 * 管理服务端连接到客户端的线程的类
 */
public class ManageServerConnectClientThread {

    private static HashMap<String, ServerConnectClientThread> hashMap = new HashMap<>();

    //添加线程
    public static void addServerThread(String userID, ServerConnectClientThread thread) {

        hashMap.put(userID, thread);
    }

    //删除线程
    public static void removeServerThread(String userID) {

        hashMap.remove(userID);
    }

    //根据userID返回线程
    public static ServerConnectClientThread getServerThread(String userID) {

        return hashMap.get(userID);
    }

    //返回当前在线用户列表
    public static String getOnlineUserList() {
        Iterator<String> iterator = hashMap.keySet().iterator();
        String onlineUserList = "";
        while (iterator.hasNext()) {
            onlineUserList += iterator.next().toString() + " ";
        }
        return onlineUserList;
    }

}
