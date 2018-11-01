package com.egtinteractive.chatserver;

import java.io.IOException;

public class ClientThread extends Thread {
    private final Client client;

    public ClientThread(Client client) {
	this.client = client;
    }

    @Override
    public void run() {

	try {
	    final String pickMessage = "Type '-quit' for exit.\nPick a room: "; // list of rooms to add
	    
	    client.sendMsg("Chose name: ");
	    client.selectName();
	    
	    client.sendMsg(pickMessage);
	    client.pickRoom();

	    client.listenFromConsole();

	    System.out.println("User " + client.getName() + " " + client.getAnonymousNumber() + " disconnected from server.");
	    client.disconnectFromRoom();

	} catch (IOException e) {
	    System.out.println("Client thread terminated.");
	    e.printStackTrace();
	}
    }

}
