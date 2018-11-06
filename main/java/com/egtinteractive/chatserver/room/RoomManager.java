package com.egtinteractive.chatserver.room;

import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.egtinteractive.chatserver.client.ChatClient;

public class RoomManager {
    private final static String QUIT_COMMAND = "-quit";
    private Map<String, Room> chatRooms;

    public RoomManager() {
	this.chatRooms = new ConcurrentHashMap<>();
	this.chatRooms.put("Defalut", new ChatRoom("Default"));
    }

    public boolean putClientInRoom(final String selectedRoom, final ChatClient client,
	    final OutputStream outputStream) {

	if (selectedRoom.contains(QUIT_COMMAND)) {
	    client.closeClient();
	    return true;
	}

	Room chosenRoom = this.chatRooms.get(selectedRoom);
	if (chosenRoom == null) {
	    this.chatRooms.put(selectedRoom, new ChatRoom(selectedRoom));
	    client.sendMsg("You made a new room!" + '\n', outputStream);
	    chosenRoom = this.chatRooms.get(selectedRoom);
	}
	chosenRoom.addClient(client);

	client.setRoom(chosenRoom);
	client.sendMsg("You joined: " + chosenRoom.toString() + '\n', outputStream);

	chosenRoom.sendToAll(
		("User " + client.toString() + client.getAnonymousNumber() + " joined room." + '\n').getBytes(), client);

	return true;
    }

    public void closeRooms() {
	for (Room room : this.chatRooms.values()) {
	    room.closeRoom();
	}
    }
}