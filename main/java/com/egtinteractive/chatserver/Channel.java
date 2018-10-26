package com.egtinteractive.chatserver;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Channel implements Runnable {
    private final Socket socket;
    private final SocketListener socketListener;
    private Scanner reader;
    private PrintWriter writer;

    private boolean running;

    public Channel(final Socket socket, final SocketListener socketListener) {
	this.socket = socket;
	this.socketListener = socketListener;
    }

    public void start() {
	final Thread thread = new Thread(this);
	thread.start();
    }

    public void stop() throws IOException {
	this.running = false;

	this.writer.close();
	this.reader.close();
	this.socket.close();
    }

    @Override
    public void run() {
	try {
	    final OutputStream outputStream = this.socket.getOutputStream();
	    this.writer = new PrintWriter(outputStream);

	    final InputStream inputStream = this.socket.getInputStream();
	    this.reader = new Scanner(inputStream);

	    if (this.socketListener != null) {
		this.socketListener.onConnected(this);
	    }

	    this.running = true;
	    while (this.running) {
		try {
		    final String msg = reader.nextLine();

		    if (this.socketListener != null) {
			this.socketListener.onReceived(this, msg);
		    }
		} catch (NoSuchElementException e) {
		    break;
		}
	    }

	    if (this.socketListener != null) {
		this.socketListener.onDisconnected(this);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    public void send(final String msg) {
	this.writer.println(msg);
	this.writer.flush();
    }

    public Socket getSocket() {
	return this.socket;
    }
}
