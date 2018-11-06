package com.egtinteractive.chatserver.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.egtinteractive.chatserver.room.ChatRoom;

public class ClientReader extends Thread {
    private final InputStream inputStream;
    private final ChatRoom chatRoom;
    private final Client client;

    public ClientReader(final InputStream inputStream, Client client, ChatRoom chatRoom) {
	this.inputStream = inputStream;
	this.chatRoom = chatRoom;
	this.client = client;
    }

    @Override
    public void run() {
	try {
	    byte[] messageBuffer = new byte[1024];
	    int position = 0;
	    int readByte;
	    while ((readByte = inputStream.read()) != -1) {
		if (position >= messageBuffer.length - 2) {
		    messageBuffer = Arrays.copyOf(messageBuffer, 2 * messageBuffer.length);
		}

		if (readByte == '\r' || readByte == '\n') {
		    messageBuffer[position++] = (byte) '\r';
		    messageBuffer[position++] = (byte) '\n';
		    byte[] message = Arrays.copyOf(messageBuffer, position);
		    chatRoom.sendToAll((client.toString() + ": ").getBytes(), this.client);
		    chatRoom.sendToAll(message, this.client);
		    position = 0;

		    if (readByte == '\r') {
			readByte = inputStream.read();
			if (readByte == -1) {
			    break;
			}
			if (readByte != '\n' && readByte != '\0') {
			    messageBuffer[position++] = (byte) readByte;
			}
		    }
		} else {
		    messageBuffer[position++] = (byte) readByte;
		}
	    }

	} catch (IOException e) {
	    e.printStackTrace();

	}
    }

    public boolean isWorking() {
	return isAlive();
    }
}
