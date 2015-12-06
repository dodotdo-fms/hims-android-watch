package com.example.mycom.hims.server_interface;

import org.json.JSONObject;

import com.example.mycom.hims.Common.NetDefine;
import com.example.mycom.hims.OnAsyncTaskCompleted;

import java.io.InputStream;


/**
 * Created by corekey on 10/5/15.
 */
public class QueryHimsServer implements NetDefine{


    private static String token = "1";
    
    public static void setToken(String newToken) {
    	token = newToken;
    }

    public static InputStream makeDeleteRequest(String path) {
        return QueryTask.deleteRequest(path, token);
    }

    public static InputStream makeGetRequest(String path) {
        return QueryTask.getRequest(path, token);
    }

    public static InputStream makePostRequest(String path, JSONObject body) {
        return QueryTask.postRequest(path, body, token);
    }

    public static void makeDeleteRequest(String path, OnAsyncTaskCompleted callbackTask) { /* ASYNC */
        ServerQueryParam serverQueryParam = new ServerQueryParam(path, token, ServerQueryParam.QUERY_TYPE_DELETE);

        QueryAsyncTask queryAsyncTask = new QueryAsyncTask(callbackTask);
        queryAsyncTask.execute(serverQueryParam);
    }

    public static void makeGetRequest(String path, OnAsyncTaskCompleted callbackTask) { /* ASYNC */
        ServerQueryParam serverGetParam = new ServerQueryParam(path, token, ServerQueryParam.QUERY_TYPE_GET);

        QueryAsyncTask queryAsyncTask = new QueryAsyncTask(callbackTask);
        queryAsyncTask.execute(serverGetParam);

    }

    public static void makePostRequest(String path, JSONObject body,
                                       OnAsyncTaskCompleted callbackTask) { /* ASYNC */
        ServerQueryParam serverPostParam = new ServerQueryParam(path, body, token, ServerQueryParam.QUERY_TYPE_POST);

        QueryAsyncTask queryAsyncTask = new QueryAsyncTask(callbackTask);
        queryAsyncTask.execute(serverPostParam);
    }

}
