package edu.hzz.aio.echo.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;

public class AdapterHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {
    //  接收到新的连接
    @Override
    public void completed(AsynchronousSocketChannel socketChannel, AsynchronousServerSocketChannel serverSocketChannel) {
        //  继续监听新的连接
        if(serverSocketChannel.isOpen()){
            serverSocketChannel.accept(serverSocketChannel,this);
        }

        if(socketChannel != null && socketChannel.isOpen()){
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            Map<String,Object> map = new HashMap<>();
            map.put("type","read");
            map.put("data",byteBuffer);
            socketChannel.read(byteBuffer,map,new ClientHandler(socketChannel));
        }
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {

    }

}
