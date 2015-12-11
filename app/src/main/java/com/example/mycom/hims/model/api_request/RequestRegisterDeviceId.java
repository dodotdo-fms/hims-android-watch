package com.example.mycom.hims.model.api_request;

/**
 * Created by Omjoon on 2015. 12. 11..
 */
public class RequestRegisterDeviceId {
    String device_type;
    String register_id;

    public RequestRegisterDeviceId(String device_type,String register_id){
        this.device_type = device_type;
        this.register_id = register_id;
    }

}
