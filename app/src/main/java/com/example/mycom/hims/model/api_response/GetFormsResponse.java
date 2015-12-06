package com.example.mycom.hims.model.api_response;

import java.util.List;

import com.example.mycom.hims.model.Form;
import com.google.gson.annotations.SerializedName;

public class GetFormsResponse {

	@SerializedName("result")
	private List<Form> forms;

	public List<Form> getForms() {
		return forms;
	}

	public void setForms(List<Form> forms) {
		this.forms = forms;
	}
	
}
