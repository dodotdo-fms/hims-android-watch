package com.example.mycom.hims.data;

import com.example.mycom.hims.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Omjoon on 2015. 11. 27..
 */
public class Users {

    static Users instance;
    List<User> users;
    HashMap<String,User> userHashMap;

    public static Users getInstance(){
        if (instance == null){
            instance = new Users();
        }

        return instance;
    }

    public Users(){
        users = new ArrayList<>();
        userHashMap = new HashMap<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        userHashMap.clear();
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) { // 값이 나올때까지 while문을 돈다
            User user = iterator.next(); // 문자열 변수 s에 다음값을 넣는다
           userHashMap.put(user.getId(),user);
        }


    }

    public HashMap<String, User> getUserHashMap() {
        return userHashMap;
    }

    public void setUserHashMap(HashMap<String, User> userHashMap) {
        this.userHashMap = userHashMap;
    }

    public User getUser(String id){
        return userHashMap.get(id);
    }

    public boolean isExistMe(String id){

          return userHashMap.containsKey(id);
    }

}
