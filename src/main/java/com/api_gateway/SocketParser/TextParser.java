package com.api_gateway.SocketParser;

import java.io.InputStream;

public class TextParser {

    boolean Validator(int x){
        return x == -1;
    }

    long BringPayload(int init, InputStream inputStream) {
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

    public String txtReader(InputStream inputStream){
        StringBuilder stringBuilder = new StringBuilder();

        while(true){
            try {

                int header = inputStream.read();
                if(Validator(header)) break;

                boolean FIN = (header & 0x80) != 0;
                int opcode = (header & 0x0F);

                if(opcode != 1){
                    break;
                }

                int payloadInfo = inputStream.read();
                if(Validator(payloadInfo)) break;

                boolean mask = (payloadInfo & 0x80) != 0;
                int initLen = (payloadInfo & 0x7F);

                long payload = BringPayload(initLen, inputStream);

                int[] Masked = new int[4];

                if(mask){
                    for(int i = 0; i < 4; i++){
                        Masked[i] = inputStream.read();
                        if(Validator(Masked[i])) break;
                    }
                }

                int j = 0;

                for(long i = 0; i < payload; i++){
                    int temp = inputStream.read();
                    if(Validator(temp)) break;

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
                break;
            }
        }

        return stringBuilder.toString();
    }
}