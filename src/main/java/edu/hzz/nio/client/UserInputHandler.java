package edu.hzz.nio.client;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class UserInputHandler implements Runnable{
    private ChatClient chatClient;

    public UserInputHandler(ChatClient chatClient){
        this.chatClient = chatClient;
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNext()){
                String msg = scanner.nextLine();
                chatClient.send(msg);

                if(chatClient.readyToQuit(msg)){
                   break;
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }finally {
            chatClient.close();
        }
    }
}
