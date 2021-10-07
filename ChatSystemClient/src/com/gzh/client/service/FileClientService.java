package com.gzh.client.service;

import com.gzh.common.Message;
import com.gzh.common.MessageType;

import java.io.*;
import java.util.Date;

/**
 * @author 高梓航
 * @version 1.0.0
 * @createTime 2021年10月05日 16:06:00
 * 客户端与文件有关的需求
 */
public class FileClientService {

    /**
     *
     * @param src   源文件路径
     * @param dest  目标路径
     * @param senderID  发送方
     * @param getterID  接收方
     */

    public void sendFile(String src, String dest, String senderID, String  getterID) {

        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderID);
        message.setGetter(getterID);
        message.setSendTime(new Date().toString());
        message.setSrc(src);
        message.setDest(dest);

        //将src文件以字节数组的形式读取文件输入流
        byte[] fileBytes = new byte[(int) new File(src).length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(src);
            fis.read(fileBytes);
            //设置到message对象的属性上
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //提示信息
        System.out.println(senderID + "给" + getterID + "发送文件：" + src + " 到：" + dest);
        //发送文件
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientThread(senderID).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
