package com.example.mycom.hims.common;

/**
 * Created by Omjoon on 2015. 11. 26..
 */
public class MyAccount {
    static MyAccount instance;
    String position;
    String id;
    String password;
    String tokenId;
    public static MyAccount getInstance(){
        if(instance == null){
            instance = new MyAccount();
        }

        return instance;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
