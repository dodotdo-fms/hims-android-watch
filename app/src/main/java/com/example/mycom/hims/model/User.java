package com.example.mycom.hims.model;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class User {
	
	private String id;
	private String name;
	private String position;
	private String team;
	@SerializedName("recent_login_timestamp")
	private Date recentLoginTimestamp;
	@SerializedName("register_timestamp")
	private Date registerTimestamp;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPosition() {
		return position;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getTeam() {
		return team;
	}
	
	public void setTeam(String team) {
		this.team = team;
	}
	
	public Date getRecentLoginTimestamp() {
		return recentLoginTimestamp;
	}
	
	public void setRecentLoginTimestamp(Date recentLoginTimestamp) {
		this.recentLoginTimestamp = recentLoginTimestamp;
	}
	
	public Date getRegisterTimestamp() {
		return registerTimestamp;
	}
	
	public void setRegisterTimestamp(Date registerTimestamp) {
		this.registerTimestamp = registerTimestamp;
	}
	
}
