package com.gyo.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.CharBuffer;

public class ReaderAndWriterClient {

    public static void run() throws IOException {
        // HTTP Client with Reader And Writer
        System.out.println("Hello Reader&Writer");

        // Make Socket and Connect
        Socket socket = new Socket("example.com", 80);

        System.out.println("Connect!");

        // HTTP Request Message
        String message = """
                GET / HTTP/1.1
                Host: example.com
                                
                """; // 빈 줄 빼먹지 말 것

        OutputStream outputStream = socket.getOutputStream();
        Writer writer = new OutputStreamWriter(outputStream);
        writer.write(message);
        writer.flush();

        System.out.println("Reqeust by Writer!");

        // Reader을 활용한 Response
        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        CharBuffer charBuffer = CharBuffer.allocate(1_000_000);
        inputStreamReader.read(charBuffer);
        charBuffer.flip();  //  주의
        System.out.println(charBuffer);

        // close
        socket.close();
        System.out.println("Closed!!");
    }
}
