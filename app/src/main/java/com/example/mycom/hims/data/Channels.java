package com.example.mycom.hims.data;

import android.util.Log;

import com.example.mycom.hims.model.Channel;
import com.example.mycom.hims.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Omjoon on 2015. 11. 27..
 */
public class Channels {
    static Channels instance;
    List<Channel> channels;
    HashMap<String,Channel> channelHashMap;
    public static Channels getInstance(){
        if(instance == null){
            instance = new Channels();
        }

        return instance;
    }

    private Channels(){
        channelHashMap = new HashMap<>();
        channels = new ArrayList<>();
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
        Iterator<Channel> iterator = channels.iterator();
        while (iterator.hasNext()) { // 값이 나올때까지 while문을 돈다
            Channel channel = iterator.next(); // 문자열 변수 s에 다음값을 넣는다
            Log.e(channel.getName(),channel.getId());
            channelHashMap.put(channel.getId(),channel);
        }

    }

    public HashMap<String, Channel> getChannelHashMap() {
        return channelHashMap;
    }

    public void setChannelHashMap(HashMap<String, Channel> channelHashMap) {
        this.channelHashMap = channelHashMap;
    }

    public Channel getChannel(String id){

        return channelHashMap.get(id);
    }

    public Channel getChannel(int id){

        return channelHashMap.get(String.valueOf(id));
    }

    public void clean(){
        channels.clear();
        channelHashMap.clear();
    }
}
