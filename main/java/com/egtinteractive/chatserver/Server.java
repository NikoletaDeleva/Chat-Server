package com.egtinteractive.chatserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private final static String HOST = "localhost";
    private final static int PORT = 1111;
    private final AtomicInteger clientNumber;
    private ServerSocket serverSocket;
    private RoomManager roomManager;

    public Server() {
	this.clientNumber = new AtomicInteger(1);
	this.roomManager = new RoomManager();
    }

    public void start() {
	try {
	    final InetAddress address = InetAddress.getByName(HOST);

	    serverSocket = new ServerSocket(PORT, 100, address);

	    System.out.println("Listening on " + serverSocket);

	    while (!Thread.currentThread().isInterrupted()) {

		final Socket newSocket = serverSocket.accept();
		System.out.println("Connection established.");

		final Client newClient = new ChatClient(clientNumber.get(), newSocket, roomManager);

		new ClientThread(newClient).start();

		clientNumber.incrementAndGet();
	    }

	} catch (IOException e) {
	    System.out.println("Server stopped!");
	    e.printStackTrace();
	} finally {
	    stop();
	}

    }

    private void stop() {
	try {
	    roomManager.closeRooms();
	    serverSocket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
