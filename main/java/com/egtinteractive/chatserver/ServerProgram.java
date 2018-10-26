package com.egtinteractive.chatserver;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ServerProgram implements SocketListener {
    private Server server;

    @Override
    public void onConnected(final Channel channel) {
	final Socket socket = channel.getSocket();
	final String hostName = socket.getInetAddress().getHostName();
	final int port = socket.getPort();

	final String msg = "Client connected from " + hostName + ":" + port;
	System.out.println(msg);

	for (Channel c : this.server.getChannels()) {
	    if (c != channel)
		c.send(msg);
	}
    }

    @Override
    public void onDisconnected(final Channel channel) {
	this.server.remove(channel);

	final Socket socket = channel.getSocket();
	final String hostName = socket.getInetAddress().getHostName();
	final int port = socket.getPort();

	final String msg = "Client disconnected from " + hostName + ":" + port;
	System.out.println();

	this.server.broadcast(msg);
    }

    @Override
    public void onReceived(final Channel channel, final String msg) {
	System.out.println(msg);
	this.server.broadcast(msg);
    }

    public void start() throws IOException {
	final Scanner scanner = new Scanner(System.in);

	System.out.print("Port : ");
	final int port = Integer.parseInt(scanner.nextLine());

	this.server = new Server(this);
	this.server.bind(port);
	this.server.start();
	System.out.println("Server has started.");

	while (true) {
	    final String msg = scanner.nextLine();

	    if (msg.isEmpty()) {
		break;
	    }
	    this.server.broadcast("Server >> " + msg);
	}

	scanner.close();
	this.server.stop();

	System.out.println("Server has closed.");
    }

    public static void main(String[] args) throws IOException {
	final ServerProgram program = new ServerProgram();
	program.start();
    }
}
