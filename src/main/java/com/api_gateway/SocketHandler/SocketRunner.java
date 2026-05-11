package com.api_gateway.SocketHandler;
import com.api_gateway.ProtocolSwitcher.ProtocolParser;
import com.api_gateway.ProtocolSwitcher.ProtocolUpgrader;
import com.api_gateway.SocketParser.TextParser;
import com.api_gateway.SocketResponder.ReqResponder;
import com.api_gateway.dto.HttpRequest;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class SocketRunner {

    ProtocolParser protocolParser;
    ProtocolUpgrader protocolUpgrader;
    ReqResponder reqResponder;
    TextParser textParser;
    public SocketRunner(ProtocolParser protocolParser, ProtocolUpgrader protocolUpgrader, ReqResponder reqResponder,TextParser textParser){
        this.protocolParser = protocolParser;
        this.protocolUpgrader = protocolUpgrader;
        this.reqResponder = reqResponder;
        this.textParser = textParser;
    }

    public void runner(){
        try(ServerSocket serverSocket = new ServerSocket(9090)){

            while(true) {
                Socket socket = serverSocket.accept();
                System.out.print("Adi");

                InputStream inputStream = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                HttpRequest httpRequest= new HttpRequest();
                protocolParser.runner(inputStream,httpRequest,reqResponder,out);
                boolean upgraded = protocolUpgrader.Upgrader(httpRequest,reqResponder,out);

                if(upgraded){
                    while(true){
                        String message = textParser.txtReader(inputStream, out,reqResponder);

                        if(message == null){
                            socket.close();
                            break;
                        }
                        reqResponder.Writer(out,message);
                    }
                }
            }

        }catch (Exception e){
            System.out.print(e.getMessage());
        }

    }
}
