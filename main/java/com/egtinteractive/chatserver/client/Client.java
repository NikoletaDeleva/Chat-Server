package com.egtinteractive.chatserver.client;

import com.egtinteractive.chatserver.room.Room;

public interface Client {

    public void sendMsg(String message);

    public void disconnectFromRoom();

    public void closeClient();

    public int getAnonymousNumber();

    public void setRoom(String room);

    public void startClient();

    public ClientReader getReader();

    public ClientWriter getWriter();

    public void close();

    public void sendToOthers(byte[] bytes);

    public void setName(String name);

    public String getName();

    public Room getRoom();

}
