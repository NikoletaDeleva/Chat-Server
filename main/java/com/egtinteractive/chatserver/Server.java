package com.egtinteractive.chatserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private final static String QUIT_COMMAND = "-quit";
    private final static String HOST = "localhost"; 
    private final static int PORT = 1111;
    private final Map<String, ChatRoom> chatRooms;
    private ServerSocket serverSocket;

    public Server() {
	this.chatRooms = new ConcurrentHashMap<>();
	chatRooms.put("Defalut", new ChatRoom("Default"));
    }

    public void start() {
	try {
	    final InetAddress address = InetAddress.getByName(HOST);
	   

	    this.serverSocket = new ServerSocket(PORT, 100, address);

	    System.out.println("Listening on " + serverSocket);

	    while (!Thread.currentThread().isInterrupted()) {

		final Socket newSocket = serverSocket.accept();
		System.out.println("Connection established.");

		final Client newClient = new Client(newSocket);

		new ClientThread(newClient, this).start();
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
	    for (ChatRoom room : chatRooms.values()) {
		room.closeRoom();
	    }
	    serverSocket.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    boolean putClientInRoom(final String selectedRoom, final Client client) {

	if (selectedRoom.contains(QUIT_COMMAND)) {
	    client.closeClient();
	    return true;
	}

	ChatRoom chosenRoom = chatRooms.get(selectedRoom);
	if (chosenRoom == null) {
	    chatRooms.put(selectedRoom, new ChatRoom(selectedRoom));
	    client.sendMsg("You made a new room!");
	    chosenRoom = chatRooms.get(selectedRoom);
	}
	chosenRoom.addClient(client);

	client.setRoom(chosenRoom);
	client.sendMsg("You joined: " + chosenRoom.toString());

	chosenRoom.sendToAll("User " + client.getName() + " joined room.", client.getName());

	return true;
    }
}
