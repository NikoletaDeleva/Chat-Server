package com.egtinteractive.chatserver;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ClientProgram implements SocketListener {; 
    private Map<String, List<String>> rooms = new ConcurrentHashMap<>();
    
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

	System.out.print("Room : ");
	String room = scanner.nextLine();
	int port = room.hashCode();
	
	if(!isExistingRoom(room)) {
	    ServerProgram server = new ServerProgram(port);
	    server.addNewClient(name); 
	    rooms.put(room, server.getClients());
	    server.start();
	}else {
	    rooms.get(room).add(name);
	}
	
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
    
    public boolean isExistingRoom(String room) {
 	return rooms.containsKey(room);
     }
}
