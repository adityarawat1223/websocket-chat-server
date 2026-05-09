package com.api_gateway.SocketResponder;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ReqResponder {

    public void invalid_request(PrintWriter printWriter){
        printWriter.print("HTTP/1.1 400 Bad Request\r\n");
        printWriter.print("Content-Type: text/plain\r\n");
        printWriter.print("Content-Length: 27\r\n");
        printWriter.print("\r\n");
        printWriter.print("Invalid WebSocket request");

    }

    public void server_error(PrintWriter printWriter , String error){
       int len = error.getBytes(StandardCharsets.UTF_8).length;
        printWriter.print("HTTP/1.1 500 Internal Server Error\r\n");
        printWriter.print("Content-Type: text/plain\r\n");
        printWriter.print("Content-Length:" + len +  "\r\n");
        printWriter.print("\r\n");
        printWriter.print(error);
    }

    public void upgrade(PrintWriter printWriter, String Key){
        printWriter.print("HTTP/1.1 101 Switching Protocols\r\n");
        printWriter.print("Upgrade: websocket\r\n");
        printWriter.print("Connection: Upgrade\r\n");
        printWriter.print("Sec-WebSocket-Accept: " + Key + "\r\n");
        printWriter.print("\r\n");
    }

//    public void close_frame(OutputStream outputStream, String reason){
//
//        printWriter.write(0x88);
//        int len = reason.getBytes(StandardCharsets.UTF_8).length;
//        printWriter.write(len);
//        printWriter.write(reason);
//    }
}
