package edu.hzz.aio.chat.server;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 客户端通信操作
 */
public class ClientHandler implements CompletionHandler<Integer, Object> {
    private final ChatServer server;
    private final AsynchronousSocketChannel clientSocketChannel;

    public ClientHandler(ChatServer server,AsynchronousSocketChannel clientSocketChannel){
        this.server = server;
        this.clientSocketChannel = clientSocketChannel;
    }

    @Override
    public void completed(Integer result, Object attachment) {
        if(attachment != null){
            ByteBuffer buffer = (ByteBuffer) attachment;
            //  读取数据之后，需要转发
            if(result <= 0){
                //  客户端异常
                System.out.println("客户端异常");
                this.server.removeClient(this);
            }else{
                String msg = receive(buffer);
                //  服务端打印
                System.out.printf("%s: %s\n",this.server.getClientName(clientSocketChannel),msg);
                //  转发
                this.server.forwardMsg(this.clientSocketChannel,msg);

                // 检测是否退出
                if(readyToQuit(msg)){
                    System.out.printf("%s 退出",this.server.getClientName(clientSocketChannel));
                    this.server.removeClient(this);
                }else{
                    //  则继续读
                    buffer.clear();
                    clientSocketChannel.read(buffer,buffer,this);
                }
            }
        }
    }


    private String receive(ByteBuffer buffer){
        //  开启读模式
        buffer.flip();
        CharBuffer charBuffer = this.server.charset.decode(buffer);
        return charBuffer.toString();
    }

    public boolean readyToQuit(String msg){
        return "quit".equals(msg);
    }

    public AsynchronousSocketChannel getClientSocketChannel(){
        return this.clientSocketChannel;
    }

    @Override
    public void failed(Throwable exc, Object data) {

    }
}
