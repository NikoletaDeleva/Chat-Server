package com.egtinteractive.chatserver.room;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.egtinteractive.chatserver.client.ChatClient;
import com.egtinteractive.chatserver.client.Client;

public class ChatRoom implements Room {
    private final String name;
    private final Map<Integer, ChatClient> mapClients;
    volatile boolean isRunning;

    public ChatRoom(final String name) {
	this.name = name;
	this.isRunning = true;
	this.mapClients = new ConcurrentHashMap<>();
    }

    @Override
    public void addClient(final ChatClient client) {
	if (client != null) {
	    this.mapClients.putIfAbsent(client.getAnonymousNumber(), client);
	}
    }

    @Override
    public void removeClient(final Client client) {
	mapClients.remove(client.getAnonymousNumber());
	client.close();
	if (isRunning) {
	    sendToAll(("User " + client.toString() + "has left the room.").getBytes(), client);
	}
    }

    @Override
    public void sendToAll(final byte[] message, final Client client) {

	for (Client cl : this.mapClients.values()) {
	    if (cl.getReader() != client.getReader()) {
		cl.getWriter().addMessage(message);
	    }
	}
    }

    @Override
    public void closeRoom() {
	isRunning = false;
	for (Client client : this.mapClients.values()) {
	    client.closeClient();
	}
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
	return result;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	final ChatRoom other = (ChatRoom) obj;
	if (this.name == null) {
	    if (other.name != null)
		return false;
	} else if (!this.name.equals(other.name))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return this.name;
    }

}
