package com.gzh.server.service;

import com.gzh.common.Message;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 高梓航
 * @version 1.0.0
 * @createTime 2021年10月06日 15:33:00
 * 管理离线Client的留言/文件
 */
public class ManageOffLineMessage {

    private static HashMap<String, ArrayList<Message>> hashMap = new HashMap<>();

    //添加离线Message对象
    public static void addMessage(String userID, Message message) {

        if(hashMap.get(userID) == null) {
            ArrayList<Message> messagesArrayList = new ArrayList<>();
            messagesArrayList.add(message);
            hashMap.put(userID, messagesArrayList);
        } else {
            hashMap.get(userID).add(message);
        }
    }

    //离线Message对象数量
    public static int numOfOffLineMessage(String userID) {

        return hashMap.get(userID) == null ? 0 : hashMap.get(userID).size();
    }

    //取出离线Message对象
    public static ArrayList<Message> getMessageArrayList(String userID) {

        return hashMap.get(userID);
    }
}
