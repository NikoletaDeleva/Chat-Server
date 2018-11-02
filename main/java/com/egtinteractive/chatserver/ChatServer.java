package com.egtinteractive.chatserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatServer implements Server {
    private final static String HOST = "localhost";
    private final static int PORT = 1111;
    private final AtomicInteger clientNumber;
    private ServerSocket serverSocket;
    private RoomManager roomManager;

    public ChatServer() {
	this.clientNumber = new AtomicInteger(1);
	this.roomManager = new RoomManager();
    }

    @Override
    public void start() {
	try {
	    final InetAddress address = InetAddress.getByName(HOST);

	    this.serverSocket = new ServerSocket(PORT, 100, address);

	    System.out.println("Listening on " + this.serverSocket);

	    while (!Thread.currentThread().isInterrupted()) {

		final Socket newSocket = this.serverSocket.accept();
		System.out.println("Connection established.");

		final Client newClient = new ChatClient(this.clientNumber.get(), newSocket, this.roomManager);

		new ClientThread(newClient).start();

		this.clientNumber.incrementAndGet();
	    }

	} catch (IOException e) {
	    System.out.println("Server stopped!");
	    e.printStackTrace();
	} finally {
	    stop();
	}

    }

    @Override
    public void stop() {
	try {
	    this.roomManager.closeRooms();
	    this.serverSocket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
