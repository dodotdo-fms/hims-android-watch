package com.example.mycom.hims.model.api_response;

import com.google.gson.annotations.SerializedName;

public class PostRequirementResponse {

	@SerializedName("result")
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
