package com.example.mycom.hims.server_interface;

import com.example.mycom.hims.model.Channel;
import com.squareup.okhttp.Call;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Omjoon on 2015. 12. 4..
 */
public interface RetrofitAPIService {
    @POST("/walkie/channel/{memberId}/msg")
    retrofit.Call listChanels(@Path("memberId") String user);
}
