package com.egtinteractive.chatserver;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ServerProgram implements SocketListener {
    private Server server;

    @Override
    public void onConnected(Channel channel) {
	Socket socket = channel.getSocket();
	String hostName = socket.getInetAddress().getHostName();
	int port = socket.getPort();

	String msg = "Client connected from " + hostName + ":" + port;
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
	int port = socket.getPort();

	String msg = "Client disconnected from " + hostName + ":" + port;
	System.out.println();

	server.broadcast(msg);
    }

    @Override
    public void onReceived(Channel channel, String msg) {
	System.out.println(msg);
	server.broadcast(msg);
    }

    public void start() throws IOException {
	Scanner scanner = new Scanner(System.in);

	System.out.print("Port : ");
	int port = Integer.parseInt(scanner.nextLine());

	server = new Server(this);
	server.bind(port); 
	server.start(); 
	System.out.println("Server has started.");

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

    public static void main(String[] args) throws IOException {
	ServerProgram program = new ServerProgram();
	program.start();
    }
}
