package com.example.mycom.hims.common;

/**
 * Created by Omjoon on 2015. 12. 4..
 */
public interface NetDefine {
    public static final String HIMS_SERVER_PROTOCOL = "http";
//    public static final String HIMS_SERVER_HOST = "52.8.181.110";
    public static final String HIMS_SERVER_HOST = "52.53.251.182:8080";
    public static final String BASIC_PATH = HIMS_SERVER_PROTOCOL + "://" + HIMS_SERVER_HOST + "/api";
}
