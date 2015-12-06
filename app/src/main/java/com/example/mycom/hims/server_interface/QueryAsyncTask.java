package com.example.mycom.hims.server_interface;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mycom.hims.OnAsyncTaskCompleted;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import com.google.gson.Gson;

/**
 * Created by corekey on 10/11/15.
 */
public class QueryAsyncTask extends AsyncTask<ServerQueryParam,Object,InputStream> {
    private OnAsyncTaskCompleted listener;

    private static Gson gson = new Gson();

    public QueryAsyncTask(OnAsyncTaskCompleted listener) {
        this.listener = listener;
    }

    private InputStream doDeleteProcess(String path, String token) {
        InputStream inputStream = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpDelete httpDelete = new HttpDelete(path.replace(" ", "%20"));
        httpDelete.setHeader("Content-Type", "application/json");
        httpDelete.setHeader("Authorization", "Basic " + token);
        HttpResponse httpResponse;
        Log.e("path",path);

        try {
            httpResponse = httpClient.execute(httpDelete);

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
            }
            else {
                Log.e(QueryHimsServer.class.getSimpleName(), "unexpected status code! (" + statusCode + ")");
            }
        }
        catch (ClientProtocolException cpe) {
            Log.e(this.getClass().getSimpleName(), "client protocol exception:" + cpe.getMessage().toString());
        }
        catch (IOException ioe) {
            Log.e(this.getClass().getSimpleName(), "io exception: " + ioe.getMessage().toString());
        }
        return inputStream;
    }

    private InputStream doPostProcess(String path, JSONObject body, String token) {
        InputStream inputStream = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(path.replace(" ", "%20"));
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Basic " + token);
        HttpResponse httpResponse;
       Log.e("path",path);
        try {
            httpPost.setEntity(new StringEntity(body.toString(), "UTF8"));
            httpResponse = httpClient.execute(httpPost);

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
            }
            else {
                Log.e(QueryHimsServer.class.getSimpleName(), "unexpected status code! (" + statusCode + ")");
            }
        }
        catch (ClientProtocolException cpe) {
            Log.e(this.getClass().getSimpleName(), "client protocol exception:" + cpe.getMessage().toString());
        }
        catch (UnsupportedEncodingException uee) {
            Log.e(this.getClass().getSimpleName(), "unsupported encoding: " + uee.getMessage().toString());
        }
        catch (IOException ioe) {
            Log.e(this.getClass().getSimpleName(), "io exception: " + ioe.getMessage().toString());
        }
        return inputStream;
    }

    private InputStream doGetProcess(String path, String token) {
        InputStream inputStream = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(path.replace(" ", "%20"));
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Authorization", "Basic " + token);
        HttpResponse httpResponse;
        Log.e("path",path);
        try {
            httpResponse = httpClient.execute(httpGet);

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = httpResponse.getEntity();
                inputStream = entity.getContent();
            }
            else {
                Log.e(QueryHimsServer.class.getSimpleName(), "unexpected status code! (" + statusCode + ")");
            }
        }
        catch (ClientProtocolException cpe) {
            Log.e(this.getClass().getSimpleName(), "client protocol exception:" + cpe.getMessage().toString());
        }
        catch (IOException ioe) {
            Log.e(this.getClass().getSimpleName(), "io exception: " + ioe.getMessage().toString());
        }
        InputStream i2 = inputStream;

        return inputStream;
    }


    @Override
    protected void onPreExecute() {
        // Runs on the UI thread before doInBackground()
    }

    @Override
    protected InputStream doInBackground(ServerQueryParam... params) {
        InputStream inputStream = null;

        ServerQueryParam param = params[0];
        switch (param.queryType) {
            case ServerQueryParam.QUERY_TYPE_POST:
                inputStream = doPostProcess(param.getPath(), param.getBody(), param.getToken());
                break;
            case ServerQueryParam.QUERY_TYPE_DELETE:
                inputStream = doDeleteProcess(param.getPath(), param.getToken());
                break;
            case ServerQueryParam.QUERY_TYPE_GET:
                inputStream = doGetProcess(param.getPath(), param.getToken());
                break;
            case ServerQueryParam.QUERY_TYPE_PUT:
                /* not defined yet */
                break;
        }

        return inputStream;
    }

    @Override
    protected void onProgressUpdate(Object... progress) {
        // Runs on UI thread after publishProgress(Progress...) is invoked
        // from doInBackground()
    }

    @Override
    protected void onPostExecute(InputStream inputStream) {
        if (this.listener != null) {
            this.listener.onAsyncTaskCompleted(inputStream);   // execute callback function
        }
    }

    @Override
    protected void onCancelled(InputStream inputStream) {
        // Runs on UI thread after cancel() is invoked
        // and doInBackground() has finished/returned
    }


}
