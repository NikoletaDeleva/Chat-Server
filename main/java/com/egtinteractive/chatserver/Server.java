package com.egtinteractive.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements Runnable {
    private boolean running;

    private ServerSocket serverSocket;
    private ArrayList<Channel> channels;

    private SocketListener socketListener;

    public Server(SocketListener socketListener) {
	this.socketListener = socketListener;
    }

    public void bind(int port) throws IOException {
	this.serverSocket = new ServerSocket(port);
    }

    public void start() {
	Thread thread = new Thread(this);
	thread.start();
    }

    public void stop() throws IOException {
	running = false;
	serverSocket.close();
    }

    @Override
    public void run() {
	channels = new ArrayList<>();

	running = true;
	while (running) {
	    try {
		Socket socket = serverSocket.accept();

		Channel channel = new Channel(socket, socketListener);
		channel.start();

		channels.add(channel);
	    } catch (SocketException e) {
		break;
	    } catch (IOException e) {
		break;
	    }
	}

	try {
	    for (Channel channel : channels) {
		channel.stop();
	    }

	    channels.clear();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void broadcast(String msg) {
	if (!running)
	    return;

	for (Channel channel : channels) {
	    channel.send(msg);
	}
    }

    public void remove(Channel channel) {
	if (!running)
	    return;

	channels.remove(channel);
    }

    public ArrayList<Channel> getChannels() {
	return channels;
    }
}
