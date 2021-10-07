package com.gzh.client.view;

import com.gzh.client.service.MessageClientService;
import com.gzh.client.service.FileClientService;
import com.gzh.client.service.UserClientService;

import java.util.Scanner;

/**
 * @author 高梓航
 * @version 1.0.0
 * @createTime 2021年10月03日 15:02:00
 * 客户端菜单界面
 */
public class ClientView {

    private boolean loop = true; //控制是否显示菜单
    private String key = ""; //记录用户登录系统时的输入: 1 or 9
    private UserClientService userClientService = new UserClientService();//用户服务
    private MessageClientService messageClientService = new MessageClientService();//消息服务
    private FileClientService fileClientService = new FileClientService();//文件服务

    //显示主菜单
    private void mainMenu() throws Exception {
        while (loop) {
            System.out.println("==========欢迎登录网络通信系统==========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出登录");
            System.out.print("请输入你的选择：");

            Scanner scanner = new Scanner(System.in);
            key = scanner.next();

            //根据用户的输入，处理不同的逻辑
            switch (key) {
                case "1":
                    System.out.print("请输入用户ID：");
                    String userID = scanner.next();
                    System.out.print("请输入密码：");
                    String passWord = scanner.next();

                    if(userClientService.checkUser(userID, passWord)) {
                        System.out.println("==========欢迎 (用户:" + userID + ")==========");
                        //进入二级菜单
                        while(loop) {
                            System.out.println("==========网络通信系统二级菜单 (用户:" + userID + ")==========");
                            System.out.println("\t\t 1 显示在线用户");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("请输入你的选择：");
                            key = scanner.next();
                            switch (key) {
                                case "1":
                                    userClientService.getOnlineUserList();
                                    break;
                                case "2":
                                    System.out.print("请输入要群发的消息：");
                                    String public_content = scanner.next();
                                    messageClientService.publicChat(userID, public_content);
                                    break;
                                case "3":
                                    System.out.print("请输入想聊天的用户ID：");
                                    String getterID = scanner.next();
                                    System.out.print("请输入要发送的消息：");
                                    String private_content = scanner.next();
                                    messageClientService.privateChat(userID, getterID, private_content);
                                    break;
                                case "4":
                                    System.out.println("请输入想发送文件的用户ID：");
                                    String fileGetterID = scanner.next();
                                    System.out.println("请输入源文件：");
                                    String src = scanner.next();
                                    System.out.println("请输入目标路径：");
                                    String dest = scanner.next();
                                    fileClientService.sendFile(src, dest, userID, fileGetterID);
                                    break;
                                case "9":
                                    loop = !loop;
                                    userClientService.logout();
                                    break;
                            }
                        }
                    }
                    break;
                case "9":
                    loop = !loop;
                    System.out.println("用户已退出本次登录...");
                    System.exit(0);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        try {
            new ClientView().mainMenu();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
