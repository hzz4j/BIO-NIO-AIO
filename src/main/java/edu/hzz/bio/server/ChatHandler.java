package edu.hzz.bio.server;

import java.io.*;
import java.net.Socket;

public class ChatHandler implements Runnable{

    private ChatServer chatServer;
    private Socket socket;

    public ChatHandler(ChatServer chatServer,Socket socket){
        this.chatServer = chatServer;
        this.socket = socket;
    }

    /**
     * 读取客户端发送来的数据
     * @return
     */
    public String receiveMsg() throws IOException {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        return reader.readLine();
    }

    @Override
    public void run() {
        try{
            //  存储新上线的用户
            this.chatServer.addClient(socket);

            //  读取用户发送的信息
            String msg = null;
            while((msg = receiveMsg())!=null){
                String fullMessage = String.format("客户端【%d】: %s\n",socket.getPort(),msg);
                System.out.printf(fullMessage);
                //  将消息转发给聊天室里其他用户
                chatServer.forwardMsg(socket,fullMessage);
                if(chatServer.isReadyToQuit(msg)){
                    break;
                }
            }
        }catch (IOException exception){
            System.out.printf("服务器连接的客户端【%d】异常\n",socket.getPort());
        }finally {
            //  在服务器删除该客户端
            try {
                chatServer.removeClient(socket);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
