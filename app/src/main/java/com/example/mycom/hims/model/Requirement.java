package com.example.mycom.hims.model;

import com.google.gson.annotations.SerializedName;

public class Requirement {

	@SerializedName("origin")
	private String origin;
	
	@SerializedName("spanish")
	private String spanish;

	public String getOrigin() {
		if(origin == null){
			return "";
		}
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getSpanish() {
		if(spanish == null){
			return "";
		}
		return spanish;
	}

	public void setSpanish(String spanish) {
		this.spanish = spanish;
	}
	
}
