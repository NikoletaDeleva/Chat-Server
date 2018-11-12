package com.egtinteractive.chatserver.client;

import com.egtinteractive.chatserver.room.Room;

public interface Client {

    public void sendMsg(final String message);

    public void disconnectFromRoom();

    public void closeClient();

    public int getAnonymousNumber();

    public void setRoom(final String room);

    public void startClient();

    public ClientReader getReader();

    public ClientWriter getWriter();

    public void close();

    public void sendToOthers(final byte[] bytes);

    public void setName(final String name);

    public String getName();

    public Room getRoom();

    void setRoom(final Room chosenRoom);

}
