package com.example.mycom.hims.model;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Evaluation {

	@SerializedName("id")
	private int id;
	
	@SerializedName("timestamp")
	private Date timestamp;
	
	@SerializedName("check")
	private boolean check;
	
	@SerializedName("inspection_description")
	private String inspectionDesc;
	
	@SerializedName("total_point")
	private int totalPoint;
	
	@SerializedName("cleaner_id")
	private String cleanerId;
	
	@SerializedName("evaluator_id")
	private String evaluatorId;
	
	@SerializedName("room_num")
	private int roomNumber;
	
	@SerializedName("type")
	private String type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getInspectionDesc() {
		return inspectionDesc;
	}

	public void setInspectionDesc(String inspectionDesc) {
		this.inspectionDesc = inspectionDesc;
	}

	public int getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(int totalPoint) {
		this.totalPoint = totalPoint;
	}

	public String getCleanerId() {
		return cleanerId;
	}

	public void setCleanerId(String cleanerId) {
		this.cleanerId = cleanerId;
	}

	public String getEvaluatorId() {
		return evaluatorId;
	}

	public void setEvaluatorId(String evaluatorId) {
		this.evaluatorId = evaluatorId;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
