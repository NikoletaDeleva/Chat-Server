package com.egtinteractive.chatserver;

public interface Room {

    public void addClient(ChatClient client);

    public void removeClient(final Client client);

    public void sendToAll(String message, Client client);

    public void closeRoom();

    public boolean containsClient(String name);

}