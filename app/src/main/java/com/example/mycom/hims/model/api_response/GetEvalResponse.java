package com.example.mycom.hims.model.api_response;

import java.util.List;

import com.example.mycom.hims.model.Evaluation;

public class GetEvalResponse {

	private List<Evaluation> evaluations;

	public List<Evaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<Evaluation> evaluations) {
		this.evaluations = evaluations;
	}
	
}
