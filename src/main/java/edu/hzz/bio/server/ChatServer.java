package edu.hzz.bio.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private final int DEFAULT_PORT = 9898;
    private final String QUIT = "quit";

    private ServerSocket serverSocket;
    private Map<Integer, Writer> connectedClients;
    private ExecutorService executorService;

    public ChatServer(){
        executorService = Executors.newFixedThreadPool(5);
        connectedClients = new HashMap<>();
    }

    /**
     * 添加客户端
     * @param socket
     * @throws IOException
     */
    public synchronized void addClient(Socket socket) throws IOException {
        if(socket != null){
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            );

            connectedClients.put(socket.getPort(),writer);
            System.out.printf("成功加入一个客户端【%d】\n",socket.getPort());
        }

    }

    /**
     * 删除客户端
     * @param target
     */
    public synchronized void removeClient(Socket target) throws IOException {
        if(target != null){
            int port = target.getPort();
            if(connectedClients.containsKey(port)){
                this.connectedClients.get(port).close();
            }
           connectedClients.remove(port);
            System.out.println("客户端[" + port + "]已断开连接");
        }

    }

    /**
     * 群发消息
     * @param sender 消息来源
     * @param msg   消息
     * @throws IOException
     */
    public synchronized void forwardMsg(Socket sender,String msg) throws IOException {
        for(Map.Entry<Integer,Writer> client: connectedClients.entrySet()){
            if(!client.getKey().equals(sender.getPort())){
                Writer writer = client.getValue();
                writer.write(msg);
                writer.flush();
            }
        }
    }

    /**
     * 是否需要退出
     * @param msg
     * @return
     */
    public boolean isReadyToQuit(String msg){
        return QUIT.equals(msg);
    }

    public void close(){
        if(serverSocket != null && !serverSocket.isClosed()){
            try {
                if(serverSocket != null){
                    serverSocket.close();
                    System.out.println("服务器成功关闭");
                }
            } catch (IOException e) {
                System.out.println("服务器关闭异常");
                e.printStackTrace();
            }
        }
    }

    public void start(){
        //  创建一个socket并绑定到指定端口
        try {
            this.serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.printf("服务器启动成功，监听端口【%d】\n",DEFAULT_PORT);

            //  等待客户端连接进来
            while(true){
                Socket clientSocket = serverSocket.accept();
                //  处理每一个客户端的连接
                executorService.submit(new ChatHandler(this,clientSocket));
            }

        } catch (IOException e) {
            System.out.println("服务器启动失败");
            e.printStackTrace();
        }finally {
            close();
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }
}
