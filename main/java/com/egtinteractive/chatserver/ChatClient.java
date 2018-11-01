package com.egtinteractive.chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient implements Client {
    private static final String QUIT_COMMAND = "-quit";
    private final int anonymousNumber;
    private final Socket socket;
    private final RoomManager roomManager;
    private String name;
    private ChatRoom room;
    private final BufferedReader bufferedReader;

    public ChatClient(final int number, final Socket newSocket, final RoomManager roomManager) throws IOException {
	this.anonymousNumber = number;
	this.socket = newSocket;
	this.name = "Anonymus";
	this.roomManager = roomManager;
	this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    @Override
    public void sendMsg(String message) {
	try {
	    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
	    printWriter.println(message);
	} catch (IOException e) {
	    System.out.println("User disconnected!");
	}

    }

    @Override
    public void listenFromConsole() throws IOException {
	if (!socket.isClosed()) {
	    String message;
	    while ((message = bufferedReader.readLine()) != null) {
		if (message.contains(QUIT_COMMAND)) {
		    room.removeClient(this);
		    break;
		}
		if (message.isEmpty()) {
		    continue;
		}
		final String messageToSend = this.getName()+ " " + this.getAnonymousNumber() + " wrote: " + message;
		room.sendToAll(messageToSend, this);
	    }
	}
    }

    @Override
    public void selectName() throws IOException {
	String name = bufferedReader.readLine();
	if (name != null) {
	    this.name = name;
	}
    }

    @Override
    public void pickRoom() throws IOException {
	String message;
	while ((message = bufferedReader.readLine()) != null) {
	    if (message != null && roomManager.putClientInRoom(message, this)) {
		break;
	    }
	}
    }

    @Override
    public void disconnectFromRoom() {
	if (this.room != null) {
	    this.room.removeClient(this);
	}
    }

    @Override
    public void closeClient() {
	try {
	    socket.shutdownInput();
	    socket.shutdownOutput();
	    socket.close();
	} catch (IOException e) {
	    System.out.println("Shutting down fail!");
	}
    }


    @Override
    public int getAnonymousNumber() {
	return this.anonymousNumber;
    }

    @Override
    public void setRoom(Room chosenRoom) {
	this.room = (ChatRoom) chosenRoom;
    }

    @Override
    public String getName() {
	return this.name;
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
	ChatClient other = (ChatClient) obj;
	if (anonymousNumber != other.anonymousNumber)
	    return false;
	return true;
    }
}
