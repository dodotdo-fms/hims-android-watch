package com.example.mycom.hims.model;

import com.google.gson.annotations.SerializedName;

public class Form {

	@SerializedName("id")
	private int id;
	
	@SerializedName("form_num")
	private int number;
	
	@SerializedName("total_point")
	private int totalPoint;
	
	@SerializedName("form_type")
	private String type;
	
	@SerializedName("description")
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(int totalPoint) {
		this.totalPoint = totalPoint;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
