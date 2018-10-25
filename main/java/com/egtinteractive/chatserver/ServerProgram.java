package com.egtinteractive.chatserver;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServerProgram implements SocketListener {
    private Server server;
    private int port;
    private List<String> clients;
    
 
public ServerProgram(int port) {
	this.port = port;
	this.clients = new ArrayList<>();
    }
    @Override
    public void onConnected(Channel channel) {
	Socket socket = channel.getSocket();
	String hostName = socket.getInetAddress().getHostName();

	String msg = "Client connected from " + hostName + ":" + this.port;
	System.out.println(msg);

	for (Channel c : server.getChannels()) {
	    if (c != channel)
		c.send(msg);
	}
    }

    @Override
    public void onDisconnected(Channel channel) {
	server.remove(channel);

	Socket socket = channel.getSocket();
	String hostName = socket.getInetAddress().getHostName();

	String msg = "Client disconnected from " + hostName + ":" + this.port;
	System.out.println();

	server.broadcast(msg);
    }

    @Override
    public void onReceived(Channel channel, String msg) {
	System.out.println(msg);
	server.broadcast(msg);
    }

    public List<String> getClients() {
	return clients;
    }

    public void start() throws IOException {
	Scanner scanner = new Scanner(System.in);

	server = new Server(this);
	server.start();
	System.out.println("New room created!");

	while (true) {
	    String msg = scanner.nextLine();

	    if (msg.isEmpty())
		break;

	    server.broadcast("Server >> " + msg);
	}

	scanner.close();
	server.stop();

	System.out.println("Server has closed.");
    }

    public void addNewClient(String name) {
	this.clients.add(name);
    }
    
    
}
