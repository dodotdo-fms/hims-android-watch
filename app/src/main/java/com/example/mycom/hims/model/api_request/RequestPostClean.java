package com.example.mycom.hims.model.api_request;

/**
 * Created by Omjoon on 2015. 12. 7..
 */
public class RequestPostClean {
    String room_num;
    String state;
    public RequestPostClean(String room_num,String state){
        this.room_num = room_num;
        this.state = state;
    }
}
