package com.egtinteractive.chatserver.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.egtinteractive.chatserver.room.Room;
import com.egtinteractive.chatserver.room.RoomManager;

public class ChatClient implements Client {
    private final int anonymousNumber;
    private final RoomManager roomManager;
    private final Socket socket;

    private ClientReader clientReader;
    private ClientWriter clientWriter;

    private volatile String name;
    private Room room;

    public ChatClient(final int number, final Socket socket, final RoomManager roomManager) {
	this.anonymousNumber = number;
	this.socket = socket;
	this.roomManager = roomManager;
	this.room = roomManager.getDefaultRoom();

	this.newClient(socket);

    }

    private void newClient(final Socket socket) {

	try {
	    final OutputStream outputStream = socket.getOutputStream();
	    final InputStream inputStream = socket.getInputStream();

	    this.clientWriter = new ClientWriter(outputStream);
	    this.clientReader = new ClientReader(inputStream, this);

	} catch (IOException e) {
	    e.printStackTrace();
	    try {
		socket.close();
	    } catch (IOException e1) {
		e1.printStackTrace();
	    }

	}

    }

    @Override
    public void startClient() {
	if (!this.socket.isClosed()) {
	    this.clientWriter.start();
	    this.clientReader.start();
	}
    }

    @Override
    public void sendToOthers(final byte[] message) {
	this.room.sendToAll(message, this);
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
    public void sendMsg(final String message) {
	this.clientWriter.addMessage(message.getBytes());
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
    public void setRoom(final String room) {
	this.roomManager.putClientInRoom(room, this);
    }

    @Override
    public Room getRoom() {
	return this.room;
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
    public void setRoom(final Room chosenRoom) {
	this.room = chosenRoom;
    }

    @Override
    public void setName(final String name) {
	this.name = name;
    }

    @Override
    public String getName() {
	return this.name;
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
