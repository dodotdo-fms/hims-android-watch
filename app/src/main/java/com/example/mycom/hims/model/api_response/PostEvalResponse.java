package com.example.mycom.hims.model.api_response;

import com.google.gson.annotations.SerializedName;

public class PostEvalResponse {

	@SerializedName("result")
	private EvalResult result;
	
	public int getPoint() {
		return result.point;
	}

	public void setPoint(int point) {
		result.point = point;
	}

	public int getTotalPoint() {
		return result.totalPoint;
	}

	public void setTotalPoint(int totalPoint) {
		result.totalPoint = totalPoint;
	}
	
	private class EvalResult {
		@SerializedName("received_point")
		public int point;
		
		@SerializedName("total_point")
		public int totalPoint;
	}
	
}
