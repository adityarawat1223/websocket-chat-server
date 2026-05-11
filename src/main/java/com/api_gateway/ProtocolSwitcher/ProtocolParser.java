package com.api_gateway.ProtocolSwitcher;
import com.api_gateway.SocketResponder.ReqResponder;
import com.api_gateway.dto.HttpRequest;
import java.io.InputStream;
import java.io.OutputStream;

public class ProtocolParser {
    void reqParser(String message, HttpRequest httpRequest) {
        String[] parts = message.split(" ");
        httpRequest.method = parts[0];
        httpRequest.path = parts[1].toLowerCase();
        httpRequest.version = parts[2];
    }
    void headerParser(String message, HttpRequest httpRequest) {
        int idx = message.indexOf(":");
        String key = message.substring(0, idx).trim().toLowerCase();
        String value = message.substring(idx + 1).trim();
        httpRequest.headers.put(key, value);
    }
    public void runner(InputStream inputStream , HttpRequest httpRequest , ReqResponder reqResponder, OutputStream outputStream){

        boolean req = true;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while (true) {
                int input = inputStream.read();

                if(input == -1){
                    break;
                }
                if((char)input == '\r'){
                    continue;
                }

                if((char) input == '\n'){
                    if(req){
                        reqParser(stringBuilder.toString(),httpRequest);
                        req = false;
                    }
                    else{
                        if(stringBuilder.isEmpty()){
                            break;
                        }
                        headerParser(stringBuilder.toString(),httpRequest);
                    }

                    stringBuilder = new StringBuilder();
                }
                else{
                    stringBuilder.append((char)input);
                }
            }
        }catch (Exception e){
            reqResponder.server_error(outputStream,e.getMessage());
        }

    }
}
