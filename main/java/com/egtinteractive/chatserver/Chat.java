package com.egtinteractive.chatserver;

import java.io.IOException;
import java.net.UnknownHostException;

public class Chat {

    public static void main(String[] args) throws UnknownHostException, IOException {
	ClientProgram program = new ClientProgram();
	program.start();
    }
}
