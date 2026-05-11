package com.api_gateway;

import com.api_gateway.ProtocolSwitcher.ProtocolParser;
import com.api_gateway.ProtocolSwitcher.ProtocolUpgrader;
import com.api_gateway.SocketHandler.SocketRunner;
import com.api_gateway.SocketParser.TextParser;
import com.api_gateway.SocketResponder.ReqResponder;

public class Main {
    public static void main(String[] args) {
        ProtocolParser protocolParser = new ProtocolParser();
        ProtocolUpgrader protocolUpgrader = new ProtocolUpgrader();
        ReqResponder reqResponder = new ReqResponder();
        TextParser textParser = new TextParser();
        SocketRunner socketRunner = new SocketRunner(protocolParser,protocolUpgrader ,reqResponder,textParser);
        socketRunner.runner();


    }
}