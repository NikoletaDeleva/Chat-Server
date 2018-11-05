package com.egtinteractive.chatserver;

import com.egtinteractive.chatserver.server.ChatServer;
import com.egtinteractive.chatserver.server.Server;

public class Chat {
    public static void main(String[] args) {
	final Server server = new ChatServer();
	server.start();
    }
}
