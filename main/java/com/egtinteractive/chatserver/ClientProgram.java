package com.egtinteractive.chatserver;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientProgram implements SocketListener {
    @Override
    public void onConnected(Channel channel) {
	System.out.println("Connected.");
    }

    @Override
    public void onDisconnected(Channel channel) {
	System.out.println("Disconnected.");
    }

    @Override
    public void onReceived(Channel channel, String msg) {
	System.out.println(msg);
    }

    public void start() throws UnknownHostException, IOException {
	Scanner scanner = new Scanner(System.in);

	System.out.print("Name : ");
	String name = scanner.nextLine();

	System.out.print("Port : ");
	int port = Integer.parseInt(scanner.nextLine());

	Socket socket = new Socket("localhost", port);
	System.out.println("Connected");

	Channel channel = new Channel(socket, this);
	channel.start();

	while (true) {
	    String msg = scanner.nextLine();

	    if (msg.isEmpty())
		break;

	    channel.send(name + " >> " + msg);
	}

	scanner.close();
	channel.stop();

	System.out.println("Closed");
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
	ClientProgram program = new ClientProgram();
	program.start();
    }

}
