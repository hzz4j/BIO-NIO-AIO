package edu.hzz.aio.echo.test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ByteBufferDemo {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String msg = "hello";
        byte[] bytes = msg.getBytes("utf-8");
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.flip(); //   limit 0
        byte[] array = buffer.array();
        String res = new String(array,"utf-8");
        System.out.println(res);


        ByteBuffer byteBuffer = ByteBuffer.allocate(msg.getBytes().length+10);
        byteBuffer.put(msg.getBytes());
        byteBuffer.flip();
        byteBuffer.flip();
        byteBuffer.flip();
        int length = msg.getBytes().length;
        byte[] array1 = byteBuffer.array();
        String res2 = new String(array1);
        System.out.println(res2);
    }
}
