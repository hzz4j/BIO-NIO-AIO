package edu.hzz.bio.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class UserInputHandler implements Runnable{
    private ChatClient chatClient;
    private Socket socket;


    public UserInputHandler(ChatClient client,Socket socket){
        this.chatClient = client;
        this.socket = socket;
    }

    @Override
    public void run() {
        Scanner consoleReader = new Scanner(System.in);
        String msg = null;
        try {

            while(consoleReader.hasNext()){
                chatClient.send(consoleReader.nextLine());
                if(chatClient.isReadyToQuit(msg)){
                    break;
                }
            }
        } catch (IOException exception) {
            System.out.println("传输到服务器异常");
            exception.printStackTrace();
        }
    }
}
