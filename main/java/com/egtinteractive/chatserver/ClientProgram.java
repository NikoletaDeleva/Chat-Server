package com.egtinteractive.chatserver;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientProgram implements SocketListener {
    @Override
    public void onConnected(final Channel channel) {
	System.out.println("Connected.");
    }

    @Override
    public void onDisconnected(final Channel channel) {
	System.out.println("Disconnected.");
    }

    @Override
    public void onReceived(final Channel channel, final String msg) {
	System.out.println(msg);
    }

    public void start() throws UnknownHostException, IOException {
	final Scanner scanner = new Scanner(System.in);

	System.out.print("Name : ");
	final String name = scanner.nextLine();

	System.out.print("Port : ");
	final int port = Integer.parseInt(scanner.nextLine());

	final Socket socket = new Socket("localhost", port);
	System.out.println("Connected");

	final Channel channel = new Channel(socket, this);
	channel.start();

	while (true) {
	    final String msg = scanner.nextLine();

	    if (msg.isEmpty()) {
		break;
	    }

	    channel.send(name + " >> " + msg);
	}

	scanner.close();
	channel.stop();

	System.out.println("Closed");
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
	final ClientProgram program = new ClientProgram();
	program.start();
    }

}
