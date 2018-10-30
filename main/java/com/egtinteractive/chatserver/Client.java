package com.egtinteractive.chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String QUIT_COMMAND = "-quit";
    private String name;
    final Socket socket;
    private ChatRoom room; 
    private int anonymousNumber = 0;

    public Client(Socket socket) {
	this.socket = socket;
	this.name = "anonymous" + anonymousNumber++;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void sendMsg(String message) {
	try (PrintWriter printWriter = new PrintWriter(socket.getOutputStream())) {
	    printWriter.println(message);
	} catch (IOException e) {
	    System.out.println("User disconnected!");
	}

    }

    public void closeClient() {
	try {
	    socket.shutdownInput();
	    socket.shutdownOutput();
	    socket.close();
	} catch (IOException e) {
	    System.out.println("Shutting down fail!");
	}
    }

    public void setRoom(ChatRoom chosenRoom) {
	this.room = chosenRoom;
    }

    public ChatRoom getRoom() {
	return this.room;
    }

    public void disconnectFromRoom() {
	if (room != null) {
	    room.removeClient(name);
	}
    }

    public Socket getSocket() {
	return socket;
    }

    public void listenFromConsole() throws IOException {
	if (!socket.isClosed()) {
	    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	    String message;
	    while ((message = bufferedReader.readLine()) != null) {
		if (message.contains(QUIT_COMMAND)) {
		    room.removeClient(name);
		    break;
		}
		if (message.isEmpty()) {
		    continue;
		}
		final String messageToSend = this.getName() + " wrote: " + message;
		room.sendToAll(messageToSend, name);
	    }
	}
    }
}
