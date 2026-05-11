package com.api_gateway.ProtocolSwitcher;
import com.api_gateway.SocketResponder.ReqResponder;
import com.api_gateway.dto.HttpRequest;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class ProtocolUpgrader {

    private static final String GUID =
            "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    boolean validator(HttpRequest httpRequest){
        boolean yes = true;

        yes &= (httpRequest.path.equals("/chat"));
        yes &= (httpRequest.version.equals("HTTP/1.1"));

        String Connection = httpRequest.headers.get("connection");
        String Key = httpRequest.headers.get("sec-websocket-key");
        String Version = httpRequest.headers.get("sec-websocket-version");
        if(Version == null || Key == null || Connection == null){
            return false;
        }
        yes&= (Connection.equalsIgnoreCase("upgrade"));
        yes &= (Version.equalsIgnoreCase("13"));
        return yes;
    }
    public boolean Upgrader(HttpRequest httpRequest , ReqResponder reqResponder, OutputStream outputStream) {

        if(validator(httpRequest)){
            String Key = httpRequest.headers.get("sec-websocket-key");
            String value = Key + GUID;
            try {
                MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
                byte[] hash = sha1.digest(value.getBytes(StandardCharsets.UTF_8));

                Key = Base64.getEncoder().encodeToString(hash);
                reqResponder.upgrade(outputStream,Key);
                return true;
            }
            catch (Exception e){
                reqResponder.server_error(outputStream,e.getMessage());
            }
        }
        reqResponder.invalid_request(outputStream);
        return false;
    }
}
