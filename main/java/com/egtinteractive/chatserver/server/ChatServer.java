package com.egtinteractive.chatserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.egtinteractive.chatserver.client.ChatClient;
import com.egtinteractive.chatserver.client.Client;
import com.egtinteractive.chatserver.room.RoomManager;

public class ChatServer implements Server {
    private final static int PORT = 1111;

    private final AtomicInteger clientNumber;
    private final ServerSocket serverSocket;

    private ExecutorService executorService;

    private RoomManager roomManager;

    private ChatServer(final ServerSocket serverSocket) {
	this.clientNumber = new AtomicInteger(1);
	this.roomManager = new RoomManager();
	this.serverSocket = serverSocket;
    }

    public static ChatServer newChatServer() throws IOException {
	final ServerSocket serverSocket = new ServerSocket(PORT);
	return new ChatServer(serverSocket);
    }

    @Override
    public void start() {
	try {

	    System.out.println("Listening on " + this.serverSocket);

	    while (!Thread.currentThread().isInterrupted()) {

		final Socket newSocket = this.serverSocket.accept();
		System.out.println("Connection established.");

		final Client chatClient = new ChatClient(clientNumber.get(), newSocket, this.roomManager);

		this.executorService = Executors.newSingleThreadExecutor();

		this.executorService.execute(new Runnable() {
		    public void run() {
			try {
			    final BufferedReader bufferedReader = new BufferedReader(
				    new InputStreamReader(newSocket.getInputStream()));
			    final PrintWriter printWriter = new PrintWriter(newSocket.getOutputStream(), true);

			    chatClient.setRoomAndName(bufferedReader, printWriter);
			} catch (IOException e) {
			    e.printStackTrace();
			}
		    }
		});

		chatClient.startClient();
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
