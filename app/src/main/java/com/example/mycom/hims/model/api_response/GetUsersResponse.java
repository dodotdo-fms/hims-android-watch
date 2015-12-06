package com.example.mycom.hims.model.api_response;

import java.util.List;

import com.example.mycom.hims.model.User;
import com.google.gson.annotations.SerializedName;

public class GetUsersResponse {

	@SerializedName("result")
	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
}
