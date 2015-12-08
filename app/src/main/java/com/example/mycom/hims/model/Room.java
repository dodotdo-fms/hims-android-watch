package com.example.mycom.hims.model;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Room {

	@SerializedName("room_number")
	private int number;
	
	@SerializedName("room_type")
	private String type;
	
	@SerializedName("state")
	private String state;
	
	@SerializedName("customer_name")
	private String customerName;
	
	@SerializedName("cleaner_id")
	private String cleanerId;
	
	@SerializedName("evaluator_id")
	private String evaluatorId;
	
	@SerializedName("checkin_time")
	private Date checkinTime;
	
	@SerializedName("requirement")
	private Requirement requirement;
	
	@SerializedName("checkout_time")
	private Date checkoutTime;
	
	@SerializedName("arrival_date")
	private Date arrivalDate;
	
	@SerializedName("departure_date")
	private Date departureDate;
	
	@SerializedName("clean_assigned_datetime")
	private Date cleanAssignedDate;
	
	@SerializedName("inspect_assigned_datetime")
	private Date inspectAssignedDate;
	

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public Date getCheckinTime() {
		return checkinTime;
	}

	public void setCheckinTime(Date checkinTime) {
		this.checkinTime = checkinTime;
	}

	public Requirement getRequirement() {
		return requirement;
	}

	public void setRequirement(Requirement requirement) {
		this.requirement = requirement;
	}

	public Date getCheckoutTime() {
		return checkoutTime;
	}

	public void setCheckoutTime(Date checkoutTime) {
		this.checkoutTime = checkoutTime;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public Date getCleanAssignedDate() {
		return cleanAssignedDate;
	}

	public void setCleanAssignedDate(Date cleanAssignedDate) {
		this.cleanAssignedDate = cleanAssignedDate;
	}

	public Date getInspectAssignedDate() {
		return inspectAssignedDate;
	}

	public void setInspectAssignedDate(Date inspectAssignedDate) {
		this.inspectAssignedDate = inspectAssignedDate;
	}




	public String toString(){

		return number+","+type+","+state+","+customerName+","+cleanerId+","+evaluatorId+","+checkinTime+","+checkoutTime.toString();
	}

}
