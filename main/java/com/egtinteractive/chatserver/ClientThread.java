package com.egtinteractive.chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientThread extends Thread {
    private final Client client;
    private final Server server;

    public ClientThread(Client client, Server server) {
	this.server = server;
	this.client = client;
    }

    @Override
    public void run() {

	try (final BufferedReader bufferedReader = new BufferedReader(
		new InputStreamReader(client.getSocket().getInputStream()));) {

	    client.sendMsg("Chose name: ");
	    this.selectName(bufferedReader);

	    final String pickMessage = "Type '-quit' for exit.\nPick a room: "; // list of rooms to add

	    client.sendMsg(pickMessage);
	    this.pickRoom(bufferedReader);

	    client.listenFromConsole();

	    System.out.println("User " + client.getName() + " disconnected from server.");
	    client.disconnectFromRoom();

	} catch (IOException e) {
	    System.out.println("Client thread terminated.");
	    e.printStackTrace();
	}
    }

    private void selectName(BufferedReader bufferedReader) throws IOException {
	final String name = bufferedReader.readLine();
	if (name != null && !client.getRoom().containsClient(name)) {
	    this.client.setName(name);
	}

    }

    private void pickRoom(BufferedReader bufferedReader) throws IOException {
	String message;
	while ((message = bufferedReader.readLine()) != null) {
	    if (message != null && server.putClientInRoom(message, client)) {
		break;
	    }
	}
    }

}
