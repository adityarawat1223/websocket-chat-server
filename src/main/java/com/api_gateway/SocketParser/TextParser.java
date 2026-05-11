package com.api_gateway.SocketParser;

import com.api_gateway.SocketResponder.ReqResponder;

import java.io.InputStream;
import java.io.OutputStream;

public class TextParser {

    boolean Validator(int x){
        return x == -1;
    }

    public long BringPayload(int init, InputStream inputStream) {
        try {
            if(init < 126){
                return init;
            }

            int limit = (init == 126 ? 2 : 8);
            long value = 0;

            for(int i = 0; i < limit; i++){
                int b = inputStream.read();
                if(Validator(b)) return -1;

                value = (value << 8) | (b & 0xFF);
            }

            return value;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String txtReader(InputStream inputStream , OutputStream outputStream, ReqResponder reqResponder){
        StringBuilder stringBuilder = new StringBuilder();
        boolean close = false;
        while(true){
            try {

                int header = inputStream.read();
                if(Validator(header))  {close = true;break;}

                boolean FIN = (header & 0x80) != 0;
                int opcode = (header & 0x0F);

                if(opcode != 1){
                    if(opcode == 8){
                        reqResponder.close(outputStream);
                    }
                    close = true;
                    break;
                }

                int payloadInfo = inputStream.read();
                if(Validator(payloadInfo))  {close = true;break;}

                boolean mask = (payloadInfo & 0x80) != 0;
                int initLen = (payloadInfo & 0x7F);

                long payload = BringPayload(initLen, inputStream);

                int[] Masked = new int[4];

                if(mask){
                    for(int i = 0; i < 4; i++){
                        Masked[i] = inputStream.read();
                        if(Validator(Masked[i])) {close = true;break;}
                    }
                }

                int j = 0;

                for(long i = 0; i < payload; i++){
                    int temp = inputStream.read();
                    if(Validator(temp)) {close = true;break;}

                    if(mask){
                        temp = temp ^ Masked[j];
                        j = (j + 1) % 4;
                    }

                    stringBuilder.append((char)(temp & 0xFF));
                }

                if(FIN){
                    break;
                }

            } catch (Exception e){
               reqResponder.server_error(outputStream,e.getMessage());
            }
        }
        if(close){
            return null;
        }
        return stringBuilder.toString();
    }
}