package edu.hzz.aio.chat.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private AsynchronousServerSocketChannel serverSocketChannel;
    private List<ClientHandler> onlineClients;
    private int port;
    public Charset charset = Charset.forName("utf-8");

    public ChatServer(){
        onlineClients = new ArrayList<>();
        this.port = 9898;
    }

    public void start(){
        try {
            //  自定义线程池
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            //  将线程池放入异步通道中
            AsynchronousChannelGroup asynchronousChannelGroup = AsynchronousChannelGroup.withThreadPool(executorService);
            //  使用自定义的asynchronousChannelGroup
            serverSocketChannel = AsynchronousServerSocketChannel.open(asynchronousChannelGroup);
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1",port));

            System.out.printf("服务端启动,监听端口%d\n",port);

            while(true){
                serverSocketChannel.accept(serverSocketChannel,new AcceptHandler(this));
                //  阻塞
                System.in.read();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }finally {
            close(serverSocketChannel);
        }
    }

    public void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public String getClientName(AsynchronousSocketChannel clientSocketChannel) {
        int clientPort = -1;
        try {
            InetSocketAddress remoteAddress = (InetSocketAddress)clientSocketChannel.getRemoteAddress();
            clientPort = remoteAddress.getPort();
        } catch (IOException exception) {
            System.out.println("获取客户端地址失败");
            exception.printStackTrace();
        }

        return String.format("客户端【%d】",clientPort);
    }

    public void forwardMsg(AsynchronousSocketChannel sourceSocketChannel, String msg) {
        if(sourceSocketChannel == null){
            return;
        }
        String sendMsg = getClientName(sourceSocketChannel)+": "+msg;
        ByteBuffer buff = charset.encode(sendMsg);

        for (ClientHandler targetClientHandler: onlineClients){
            AsynchronousSocketChannel targetClientHandlerClientSocketChannel = targetClientHandler.getClientSocketChannel();
            if(targetClientHandlerClientSocketChannel.equals(sourceSocketChannel)){   //  过滤掉来源对象
                continue;
            }

            if(targetClientHandlerClientSocketChannel.isOpen()){
                //  回调传入targetClientHandler很精妙
                targetClientHandlerClientSocketChannel.write(buff,null,targetClientHandler);
            }
        }
    }

    //  添加客户端
    public void addClient(ClientHandler clientHandler) {
        onlineClients.add(clientHandler);
    }

    //  移除客户端
    public void removeClient(ClientHandler clientHandler) {
        onlineClients.remove(clientHandler);
        close(clientHandler.getClientSocketChannel());
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }
}
