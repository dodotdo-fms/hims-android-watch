package com.example.mycom.hims.model.api_response;

import com.example.mycom.hims.model.Channel;
import com.google.gson.annotations.SerializedName;

public class PostChannelResponse {

	@SerializedName("result")
	private Channel channel;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
}
