package com.example.mycom.hims.server_interface;

import android.net.Uri;
import android.text.TextUtils;

import com.example.mycom.hims.model.api_request.RequestLogin;
import com.example.mycom.hims.model.api_request.RequestPostClean;
import com.example.mycom.hims.model.api_response.GetChannelResponse;
import com.example.mycom.hims.model.api_response.GetMessageResponse;
import com.example.mycom.hims.model.api_response.GetRoomsResponse;
import com.example.mycom.hims.model.api_response.GetUsersResponse;
import com.example.mycom.hims.model.api_response.LoginResponse;
import com.example.mycom.hims.model.api_response.LogoutResponse;
import com.example.mycom.hims.model.api_response.PostCleanResponse;

import java.io.IOException;

import javax.security.auth.callback.Callback;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Omjoon on 2015. 12. 7..
 */
public class ServerQuery {
    public static void getUsers(retrofit.Callback callback){

        Call<GetUsersResponse> call = ServiceGenerator.createService(ServerAPI.class).getUsers();
        call.enqueue(callback);
    }

    public static void goLogin(String myid,String pw,retrofit.Callback callback){
        Call<LoginResponse> call = ServiceGenerator.createService(ServerAPI.class).goLogin(new RequestLogin(myid, pw));
        call.enqueue(callback);

    }

    public static void goLogout(retrofit.Callback callback){
        Call<LogoutResponse> call = ServiceGenerator.createService(ServerAPI.class).goLogout();
           call.enqueue(callback);


    }

    public static void getRooms(String state, String cleanerId, String supervisoroId, String number, String floor, retrofit.Callback callback){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
                .appendPath("api")
                .appendPath("rooms");
        appendQueryParameter(builder, "room_num", number);
        appendQueryParameter(builder, "state", state);
        appendQueryParameter(builder, "cleaner_id", cleanerId);
        appendQueryParameter(builder, "supervisor_id", supervisoroId);
        appendQueryParameter(builder, "floor", floor);
        Call<GetRoomsResponse> call = ServiceGenerator.createService(ServerAPI.class).getRooms(builder.build().toString());
        call.enqueue(callback);

    }

    public static void postClean(String room_num,String state, retrofit.Callback callback){
        Call<PostCleanResponse> call = ServiceGenerator.createService(ServerAPI.class).postClean(new RequestPostClean(room_num, state));
        call.enqueue(callback);
    }

    public static void getChannel(String channelName,String joinedMember, retrofit.Callback callback){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(QueryHimsServer.HIMS_SERVER_PROTOCOL)
                .appendPath("api")
                .appendPath("walkie")
                .appendPath("channel");
        appendQueryParameter(builder, "channel_name", channelName);
        appendQueryParameter(builder, "joined_member", joinedMember);
        Call<GetChannelResponse> call = ServiceGenerator.createService(ServerAPI.class).getChannel(builder.build().toString());
        call.enqueue(callback);
    }


    public static void getMessage(String latestMsgTime,int reqMsgNum,retrofit.Callback callback){
        String path = null;
        if(latestMsgTime!=null) {
            path = QueryHimsServer.BASIC_PATH + "/walkie/msg?last_received=" + latestMsgTime +
                    "&num=" + reqMsgNum;
        }else{
            path = QueryHimsServer.BASIC_PATH + "/walkie/msg?=" +
                    "num=" + reqMsgNum;
        }
        Call<GetMessageResponse> call = ServiceGenerator.createService(ServerAPI.class).getMessage(path);
        call.enqueue(callback);
    }


    private static void appendQueryParameter(Uri.Builder builder, String key, String value) {
        if (builder != null && !TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            builder.appendQueryParameter(key, value);
        }
    }

}
