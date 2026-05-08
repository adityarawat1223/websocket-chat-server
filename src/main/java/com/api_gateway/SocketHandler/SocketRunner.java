package com.api_gateway.SocketHandler;
import java.io.InputStream;
import java.net.*;

public class SocketRunner {


    public void runner(){
        try(ServerSocket serverSocket = new ServerSocket(9090)){
            while(true) {
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
            }

        }catch (Exception e){
            System.out.print(e.getMessage());
        }

    }
}
