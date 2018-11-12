package com.egtinteractive.chatserver.room;

import com.egtinteractive.chatserver.client.Client;

public interface Maneger {
    public void putClientInRoom(final String selectedRoom, final Client client);

    public void closeRooms();
}