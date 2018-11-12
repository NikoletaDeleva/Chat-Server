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

		message = this.messageQueue.take();
		this.outputStream.write(message);
	    }
	} catch (InterruptedException | IOException e) {
	    e.printStackTrace();
	}
    }

    public void addMessage(final byte[] message) {
	this.messageQueue.offer(message);
    }

    public void close() {
	interrupt();
    }
}
