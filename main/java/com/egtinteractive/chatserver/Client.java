package com.egtinteractive.chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String QUIT_COMMAND = "-quit";
    private final int anonymousNumber;
    private final Socket socket;
    private String name;
    private ChatRoom room;

    public Client(final int number, final Socket newSocket) {
	this.anonymousNumber = number;
	this.socket = newSocket;
	this.name = "Anonymus";
    }

    public void sendMsg(String message) {
	try {
	    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
	    printWriter.println(message);
	} catch (IOException e) {
	    System.out.println("User disconnected!");
	}

    }

    public void listenFromConsole() throws IOException {
	if (!socket.isClosed()) {
	    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	    String message;
	    while ((message = bufferedReader.readLine()) != null) {
		if (message.contains(QUIT_COMMAND)) {
		    room.removeClient(this);
		    break;
		}
		if (message.isEmpty()) {
		    continue;
		}
		final String messageToSend = this.getName() + " wrote: " + message;
		room.sendToAll(messageToSend, this);
	    }
	}
    }

    public void disconnectFromRoom() {
	if (room != null) {
	    room.removeClient(this);
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

    public Socket getSocket() {
	return socket;
    }

    public int getAnonymousNumber() {
	return this.anonymousNumber;
    }

    public ChatRoom getRoom() {
	return this.room;
    }

    public void setRoom(ChatRoom chosenRoom) {
	this.room = chosenRoom;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + anonymousNumber;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Client other = (Client) obj;
	if (anonymousNumber != other.anonymousNumber)
	    return false;
	return true;
    }
}
