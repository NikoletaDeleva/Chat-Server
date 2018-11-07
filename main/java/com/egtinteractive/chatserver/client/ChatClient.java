package com.egtinteractive.chatserver.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.egtinteractive.chatserver.room.ChatRoom;
import com.egtinteractive.chatserver.room.Room;
import com.egtinteractive.chatserver.room.RoomManager;

public class ChatClient implements Client {
    private final int anonymousNumber;
    private final RoomManager roomManager;
    private final Socket socket;

    private ClientReader clientReader;
    private ClientWriter clientWriter;

    private String name;
    private Room room;

    public ChatClient(final int number, final Socket socket, final RoomManager roomManager) {
	this.anonymousNumber = number;
	this.socket = socket;
	this.name = "Anonymus";
	this.roomManager = roomManager;
	this.room = roomManager.getDefaultRoom();

	this.newClient(socket);

    }

    private void newClient(final Socket socket) {

	try {
	    final OutputStream outputStream = socket.getOutputStream();
	    final InputStream inputStream = socket.getInputStream();

	    this.clientWriter = new ClientWriter(outputStream);
	    this.clientReader = new ClientReader(inputStream, this, this.room);

	} catch (IOException e) {
	    e.printStackTrace();
	    try {
		socket.close();
	    } catch (IOException e1) {
		e1.printStackTrace();
	    }

	}

    }

    public void setRoomAndName(final BufferedReader bufferedReader, PrintWriter printWriter) throws IOException {
	final String pickMessage = "Type '-quit' for exit.\nPick a room: "; // list of rooms to add

	this.sendMsg("Chose name: ", printWriter);
	this.selectName(bufferedReader);

	this.sendMsg(pickMessage, printWriter);
	this.pickRoom(bufferedReader, printWriter);

    }

    private void selectName(final BufferedReader bufferedReader) throws IOException {
	final String name = bufferedReader.readLine();

	if (name != null) {
	    this.name = name;
	}
    }

    private void pickRoom(final BufferedReader bufferedReader, final PrintWriter printWriter) throws IOException {
	String message;

	while ((message = bufferedReader.readLine()) != null) {
	    if (message != null && this.roomManager.putClientInRoom(message, this, printWriter)) {
		break;
	    }
	}
    }

    @Override
    public void startClient() {
	if (!this.socket.isClosed()) {
	    this.clientReader.start();
	    this.clientWriter.start();

	}
    }

    @Override
    public void close() {
	try {
	    this.socket.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	this.clientReader.interrupt();
	this.clientWriter.interrupt();
    }

    @Override
    public void sendMsg(final String message, final PrintWriter printWriter) {
	printWriter.println(message);
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
    public ClientReader getReader() {
	return this.clientReader;
    }

    @Override
    public ClientWriter getWriter() {
	return this.clientWriter;
    }

    @Override
    public String toString() {
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
