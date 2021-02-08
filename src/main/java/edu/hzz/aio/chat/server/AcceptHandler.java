package edu.hzz.aio.chat.server;


import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {
    private ChatServer server;

    public AcceptHandler(ChatServer chatServer){
        this.server = chatServer;
    }
    @Override
    public void completed(AsynchronousSocketChannel clientSocketChannel, AsynchronousServerSocketChannel serverSocketChannel) {
        if(serverSocketChannel != null && serverSocketChannel.isOpen()){
            //  继续接收连接
            serverSocketChannel.accept(serverSocketChannel,this);
        }

        //   读取数据
        if(clientSocketChannel != null && clientSocketChannel.isOpen()){
            ClientHandler clientHandler = new ClientHandler(server, clientSocketChannel);
            //   加入新的连接
            server.addClient(clientHandler);
            System.out.printf("%s加入聊天室\n",server.getClientName(clientSocketChannel));
            //  读取数据
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            clientSocketChannel.read(buffer,buffer,clientHandler);
        }
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {

    }
}
