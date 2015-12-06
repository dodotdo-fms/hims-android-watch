package com.example.mycom.hims.server_interface;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by corekey on 15. 10. 16..
 */
public class QueryTask {

    public QueryTask() {
        // none
    }

    public static InputStream deleteRequest(String path, String token) {
        InputStream inputStream = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpDelete httpDelete = new HttpDelete(path.replace(" ", "%20"));
        httpDelete.setHeader("Content-Type", "application/json");
        httpDelete.setHeader("Authorization", "Basic " + token);
        HttpResponse httpResponse;

        try {
            httpResponse = httpClient.execute(httpDelete);

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
            }
            else {
                Log.e(QueryTask.class.getSimpleName(), "unexpected status code! (" + statusCode + ")");
            }
        }
        catch (ClientProtocolException cpe) {
            Log.e(QueryTask.class.getSimpleName(), "client protocol exception:" + cpe.getMessage().toString());
        }
        catch (IOException ioe) {
            Log.e(QueryTask.class.getSimpleName(), "io exception: " + ioe.getMessage().toString());
        }

        return inputStream;
    }

    public static InputStream getRequest(String path, String token) {
        InputStream inputStream = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(path.replace(" ", "%20"));
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Authorization", "Basic " + token);
        HttpResponse httpResponse;

        try {
            httpResponse = httpClient.execute(httpGet);

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
            }
            else {
                Log.e(QueryTask.class.getSimpleName(), "unexpected status code! (" + statusCode + ")");
            }
        }
        catch (ClientProtocolException cpe) {
            Log.e(QueryTask.class.getSimpleName(), "client protocol exception:" + cpe.getMessage().toString());
        }
        catch (IOException ioe) {
            Log.e(QueryTask.class.getSimpleName(), "io exception: " + ioe.getMessage().toString());
        }

        return inputStream;
    }

    public static InputStream postRequest(String path, JSONObject body, String token) {
        InputStream inputStream = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(path.replace(" ", "%20"));
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Basic " + token);
        HttpResponse httpResponse;

        try {
            httpPost.setEntity(new StringEntity(body.toString(), "UTF8"));
            httpResponse = httpClient.execute(httpPost);

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
            }
            else {
                Log.e(QueryTask.class.getSimpleName(), "unexpected status code! (" + statusCode + ")");
            }
        }
        catch (ClientProtocolException cpe) {
            Log.e(QueryTask.class.getSimpleName(), "client protocol exception:" + cpe.getMessage().toString());
        }
        catch (UnsupportedEncodingException uee) {
            Log.e(QueryTask.class.getSimpleName(), "unsupported encoding: " + uee.getMessage().toString());
        }
        catch (IOException ioe) {
            Log.e(QueryTask.class.getSimpleName(), "io exception: " + ioe.getMessage().toString());
        }

        return inputStream;
    }
}
