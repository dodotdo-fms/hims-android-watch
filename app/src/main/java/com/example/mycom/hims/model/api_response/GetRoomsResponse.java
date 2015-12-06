package com.example.mycom.hims.model.api_response;

import java.util.List;

import com.example.mycom.hims.model.Room;
import com.google.gson.annotations.SerializedName;

public class GetRoomsResponse {

	@SerializedName("result")
	private List<Room> rooms;

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}
	
}
