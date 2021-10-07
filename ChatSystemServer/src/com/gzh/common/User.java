package com.gzh.common;

import java.io.Serializable;

/**
 * @author 高梓航
 * @version 1.0.0
 * @createTime 2021年10月03日 12:06:00
 * 表示用户信息
 */
public class User implements Serializable {
    //以对象流的形式传输需要序列化[IO]

    private static final long serialVersionUID = 1L;//保证序列化前后的兼容性
    private String userID;//ID
    private String passWord;//密码

    public User(String userID, String passWord) {
        this.userID = userID;
        this.passWord = passWord;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
