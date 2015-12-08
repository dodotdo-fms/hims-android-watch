package com.example.mycom.hims.data;

import com.example.mycom.hims.model.History;
import com.example.mycom.hims.model.VoiceMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Omjoon on 2015. 12. 2..
 */
public class Messages {

    static Messages instance;
    List<VoiceMessage> lists;
    HashMap<String,VoiceMessage> meesageHashmap;
    public static Messages getInstance(){
        if(instance ==  null){
            instance = new Messages();
        }

        return instance;
    }

    protected Messages(){
        lists = new ArrayList<>();
        meesageHashmap = new HashMap<>();
    }


    public List<VoiceMessage> getLists() {
        return lists;
    }

    public void setLists(List<VoiceMessage> lists) {
        this.lists = lists;
    }

    public HashMap<String, VoiceMessage> getMeesageHashmap() {
        return meesageHashmap;
    }

    public void setMeesageHashmap(HashMap<String, VoiceMessage> meesageHashmap) {
        this.meesageHashmap = meesageHashmap;
    }

    public VoiceMessage getHistroy(String id){
        return meesageHashmap.get(id);
    }

    public void clean(){
        meesageHashmap.clear();
        lists.clear();;
    }
}
