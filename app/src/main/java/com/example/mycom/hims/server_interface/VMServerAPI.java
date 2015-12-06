package com.example.mycom.hims.server_interface;

import android.util.Base64;
import android.util.Log;

import com.example.mycom.hims.Common.NetDefine;
import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;

import javax.security.auth.callback.Callback;

import retrofit.Retrofit;

/**
 * Created by corekey on 10/5/15.
 */
public class VMServerAPI implements NetDefine{

	private static Gson gson = new Gson();

    public static InputStream getChannelInfo(int channelId) {
        String path = QueryHimsServer.BASIC_PATH + "/walkie/channel/" + String.valueOf(channelId);
        return QueryHimsServer.makeGetRequest(path);
    }

    public static void getChannelInfoAsync(int channelId, OnAsyncTaskCompleted callbackTask) {
        String path = QueryHimsServer.BASIC_PATH + "/walkie/channel/" + String.valueOf(channelId);
    	QueryHimsServer.makeGetRequest(path, callbackTask);
    }

    public static InputStream getChannelInfoByJoinedId(String memberId) {
        String path = QueryHimsServer.BASIC_PATH + "/walkie/channel?joined_member=" + memberId;
        return QueryHimsServer.makeGetRequest(path);
    }

    public static void getChannelInfoByJoinedIdAsync(String memberId, OnAsyncTaskCompleted callbackTask) {
        String path = QueryHimsServer.BASIC_PATH + "/walkie/channel?joined_member=" + memberId;
        QueryHimsServer.makeGetRequest(path, callbackTask);
    }

    public static InputStream getAllChannelInfo() {
        String path = QueryHimsServer.BASIC_PATH + "/walkie/channel";
        return QueryHimsServer.makeGetRequest(path);
    }

    public static void getAllChannelInfoAsync(OnAsyncTaskCompleted callbackTask) {
        String path = QueryHimsServer.BASIC_PATH + "/walkie/channel";
        QueryHimsServer.makeGetRequest(path, callbackTask);
    }

    public static InputStream createChannel(String channelName, List<String> members) {
        String path = QueryHimsServer.BASIC_PATH + "/walkie/channel";
        JSONObject json = new JSONObject();

        try {
            json.put("channel_name", channelName);
            JSONArray membersArr = new JSONArray();
            for (String member : members) {
                membersArr.put(member);
            }
            json.put("members_list", membersArr);
        }
        catch (JSONException je) {
            Log.e(QueryHimsServer.class.getSimpleName(), "json exception in createChannel: " + je.getMessage().toString());
        }

        return QueryHimsServer.makePostRequest(path, json);
    }

    public static void createChannelAsync(String channelName, List<String> members,
                                                    OnAsyncTaskCompleted callbackTask) {
    	String path = QueryHimsServer.BASIC_PATH + "/walkie/channel";
    	JSONObject json = new JSONObject();

        try {            
            json.put("channel_name", channelName);
            JSONArray membersArr = new JSONArray();
            for (String member : members) {
                membersArr.put(member);
            }
            json.put("member_list", membersArr);
        }
        catch (JSONException je) {
            Log.e(QueryHimsServer.class.getSimpleName(), "json exception in createChannel: " + je.getMessage().toString());
        }

        QueryHimsServer.makePostRequest(path, json, callbackTask);
    }

    public static InputStream deleteChannel(int channelId) {
        String path = QueryHimsServer.BASIC_PATH + "/walkie/channel/" + String.valueOf(channelId) + "/delete";
        return QueryHimsServer.makeDeleteRequest(path);
    }

    public static void deleteChannelAsync(int channelId, OnAsyncTaskCompleted callbackTask) {
        String path = QueryHimsServer.BASIC_PATH + "/walkie/channel/" + String.valueOf(channelId) + "/delete";
        QueryHimsServer.makeDeleteRequest(path, callbackTask);
    }

    public static void sendMsgToChannel(int channelId, String msgFilePath,OnAsyncTaskCompleted callback) {
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASIC_PATH).build();
//        RetrofitAPIService service = retrofit.create(RetrofitAPIService.class);
//        service.listChanels()

        String path = QueryHimsServer.BASIC_PATH + "/walkie/channel/" + String.valueOf(channelId) + "/msg";
        JSONObject json = new JSONObject();

        /* read msg file and convert to json payload */
        File file = new File(msgFilePath);
        byte[] bytes = null;

        RandomAccessFile f = null;
        try {
            f = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            // Get and check length
            if(f!=null) {
                long longLength = f.length();
                int length = (int) longLength;
                if (length != longLength)
                    throw new IOException("File size >= 2 GB");
                // Read file and return data
                bytes = new byte[length];
                f.readFully(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(f!=null) {
                    f.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            String encodeBase64String = Base64.encodeToString(bytes, Base64.DEFAULT);
            json.put("msg", encodeBase64String);
        }
        catch (JSONException je) {
            Log.e(QueryHimsServer.class.getSimpleName(), "json exception in sendMsgToChannel: " +
                    je.getMessage().toString());
        }

         QueryHimsServer.makePostRequest(path, json,callback);
    }

    public static void sendMsgToChannelAsync(int channelId, String msgFilePath,
                                             OnAsyncTaskCompleted callbackTask) {
        String path = QueryHimsServer.BASIC_PATH + "/walkie/channel/" + String.valueOf(channelId) + "/msg";
        JSONObject json = new JSONObject();

           /* read msg file and convert to json payload */
        File file = new File(msgFilePath);
        byte[] bytes = null;

        RandomAccessFile f = null;
        try {
            f = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            // Get and check length
            long longLength = f.length();
            int length = (int) longLength;
            if (length != longLength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            bytes = new byte[length];
            f.readFully(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            String encodeBase64String = Base64.encodeToString(bytes, Base64.DEFAULT);
            json.put("msg", encodeBase64String);
        }
        catch (JSONException je) {
            Log.e(QueryHimsServer.class.getSimpleName(), "json exception in sendMsgToChannel: " +
                    je.getMessage().toString());
        }

        QueryHimsServer.makePostRequest(path, json, callbackTask);
    }

    public static InputStream getNewMsgFromChannel(int channelId, String latestMsgTime) { /* time format: yyyy-MM- dd HH:mm:ss */
        String path = QueryHimsServer.BASIC_PATH + "/walkie/channel/" + String.valueOf(channelId) +
                      "/msg?last_received=" + latestMsgTime;
        return QueryHimsServer.makeGetRequest(path);
    }

    public static void getNewMsgFromChannelAsync(int channelId, String latestMsgTime, /* time format: yyyy-MM- dd HH:mm:ss */
                                                 OnAsyncTaskCompleted callbackTask) {
        String path = QueryHimsServer.BASIC_PATH + "/walkie/channel/" + String.valueOf(channelId) +
                      "/msg?last_received=" + latestMsgTime;
        QueryHimsServer.makeGetRequest(path, callbackTask);
    }

    public static InputStream getNewMsg(String latestMsgTime, int reqMsgNum) { /* time format: yyyy-MM- dd HH:mm:ss */
        String path = QueryHimsServer.BASIC_PATH + "/walkie/msg?last_received=" + latestMsgTime +
                "&num=" + reqMsgNum;
        return QueryHimsServer.makeGetRequest(path);
    }

    public static void getNewMsg(String latestMsgTime, int reqMsgNum,OnAsyncTaskCompleted callback) { /* time format: yyyy-MM- dd HH:mm:ss */
        String path = null;
        if(latestMsgTime!=null) {
             path = QueryHimsServer.BASIC_PATH + "/walkie/msg?last_received=" + latestMsgTime +
                    "&num=" + reqMsgNum;
        }else{
             path = QueryHimsServer.BASIC_PATH + "/walkie/msg?=" +
                    "num=" + reqMsgNum;
        }
         QueryHimsServer.makeGetRequest(path,callback);
    }

    public static void getNewMsgAsync(String latestMsgTime, int reqMsgNum,
                                 OnAsyncTaskCompleted callbackTask) { /* time format: yyyy-MM- dd HH:mm:ss */
        String path = QueryHimsServer.BASIC_PATH + "/walkie/msg?last_received=" + latestMsgTime +
                "&num=" + reqMsgNum;
        QueryHimsServer.makeGetRequest(path, callbackTask);
    }
}
