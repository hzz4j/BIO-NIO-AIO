package edu.hzz.aio.echo.server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Map;

public class ClientHandler implements CompletionHandler<Integer,Object> {
    private AsynchronousSocketChannel socketChannel ;

    public ClientHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void completed(Integer len, Object attachment) {
        Map<String,Object> map = (Map)attachment;
        String type = (String) map.get("type");
        ByteBuffer byteBuffer = (ByteBuffer) map.get("data");
        if("read".equals(type)){
            //  写回客户端
            map.put("type","write");
            byteBuffer.flip();
            socketChannel.write(byteBuffer,map,this);
        }else if("write".equals(type)){
            byteBuffer.clear();
            map.put("type","read");
            socketChannel.read(byteBuffer,map,this);
        }
    }

    @Override
    public void failed(Throwable exc, Object attachment) {

    }
}
