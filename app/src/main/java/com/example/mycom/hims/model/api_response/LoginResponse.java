package com.example.mycom.hims.model.api_response;


public class LoginResponse {

	LoginResult result;
	
	public String getToken() {
		return result.token;
	}

	public void setToken(String token) {
		result.token = token;
	}

	public String getName() {
		return result.name;
	}

	public void setName(String name) {
		result.name = name;
	}

	public String getTeam() {
		return result.team;
	}

	public void setTeam(String team) {
		result.team = team;
	}

	public String getPosition() {
		return result.position;
	}

	public void setPosition(String position) {
		result.position = position;
	}
	
	private class LoginResult {
		public String token;
		public String name;
		public String team;
		public String position;
	}
}