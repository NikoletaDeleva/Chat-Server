package com.egtinteractive.chatserver;

import java.io.IOException;

import com.egtinteractive.chatserver.server.ChatServer;
import com.egtinteractive.chatserver.server.Server;

public class Chat {
	public static void main(String[] args) throws IOException {
		final Server server = ChatServer.newChatServer();
		server.start();
	}
}
