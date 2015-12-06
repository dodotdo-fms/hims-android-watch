package com.example.mycom.hims.model.api_response;

import java.util.List;

import com.example.mycom.hims.model.Channel;
import com.google.gson.annotations.SerializedName;

public class GetChannelResponse {

	@SerializedName("result")
	private List<Channel> channels;

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
	
}
