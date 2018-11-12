package com.egtinteractive.chatserver.room;

import com.egtinteractive.chatserver.client.Client;

public interface Room {

    public void addClient(final Client client);

    public void removeClient(final Client client);

    public void closeRoom();

    public void sendToAll(final byte[] message,final  Client client);

}