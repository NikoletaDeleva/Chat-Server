package com.egtinteractive.chatserver;

public class Chat {
    public static void main(String[] args) {
	final Server server = new ChatServer();
	server.start();
    }
}
