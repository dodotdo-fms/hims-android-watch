package com.example.mycom.hims.model.api_response;

import com.example.mycom.hims.model.VoiceMessage;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMessageResponse {

	@SerializedName("result")
	private List<VoiceMessage> voiceMessage;

	public List<VoiceMessage> getVoiceMessage() {
		return voiceMessage;
	}

	public void setVoiceMessage(List<VoiceMessage> voiceMessage) {
		this.voiceMessage = voiceMessage;
	}
	
}
