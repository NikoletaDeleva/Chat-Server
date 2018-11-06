package com.egtinteractive.chatserver.client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientWriter extends Thread {
    private final BlockingQueue<byte[]> messageQueue;
    private final OutputStream outputStream;

    public ClientWriter(OutputStream outputStream) {
	this.messageQueue = new LinkedBlockingQueue<>(10000);
	this.outputStream = outputStream;
    }

    @Override
    public void run() {
	try {
	    byte[] message;
	    while (!isInterrupted()) {

		message = messageQueue.take();
		outputStream.write(message);
	    }
	} catch (InterruptedException | IOException e) {
	    e.printStackTrace();
	}
    }

    public void addMessage(final byte[] message) {
	messageQueue.offer(message);
    }

    public boolean isWorking() {
	return isAlive();
    }

    public void close() {
	interrupt();
    }
}
