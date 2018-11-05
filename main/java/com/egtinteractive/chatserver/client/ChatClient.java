package com.egtinteractive.chatserver.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

import com.egtinteractive.chatserver.room.ChatRoom;
import com.egtinteractive.chatserver.room.Room;
import com.egtinteractive.chatserver.room.RoomManager;

public class ChatClient implements Client {
    private static final String QUIT_COMMAND = "-quit";
    private final int anonymousNumber;
    private final RoomManager roomManager;
    private final Socket socket;

    private final ClientReader clientReader;
    private final ClientWriter clientWriter;

    private String name;
    private ChatRoom room;

    private ChatClient(final int number, final Socket socket, final RoomManager roomManager, ClientReader reader,
	    ClientWriter writer) throws IOException {
	this.anonymousNumber = number;
	this.socket = socket;
	this.name = "Anonymus";
	this.roomManager = roomManager;

	this.clientWriter = writer;
	this.clientReader = reader;

	this.setRoomAndName();
    }

    public static Client newClient(final int number, final Socket socket, final RoomManager roomManager) {
	try {
	    final OutputStream outputStream = socket.getOutputStream();
	    final InputStream inputStream = socket.getInputStream();

	    final ClientReader reader = new ClientReader(inputStream);
	    final ClientWriter writer = new ClientWriter(outputStream);

	    return new ChatClient(number, socket, roomManager, reader, writer);

	} catch (IOException e) {
	    e.printStackTrace();
	    try {
		socket.close();
	    } catch (IOException e1) {
		e1.printStackTrace();
	    }
	    return null;
	}
    }

    private void setRoomAndName() {
	final String pickMessage = "Type '-quit' for exit.\nPick a room: "; // list of rooms to add

	try {
	    this.sendMsg("Chose name: ");
	    this.selectName();

	    this.sendMsg(pickMessage);
	    this.pickRoom();

	    this.listenFromConsole();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	System.out.println("User " + this.getName() + " " + this.getAnonymousNumber() + " disconnected from server.");
    }

    @Override
    public void startClient() {
	clientReader.start();
	clientWriter.start();
    }

    @Override
    public void sendMsg(final String message) {
	try {
	    PrintWriter printWriter = new PrintWriter(this.socket.getOutputStream(), true);
	    printWriter.println(message);
	} catch (IOException e) {
	    System.out.println("User disconnected!");
	}

    }

    @Override
    public void listenFromConsole() throws IOException {
	if (!this.socket.isClosed()) {
	    String message;
	    while ((message = this.bufferedReader.readLine()) != null) {
		if (message.contains(QUIT_COMMAND)) {
		    this.room.removeClient(this);
		    break;
		}
		if (message.isEmpty()) {
		    continue;
		}
		final String messageToSend = this.getName() + " " + this.getAnonymousNumber() + " wrote: " + message;
		this.room.sendToAll(messageToSend, this);
	    }
	}
    }

    @Override
    public void selectName() throws IOException {
	final String name = this.clientReader.readLine();
	if (name != null) {
	    this.name = name;
	}
    }

    @Override
    public void pickRoom() throws IOException {
	String message;
	while ((message = this.bufferedReader.readLine()) != null) {
	    if (message != null && this.roomManager.putClientInRoom(message, this)) {
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
	    this.socket.shutdownInput();
	    this.socket.shutdownOutput();
	    this.socket.close();
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
	result = prime * result + this.anonymousNumber;
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
	if (this.anonymousNumber != other.anonymousNumber)
	    return false;
	return true;
    }
}
