package com.api_gateway.SocketHandler;
import com.api_gateway.ProtocolSwitcher.ProtocolParser;
import com.api_gateway.ProtocolSwitcher.ProtocolUpgrader;
import com.api_gateway.SocketResponder.ReqResponder;
import com.api_gateway.dto.HttpRequest;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.*;

public class SocketRunner {

    ProtocolParser protocolParser;
    ProtocolUpgrader protocolUpgrader;
    ReqResponder reqResponder;
    public SocketRunner(ProtocolParser protocolParser, ProtocolUpgrader protocolUpgrader, ReqResponder reqResponder){
        this.protocolParser = protocolParser;
        this.protocolUpgrader = protocolUpgrader;
        this.reqResponder = reqResponder;
    }

    public void runner(){
        try(ServerSocket serverSocket = new ServerSocket(9090)){
            while(true) {
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                HttpRequest httpRequest= new HttpRequest();
                protocolParser.runner(inputStream,httpRequest,reqResponder,out);
                boolean upgraded = protocolUpgrader.Upgrader(httpRequest,reqResponder,out);

                if(upgraded){
                    // rest logic;
                }
            }

        }catch (Exception e){
            System.out.print(e.getMessage());
        }

    }
}
