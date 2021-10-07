package com.gzh.common;

import java.io.Serializable;

/**
 * @author 高梓航
 * @version 1.0.0
 * @createTime 2021年10月03日 14:24:00
 * 表示通信时的消息对象
 */
public class Message implements Serializable {
    //以对象流的形式传输需要序列化[IO]

    private static final long serialVersionUID = 1L;//保证序列化前后的兼容性
    private String sender;//发送方
    private String getter;//接收方
    private String content;//消息内容
    private String sendTime;//发送时间
    private String mesType;//消息类型[可以在接口中定义类型]

    //文件相关的属性
    private byte[] fileBytes;
    private int fileLen = 0;
    private String dest;//目标路径
    private String src;//源文件路径

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }
}
