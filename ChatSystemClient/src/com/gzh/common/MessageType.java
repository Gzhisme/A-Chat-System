package com.gzh.common;

/**
 * @author 高梓航
 * @version 1.0.0
 * @createTime 2021年10月03日 14:30:00
 * 表示消息类型
 */
public interface MessageType {
    String MESSAGE_LOGIN_SUCCEED = "1"; //登录成功
    String MESSAGE_LOGIN_FAIL = "2"; //登录失败
    String MESSAGE_LOGIN_TWICE = "3"; //重复登录
    String MESSAGE_GET_ONLINE_USER = "4"; //获得在线用户列表
    String MESSAGE_RETURN_ONLINE_USER = "5"; //返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "6"; //客户端请求退出
    String MESSAGE_PRIVATE_MES = "7"; //私聊消息包
    String MESSAGE_PUBLIC_MES = "8"; //群发消息包
    String MESSAGE_FILE_MES = "9"; //文件IO
    String MESSAGE_TO_ALL_ONLINE_CLIENT = "10"; //Server推送
    String MESSAGE_SERVER_TELL_OFFLINE_MES = "11"; //Server通报离线消息
}
