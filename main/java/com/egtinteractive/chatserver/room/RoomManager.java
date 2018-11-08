package com.egtinteractive.chatserver.room;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.egtinteractive.chatserver.client.ChatClient;

public class RoomManager {
    private Map<String, Room> chatRooms;

    public RoomManager() {
	this.chatRooms = new ConcurrentHashMap<>();
	this.chatRooms.put("Defalut", new ChatRoom("Default"));
    }

    public void putClientInRoom(final String selectedRoom, final ChatClient client) {

	Room chosenRoom = this.chatRooms.get(selectedRoom);
	if (chosenRoom == null) {
	    this.chatRooms.put(selectedRoom, new ChatRoom(selectedRoom));
	    client.sendMsg("You made a new room!" + '\n');
	    chosenRoom = this.chatRooms.get(selectedRoom);
	}
	chosenRoom.addClient(client);

	client.setRoom(chosenRoom);
	client.sendMsg("You joined: " + client.getRoom().toString() + '\n');

	chosenRoom.sendToAll(
		("User " + client.toString() + client.getAnonymousNumber() + " joined room." + '\n').getBytes(), client);
    }

    public void closeRooms() {
	for (Room room : this.chatRooms.values()) {
	    room.closeRoom();
	}
    }
    
    public Room getDefaultRoom() {
	return chatRooms.get("Default");
    }
}