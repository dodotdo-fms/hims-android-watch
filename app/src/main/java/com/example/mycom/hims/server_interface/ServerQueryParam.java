package com.example.mycom.hims.server_interface;

import org.json.JSONObject;

/**
 * Created by corekey on 10/11/15.
 */
public class ServerQueryParam {

    static final int QUERY_TYPE_GET = 1;
    static final int QUERY_TYPE_PUT = 2;
    static final int QUERY_TYPE_POST = 3;
    static final int QUERY_TYPE_DELETE = 4;

    int queryType = 0;
    String path;
    JSONObject body;
    String token;

    public ServerQueryParam(String path, String token, int queryType) {
        this.queryType = queryType;
        this.path = path;
        this.token = token;
    }

    public ServerQueryParam(String path, JSONObject body, String token, int queryType) {
        this.queryType = queryType;
        this.path = path;
        this.body = body;
        this.token = token;
    }

    public String getPath() {
        return this.path;
    }

    public JSONObject getBody() {
        if (this.queryType == QUERY_TYPE_POST) {
            return this.body;
        }
        return null;
    }

    public String getToken() {
        return this.token;
    }

    public int getQueryType() {
        return this.queryType;
    }
}