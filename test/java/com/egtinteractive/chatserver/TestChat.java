package com.egtinteractive.chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

    @Test()
    public void testChatServer() {

	final List<PrintWriter> writers = new ArrayList<>(CLIENTS);
	final List<BufferedReader> readers = new ArrayList<>(CLIENTS);
	final List<Socket> sockets = new ArrayList<>(CLIENTS);
	try {
	    for (int index = 0; index < CLIENTS; index++) {
		final Socket socket = new Socket(InetAddress.getLocalHost(), PORT);
		final PrintWriter writer = new PrintWriter(socket.getOutputStream());
		final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		writers.add(writer);
		readers.add(reader);
		sockets.add(socket);
	    }

	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    @AfterMethod
    public void afterMethod() {
	server.stop();
	server = null;
    }
}
