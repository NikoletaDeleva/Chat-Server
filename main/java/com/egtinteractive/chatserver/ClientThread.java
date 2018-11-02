package com.egtinteractive.chatserver;

import java.io.IOException;

public class ClientThread extends Thread {
    private final Client client;

    public ClientThread(final Client client) {
	this.client = client;
    }

    @Override
    public void run() {

	try {
	    final String pickMessage = "Type '-quit' for exit.\nPick a room: "; // list of rooms to add
	    
	    this.client.sendMsg("Chose name: ");
	    this.client.selectName();
	    
	    this.client.sendMsg(pickMessage);
	    this.client.pickRoom();

	    this.client.listenFromConsole();

	    System.out.println("User " + this.client.getName() + " " + this.client.getAnonymousNumber() + " disconnected from server.");
	    this.client.disconnectFromRoom();

	} catch (IOException e) {
	    System.out.println("Client thread terminated.");
	    e.printStackTrace();
	}
    }

}
