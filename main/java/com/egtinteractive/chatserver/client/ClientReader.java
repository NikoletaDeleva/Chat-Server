package com.egtinteractive.chatserver.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ClientReader extends Thread {
    private final InputStream inputStream;
    private final Client client;

    public ClientReader(final InputStream inputStream, Client client) {
	this.inputStream = inputStream;
	this.client = client;
    }

    @Override
    public void run() {
	try {
	    this.client.setRoomAndName(client.getWriter(), client.getReader());

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
		    this.client.sendToOthers((this.client.toString() + ": ").getBytes());
		    this.client.sendToOthers(message);
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
}
