package com.gyo.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class InputAndOutputClient {
    // InputStream, OutputStream을 기반으로한 HTTP clinet
    public static void run() throws IOException {
        System.out.println("Hello IO!");

        // Make Socket and Connect
        try (Socket socket = new Socket("example.com", 80)) {
            System.out.println("Connect!");

            // HTTP Request Message
            String message = """
                    GET / HTTP/1.1
                    Host: example.com
                                    
                    """; // 빈 줄 빼먹지 말 것

            // Request
            OutputStream outputStream = socket.getOutputStream();   // socket의 outputStream으로 네트워크 출력
            outputStream.write(message.getBytes());

            System.out.println("Reqeust by outputStream!");

            // Response
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1_000_000];
            int size = inputStream.read(bytes); // socket의 InputStream에서 bytes 배열로 reponse를 읽어온다.

            // 읽어온 Reponse 데이터 출력하기
            byte[] data = Arrays.copyOf(bytes, size);
            String text = new String(data);
            System.out.println(text);
        }
        // try-with-resource로 close
        System.out.println("Closed!!");
    }
}
