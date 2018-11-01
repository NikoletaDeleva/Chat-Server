package com.egtinteractive.chatserver;

import java.io.IOException;

public interface Client {

    public void sendMsg(String message);

    public void listenFromConsole() throws IOException;

    public void selectName() throws IOException;

    public void pickRoom() throws IOException;

    public void disconnectFromRoom();

    public void closeClient();

    public int getAnonymousNumber();

    public void setRoom(Room chosenRoom);

    public String getName();

}