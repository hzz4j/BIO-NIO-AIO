package edu.hzz.aio.chat.client;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class UserInputHandler implements Runnable{
    private ChatClient chatClient;

    public UserInputHandler(ChatClient chatClient){
        this.chatClient = chatClient;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            while(true){
                String msg = scanner.nextLine();

                //  发送数据
                chatClient.sendMsg(msg);
                // 是否退出
                if(chatClient.readyToQuit(msg)){
                    break;
                }

            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
