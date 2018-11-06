package com.egtinteractive.chatserver.client;

import java.io.OutputStream;

import com.egtinteractive.chatserver.room.Room;

public interface Client {

    public void sendMsg(String message, OutputStream outputStream);

    public void disconnectFromRoom();

    public void closeClient();

    public int getAnonymousNumber();

    public void setRoom(Room chosenRoom);

    public void startClient();

    public ClientReader getReader();

    public ClientWriter getWriter();

    public void close();

}
