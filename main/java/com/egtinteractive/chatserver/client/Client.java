package com.egtinteractive.chatserver.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.egtinteractive.chatserver.room.Room;

public interface Client {

    public void sendMsg(final String message, final PrintWriter printWriter);

    public void disconnectFromRoom();

    public void closeClient();

    public int getAnonymousNumber();

    public void setRoomAndName(BufferedReader bufferedReader, PrintWriter printWriter) throws IOException;

    public void setRoom(Room chosenRoom);

    public void startClient();

    public ClientReader getReader();

    public ClientWriter getWriter();

    public void close();

}
