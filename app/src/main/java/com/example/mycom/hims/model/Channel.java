package com.example.mycom.hims.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Channel {

	@SerializedName("channel_id")
	private String id;
	
	@SerializedName("host_id")
	private String hostId;
	
	@SerializedName("channel_name")
	private String name;
	
	@SerializedName("member_list")
	private List<String> members;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}
	
}
