package com.egtinteractive.chatserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatRoom {
    private final String name;
    private final Map<String, Client> mapClients;

    public ChatRoom(String name) {
	this.name = name;
	this.mapClients = new ConcurrentHashMap<>();
    }

    public void addClient(final Client client) {
	mapClients.put(client.getName(), client);
    }

    void removeClient(final String name) {

	if (mapClients.containsKey(name)) {
	    mapClients.get(name).closeClient();
	    mapClients.remove(name);
	    String quitMsg = "User " + name + " has left.";
	    sendToAll(quitMsg, name);
	}
    }

    public void sendToAll(final String message, final String name) {

	for (Client container : mapClients.values()) {
	    if (container == null || container.getName() == name) {
		continue;
	    }
	    container.sendMsg(message);
	}
    }

    public void closeRoom() {

	for (Client client : mapClients.values()) {
	    client.closeClient();
	}
    }
    
    public String getNameOfRoom() {
	return this.name;
    }
    
    public synchronized boolean containsClient(String name) {
	return mapClients.containsKey("name");
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
}
