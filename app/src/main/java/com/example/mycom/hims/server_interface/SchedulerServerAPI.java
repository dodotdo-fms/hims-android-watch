package com.example.mycom.hims.server_interface;

import java.io.InputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.model.Evaluation;
import com.google.gson.Gson;

/**
 * Created by corekey on 10/5/15.
 *
 * ** How to user Asynchronous API
 * 	1) make the API caller's class implement OnAsyncTaskCompleted
 * 	2) override 'public void onAsyncTaskCompleted(InputStream inputStream)' in the API caller's class
 * 	3) when calling the API, pass the last parameter with the class object
 *
 * 	By doing so, when API is completed, onAsyncTaskCompleted() method will be executed with InputStream
 * 	argument, which is the json response from the server.
 *
 */
public class SchedulerServerAPI {
	
	private static Gson gson = new Gson();

	public static InputStream login(String id, String password) {
		String path = QueryHimsServer.BASIC_PATH + "/users/login";
		JSONObject request = new JSONObject();
		InputStream response = null;

		try {
			if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(password)) {
				request.put("id", id);
				request.put("pw", password);
				response = QueryHimsServer.makePostRequest(path, request);
			}
		} catch (JSONException e) {
			Log.e(SchedulerServerAPI.class.getSimpleName(), "JSONException: " + e.getMessage());
		}

		return response;
	}

	public static void loginAsync(String id, String password, OnAsyncTaskCompleted callbackTask) {
    	String path = QueryHimsServer.BASIC_PATH + "/users/login";
    	JSONObject request = new JSONObject();
    	
    	try {
    		if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(password)) {
    			request.put("id", id);
            	request.put("pw", password);
            	QueryHimsServer.makePostRequest(path, request, callbackTask);
    		}
    	} catch (JSONException e) {
    		Log.e(SchedulerServerAPI.class.getSimpleName(), "JSONException: " + e.getMessage());
    	}
    }

	public static InputStream logout() {
		String path = QueryHimsServer.BASIC_PATH + "/users/logout";
		return QueryHimsServer.makeGetRequest(path);
	}

    public static void logoutAsync(OnAsyncTaskCompleted callbackTask) {
    	String path = QueryHimsServer.BASIC_PATH + "/users/logout";
    	QueryHimsServer.makeGetRequest(path, callbackTask);
    }

	public static InputStream getUsers(String name, String team, String room, String role) {
		Uri.Builder builder = new Uri.Builder();
		builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
				.authority(QueryHimsServer.HIMS_SERVER_HOST)
				.appendPath("api")
				.appendPath("users");
		appendQueryParameter(builder, "name", name);
		appendQueryParameter(builder, "team", team);
		appendQueryParameter(builder, "room_num", room);
		appendQueryParameter(builder, "position", role);
		return QueryHimsServer.makeGetRequest(builder.build().toString());

	}

    public static void getUsersAsync(String name, String team, String room, String role,
                                            OnAsyncTaskCompleted callbackTask) {
        Uri.Builder builder = new Uri.Builder();
    	builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
    		.authority(QueryHimsServer.HIMS_SERVER_HOST)
    		.appendPath("api")
    		.appendPath("users");
    	appendQueryParameter(builder, "name", name);
    	appendQueryParameter(builder, "team", team);
    	appendQueryParameter(builder, "room_num", room);
    	appendQueryParameter(builder, "position", role);
		QueryHimsServer.makeGetRequest(builder.build().toString(), callbackTask);
    }

	public static InputStream getMyInfo() {
		String path = QueryHimsServer.BASIC_PATH + "/users/me";
		return QueryHimsServer.makeGetRequest(path);
	}

    public static void getMyInfoAsync(OnAsyncTaskCompleted callbackTask) {
    	String path = QueryHimsServer.BASIC_PATH + "/users/me";
    	QueryHimsServer.makeGetRequest(path, callbackTask);
    }

	public static InputStream getRooms(String number, String state, String cleanerId, String supervisorId) {
		Uri.Builder builder = new Uri.Builder();
		builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
				.authority(QueryHimsServer.HIMS_SERVER_HOST)
				.appendPath("api")
				.appendPath("rooms");
		appendQueryParameter(builder, "room_num", number);
		appendQueryParameter(builder, "state", state);
		appendQueryParameter(builder, "cleaner_id", cleanerId);
		appendQueryParameter(builder, "supervisor_id", supervisorId);
		return QueryHimsServer.makeGetRequest(builder.build().toString());
	}

    public static void getRoomsAsync(String number, String state, String cleanerId, String supervisorId,
                                            OnAsyncTaskCompleted callbackTask) {
    	Uri.Builder builder = new Uri.Builder();
    	builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
    		.authority(QueryHimsServer.HIMS_SERVER_HOST)
    		.appendPath("api")
    		.appendPath("rooms");
    	appendQueryParameter(builder, "room_num", number);
    	appendQueryParameter(builder, "state", state);
    	appendQueryParameter(builder, "cleaner_id", cleanerId);
    	appendQueryParameter(builder, "supervisor_id", supervisorId);
    	QueryHimsServer.makeGetRequest(builder.build().toString(), callbackTask);
    }

	public static InputStream getForms(String type, String number, String desc, String point) {
		Uri.Builder builder = new Uri.Builder();
		builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
				.authority(QueryHimsServer.HIMS_SERVER_HOST)
				.appendPath("api")
				.appendPath("form");
		appendQueryParameter(builder, "form_type", type);
		appendQueryParameter(builder, "form_num", number);
		appendQueryParameter(builder, "description", desc);
		appendQueryParameter(builder, "total_point", point);
		return QueryHimsServer.makeGetRequest(builder.build().toString());
	}

    public static void getFormsAsync(String type, String number, String desc, String point,
                                            OnAsyncTaskCompleted callbackTask) {
    	Uri.Builder builder = new Uri.Builder();
		builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
			.authority(QueryHimsServer.HIMS_SERVER_HOST)
			.appendPath("api")
			.appendPath("form");
		appendQueryParameter(builder, "form_type", type);
		appendQueryParameter(builder, "form_num", number);
		appendQueryParameter(builder, "description", desc);
		appendQueryParameter(builder, "total_point", point);
		QueryHimsServer.makeGetRequest(builder.build().toString(), callbackTask);
    }

	public static InputStream getFormById(String id) {
		InputStream response = null;

		if (!TextUtils.isEmpty(id)) {
			Uri.Builder builder = new Uri.Builder();
			builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
					.authority(QueryHimsServer.HIMS_SERVER_HOST)
					.appendPath("api")
					.appendPath("form")
					.appendPath(id);
			response = QueryHimsServer.makeGetRequest(builder.build().toString());
		}

		return response;
	}

    public static void getFormByIdAsync(String id, OnAsyncTaskCompleted callbackTask) {
    	if (!TextUtils.isEmpty(id)) {
    		Uri.Builder builder = new Uri.Builder();
    		builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
    			.authority(QueryHimsServer.HIMS_SERVER_HOST)
    			.appendPath("api")
    			.appendPath("form")
    			.appendPath(id);
    		QueryHimsServer.makeGetRequest(builder.build().toString(), callbackTask);
    	}
    }

	public static InputStream getEvaluation(String cleaner, String evaluator, String desc) {
		Uri.Builder builder = new Uri.Builder();
		builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
				.authority(QueryHimsServer.HIMS_SERVER_HOST)
				.appendPath("api")
				.appendPath("evaluation");
		appendQueryParameter(builder, "cleaner_id", cleaner);
		appendQueryParameter(builder, "evaluator_id", evaluator);
		appendQueryParameter(builder, "description", desc);
		return QueryHimsServer.makeGetRequest(builder.build().toString());
	}

    public static void getEvaluationAsync(String cleaner, String evaluator, String desc,
                                                OnAsyncTaskCompleted callbackTask) {
    	Uri.Builder builder = new Uri.Builder();
		builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
			.authority(QueryHimsServer.HIMS_SERVER_HOST)
			.appendPath("api")
			.appendPath("evaluation");
		appendQueryParameter(builder, "cleaner_id", cleaner);
		appendQueryParameter(builder, "evaluator_id", evaluator);
		appendQueryParameter(builder, "description", desc);
		QueryHimsServer.makeGetRequest(builder.build().toString(), callbackTask);
    }

	public static InputStream sendEvaluation(String room, String type, List<Evaluation> evaluations) {
		JSONObject request = new JSONObject();
		InputStream response = null;

		if (!TextUtils.isEmpty(room) && !TextUtils.isEmpty(type) && evaluations != null && evaluations.size() > 0) {
			Uri.Builder builder = new Uri.Builder();
			builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
					.authority(QueryHimsServer.HIMS_SERVER_HOST)
					.appendPath("api")
					.appendPath("evaluation");

			try {
				JSONObject dict = new JSONObject();
				request.put("room_num", room);
				request.put("type", type);
				request.put("evaluation_form_dict", dict);
				for (Evaluation evaluation : evaluations) {
					dict.put(String.valueOf(evaluation.getId()), evaluation.isCheck());
				}
				response = QueryHimsServer.makePostRequest(builder.build().toString(), request);
			} catch (JSONException e) {
				Log.e(SchedulerServerAPI.class.getSimpleName(), "JSONException: " + e.getMessage());
			}
		}

		return response;
	}

    public static void sendEvaluationAsync(String room, String type, List<Evaluation> evaluations,
                                                  OnAsyncTaskCompleted callbackTask) {
        JSONObject request = new JSONObject();
        
        if (!TextUtils.isEmpty(room) && !TextUtils.isEmpty(type) && evaluations != null && evaluations.size() > 0) {
        	Uri.Builder builder = new Uri.Builder();
            builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
            	.authority(QueryHimsServer.HIMS_SERVER_HOST)
            	.appendPath("api")
            	.appendPath("evaluation");
            
            try {
            	JSONObject dict = new JSONObject();
            	request.put("room_num", room);
            	request.put("type", type);
            	request.put("inspection_form_dict", dict);
	            for (Evaluation evaluation : evaluations) {
	            	JSONObject eval = new JSONObject();
	            	eval.put("check", evaluation.isCheck());
	            	dict.put(String.valueOf(evaluation.getId()), eval);
	            }
	            QueryHimsServer.makePostRequest(builder.build().toString(),
                        request, callbackTask);
            } catch (JSONException e) {
            	Log.e(SchedulerServerAPI.class.getSimpleName(), "JSONException: " + e.getMessage());
            }
        }
    }
    
    public static InputStream postClean(String room, String state) {
    	JSONObject request = new JSONObject();
    	InputStream response = null;
    	
    	if (!TextUtils.isEmpty(room) && !TextUtils.isEmpty(state)) {
    		Uri.Builder builder = new Uri.Builder();
			builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
					.authority(QueryHimsServer.HIMS_SERVER_HOST)
					.appendPath("api")
					.appendPath("clean");

			try {
				request.put("room_num", room);
				request.put("state", state);
				response = QueryHimsServer.makePostRequest(builder.build().toString(), request);
			} catch (JSONException e) {
				Log.e(SchedulerServerAPI.class.getSimpleName(), "JSONException: " + e.getMessage());
			}
    	}
    	
    	return response;
    }
    
    public static void postCleanAsync(String room, String state, OnAsyncTaskCompleted callbackTask) {
    	JSONObject request = new JSONObject();
        
        if (!TextUtils.isEmpty(room) && !TextUtils.isEmpty(state)) {
        	Uri.Builder builder = new Uri.Builder();
            builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
            	.authority(QueryHimsServer.HIMS_SERVER_HOST)
            	.appendPath("api")
            	.appendPath("clean");
            
            try {
            	request.put("room_num", room);
            	request.put("state", state);
	            QueryHimsServer.makePostRequest(builder.build().toString(), request, callbackTask);
            } catch (JSONException e) {
            	Log.e(SchedulerServerAPI.class.getSimpleName(), "JSONException: " + e.getMessage());
            }
        }
    }
    
    public static InputStream postRequirement(String room, String requirement) {
    	JSONObject request = new JSONObject();
    	InputStream response = null;
    	
    	if (!TextUtils.isEmpty(room) && !TextUtils.isEmpty(requirement)) {
    		Uri.Builder builder = new Uri.Builder();
			builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
					.authority(QueryHimsServer.HIMS_SERVER_HOST)
					.appendPath("api")
					.appendPath("requirements")
					.appendPath(room);

			try {
				request.put("requirement", requirement);
				response = QueryHimsServer.makePostRequest(builder.build().toString(), request);
			} catch (JSONException e) {
				Log.e(SchedulerServerAPI.class.getSimpleName(), "JSONException: " + e.getMessage());
			}
    	}
    	
    	return response;
    }
    
    private static void appendQueryParameter(Uri.Builder builder, String key, String value) {
    	if (builder != null && !TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
    		builder.appendQueryParameter(key, value);
    	}
    }
    
}
