package com.egtinteractive.chatserver;

import java.io.IOException;
import java.net.BindException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.egtinteractive.chatserver.server.ChatServer;
import com.egtinteractive.chatserver.server.Server;

public class TestChat {

    private final int PORT = 1111;
    private final int CLIENTS = 40;
    private Server server;

    @BeforeMethod
    public void beforeMethod() throws IOException {
	while (server == null) {
	    try {
		server = ChatServer.newChatServer();
		server.start();
	    } catch (BindException e) {
		server = null;
	    }
	}
    }

    @AfterMethod
    public void afterMethod() {
	server.stop();
	server = null;
    }

    @Test()
    public void testChatServer() {

    }

}
