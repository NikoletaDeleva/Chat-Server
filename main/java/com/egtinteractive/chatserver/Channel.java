package com.egtinteractive.chatserver;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Channel implements Runnable {
    private Socket socket;
    private Scanner reader;
    private PrintWriter writer;

    private boolean running;

    private SocketListener socketListener;

    public Channel(Socket socket, SocketListener socketListener) {
	this.socket = socket;
	this.socketListener = socketListener;
    }

    public void start() {
	Thread thread = new Thread(this);
	thread.start();
    }

    public void stop() throws IOException {
	running = false;

	writer.close();
	reader.close();
	socket.close();
    }

    @Override
    public void run() {
	try {
	    OutputStream outputStream = socket.getOutputStream();
	    writer = new PrintWriter(outputStream);

	    InputStream inputStream = socket.getInputStream();
	    reader = new Scanner(inputStream);

	    if (null != socketListener)
		socketListener.onConnected(this);

	    running = true;
	    while (running) {
		try {
		    String msg = reader.nextLine();

		    if (null != socketListener)
			socketListener.onReceived(this, msg);
		} catch (NoSuchElementException e) {
		    break;
		}
	    }

	    if (null != socketListener)
		socketListener.onDisconnected(this);
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    public void send(String msg) {
	writer.println(msg);
	writer.flush();
    }

    public Socket getSocket() {
	return socket;
    }
}
