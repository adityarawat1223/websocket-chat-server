package com.api_gateway.SocketResponder;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ReqResponder {


    public void invalid_request(OutputStream out) {
        try {
            out.write("HTTP/1.1 400 Bad Request\r\n".getBytes(StandardCharsets.UTF_8));
            out.write("Content-Type: text/plain\r\n".getBytes(StandardCharsets.UTF_8));
            out.write("Content-Length: 27\r\n".getBytes(StandardCharsets.UTF_8));
            out.write("\r\n".getBytes(StandardCharsets.UTF_8));
            out.write("Invalid WebSocket request".getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void server_error(OutputStream out, String error) {
        byte[] errorBytes = error.getBytes(StandardCharsets.UTF_8);

        String headers =
                "HTTP/1.1 500 Internal Server Error\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: " + errorBytes.length + "\r\n" +
                        "\r\n";
        try {
            out.write(headers.getBytes(StandardCharsets.UTF_8));
            out.write(errorBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void upgrade(OutputStream out, String key) {
        String response =
                "HTTP/1.1 101 Switching Protocols\r\n" +
                        "Upgrade: websocket\r\n" +
                        "Connection: Upgrade\r\n" +
                        "Sec-WebSocket-Accept: " + key + "\r\n" +
                        "\r\n";
        try {
            out.write(response.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
        System.out.print(e.getMessage());
        }
    }

    public void Writer(OutputStream outputStream, String message) {
        try {

            byte[] bt = message.getBytes();
            int len = bt.length;

            for (int i = 0; i < len; i += 2048) {
                int lento = Math.min(len, i + 2048) - i;

                int header = 0;
                int payload = 0;

                header |= (i + 2048 >= len ? 0x80 : 0x00);
                if(i == 0){
                    header |= 1;
                }

                payload |= (lento > 125 ? 126 : lento);

                outputStream.write(header);
                outputStream.write(payload);

                if (lento > 125) {
                    outputStream.write((lento >> 8) & 0xFF);
                    outputStream.write(lento & 0xFF);
                }

                for (int k = i; k < Math.min(i + 2048, len); k++) {
                    outputStream.write(bt[k]);
                }

                outputStream.flush();
            }

        } catch (Exception e) {
            System.out.print(e.getMessage());

        }
    }
    public void close(OutputStream outputStream){
        try{outputStream.write(0x88);
            outputStream.write(0x00); // payload length = 0
            outputStream.flush();

        }catch (Exception e){
            System.out.print(e.getMessage());
        }


    }

}

