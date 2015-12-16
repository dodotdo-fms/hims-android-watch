package com.example.mycom.hims.model;

import java.io.File;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class VoiceMessage {

	@SerializedName("msg_id")
	private int messageId;
	
	@SerializedName("member_id")
	private String memberId;
	
	@SerializedName("timestamp")
	private Date timestamp;
	
	@SerializedName("msg_size")
	private int messageSize;
	
	@SerializedName("msg")
	private String message;
	
	@SerializedName("next_msg_num")
	private int nextMessageNum;

	@SerializedName("duration_time")
	private String durationTime;

	@SerializedName("channel_id")
	private int channelId;

	private String filepath;

	private File file;

	private String senderName;

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getMessageSize() {
		return messageSize;
	}

	public void setMessageSize(int messageSize) {
		this.messageSize = messageSize;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getNextMessageNum() {
		return nextMessageNum;
	}

	public void setNextMessageNum(int nextMessageNum) {
		this.nextMessageNum = nextMessageNum;
	}

	public String getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(String durationTime) {
		this.durationTime = durationTime;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
}
