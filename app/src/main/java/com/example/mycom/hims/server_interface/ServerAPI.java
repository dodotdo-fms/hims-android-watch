package com.example.mycom.hims.server_interface;

import com.example.mycom.hims.model.api_request.RequestLogin;
import com.example.mycom.hims.model.api_request.RequestPostClean;
import com.example.mycom.hims.model.api_response.GetChannelResponse;
import com.example.mycom.hims.model.api_response.GetMessageResponse;
import com.example.mycom.hims.model.api_response.GetRoomsResponse;
import com.example.mycom.hims.model.api_response.GetUsersResponse;
import com.example.mycom.hims.model.api_response.LoginResponse;
import com.example.mycom.hims.model.api_response.LogoutResponse;
import com.example.mycom.hims.model.api_response.PostCleanResponse;

import retrofit.Call;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Url;

/**
 * Created by Omjoon on 2015. 12. 7..
 */
public interface ServerAPI {
    @GET("/api/users")
    Call<GetUsersResponse> getUsers();

    @POST("/api/users/login")
    Call<LoginResponse> goLogin(@Body RequestLogin requestLogin);

    @GET("/api/users/logout")
    Call<LogoutResponse> goLogout();

    @GET
    Call<GetRoomsResponse> getRooms(@Url String url);

    @POST("/api/clean")
    Call<PostCleanResponse> postClean(@Body RequestPostClean requestPostClean);

    @GET
    Call<GetChannelResponse> getChannel(@Url String path);

    @GET
    Call<GetMessageResponse> getMessage(@Url String path);

}
