package com.egtinteractive.chatserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatRoom implements Room {
    private final String name;
    private final Map<Integer, ChatClient> mapClients;

    public ChatRoom(String name) {
	this.name = name;
	this.mapClients = new ConcurrentHashMap<>();
    }

    @Override
    public void addClient(final ChatClient client) {
	mapClients.putIfAbsent(client.getAnonymousNumber(), client);
    }

    void removeClient(final Client client) {

	if (mapClients.containsKey(client.getAnonymousNumber())) {
	    mapClients.get(client.getAnonymousNumber()).closeClient();
	    mapClients.remove(client.getAnonymousNumber());
	    String quitMsg = "User " + client.getName() + " has left.";
	    sendToAll(quitMsg, client);
	}
    }

    @Override
    public void sendToAll(final String message, final Client client) {

	for (Client cl : mapClients.values()) {
	    if (cl == null || cl.getAnonymousNumber() == client.getAnonymousNumber()) {
		continue;
	    }
	    cl.sendMsg(message);
	}
    }

    @Override
    public boolean containsClient(String name) {
	for (Integer key : mapClients.keySet()) {
	    if (mapClients.get(key).getName().equals(name)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public void closeRoom() {

	for (Client client : mapClients.values()) {
	    client.closeClient();
	}
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
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
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return name;
    }

}
