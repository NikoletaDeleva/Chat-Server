package com.egtinteractive.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server implements Runnable {
    private boolean running;

    private ServerSocket serverSocket;
    private ArrayList<Channel> channels;

    private SocketListener socketListener;

    public Server(final SocketListener socketListener) {
	this.socketListener = socketListener;
    }

    public void bind(final int port) throws IOException {
	this.serverSocket = new ServerSocket(port);
    }

    public void start() {
	final Thread thread = new Thread(this);
	thread.start();
    }

    public void stop() throws IOException {
	this.running = false;
	this.serverSocket.close();
    }

    @Override
    public void run() {
	this.channels = new ArrayList<>();

	this.running = true;

	while (this.running) {
	    try {
		final Socket socket = this.serverSocket.accept();

		final Channel channel = new Channel(socket, this.socketListener);
		channel.start();

		this.channels.add(channel);
	    } catch (SocketException e) {
		break;
	    } catch (IOException e) {
		break;
	    }
	}

	try {
	    for (Channel channel : this.channels) {
		channel.stop();
	    }

	    this.channels.clear();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void broadcast(final String msg) {
	if (!this.running) {
	    return;
	}

	for (Channel channel : channels) {
	    channel.send(msg);
	}
    }

    public void remove(final Channel channel) {
	if (!this.running) {
	    return;
	}

	this.channels.remove(channel);
    }

    public ArrayList<Channel> getChannels() {
	return this.channels;
    }
}
