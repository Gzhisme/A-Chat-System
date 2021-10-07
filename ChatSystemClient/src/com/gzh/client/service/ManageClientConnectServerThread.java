package com.gzh.client.service;

import java.util.HashMap;

/**
 * @author 高梓航
 * @version 1.0.0
 * @createTime 2021年10月03日 19:33:00
 * 管理客户端连接到服务端的线程的类
 */
public class ManageClientConnectServerThread {
    private static HashMap<String, ClientConnectServerThread> hashMap = new HashMap<>();

    //添加
    public static void addClientThread(String userID, ClientConnectServerThread thread) {

        hashMap.put(userID, thread);
    }

    //根据userID返回对应线程
    public static ClientConnectServerThread getClientThread(String userID) {

        return hashMap.get(userID);
    }
    
    //移除
    public static void removeClientThread(String userID){

        hashMap.remove(userID);
    }
}
