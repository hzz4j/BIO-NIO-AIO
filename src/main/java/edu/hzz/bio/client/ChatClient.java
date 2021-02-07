package edu.hzz.bio.client;

import java.io.*;
import java.net.Socket;

public class ChatClient {
    private final String SERVER_ADDRESS = "127.0.0.1";
    private final int SERVER_PORT = 9898;
    private final String QUIT = "quit";

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    /**
     * 是否退出
     * @param msg
     * @return true 确定退出; false 不退出
     */
    public boolean isReadyToQuit(String msg){
        return QUIT.equals(msg);
    }

    //  发送数据给服务端
    public void send(String msg) throws IOException{
        if(!socket.isOutputShutdown()){
            writer.write(msg+"\n");
            writer.flush();
        }
    }

    // 从服务器接收消息
    public String receive() throws IOException {
        String msg = null;
        if (!socket.isInputShutdown()) {
            msg = reader.readLine();
        }
        return msg;
    }

    public void close(){
        if(socket != null){
            try {
                socket.close();
                System.out.println("成功关闭");
            } catch (IOException exception) {
                System.out.println("关闭异常");
                exception.printStackTrace();
            }
        }
    }


    public void start(){
        try {
            //  创建一个socket
            socket = new Socket(SERVER_ADDRESS,SERVER_PORT);
            System.out.println("成功连接到服务端");
            //  读取数据
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            );
            //  处理用户的输入
            new Thread(new UserInputHandler(this,socket)).start();

            //  读取服务器转发的消息
            String msg = null;
            while((msg = receive())!=null){
                System.out.println(msg);
            }
        } catch (IOException exception) {
            System.out.printf("连接到服务端【%s:%d】失败\n",SERVER_ADDRESS,SERVER_PORT);
            exception.printStackTrace();
        }finally {
            close();
        }

    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.start();
    }
}
