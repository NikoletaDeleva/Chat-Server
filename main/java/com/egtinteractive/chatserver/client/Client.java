package com.egtinteractive.chatserver.client;

import com.egtinteractive.chatserver.room.Room;

public interface Client {

    public void sendMsg(String message, ClientWriter clientWriter);

    public void disconnectFromRoom();

    public void closeClient();

    public int getAnonymousNumber();

    public void setRoom(Room chosenRoom);

    public void startClient();

    public ClientReader getReader();

    public ClientWriter getWriter();

    public void close();

    public void setRoomAndName(ClientWriter writer, ClientReader reader);

    public void sendToOthers(byte[] bytes);

}
