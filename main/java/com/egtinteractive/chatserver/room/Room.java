package com.egtinteractive.chatserver.room;

import com.egtinteractive.chatserver.client.ChatClient;
import com.egtinteractive.chatserver.client.Client;

public interface Room {

    public void addClient(ChatClient client);

    public void removeClient(final Client client);

    public void closeRoom();

    public void sendToAll(byte[] message, Client client);

}