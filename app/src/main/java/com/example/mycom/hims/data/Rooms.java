package com.example.mycom.hims.data;

import com.example.mycom.hims.model.Room;
import com.example.mycom.hims.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Omjoon on 2015. 11. 28..
 */
public class Rooms {

    static Rooms instance;

    List<Room> lists;
    HashMap<Integer,Room> roomHashMap;

    public static Rooms getInstance(){
        if (instance == null){
            instance = new Rooms();
        }

        return instance;
    }

    private Rooms(){
        lists = new ArrayList<>();
        roomHashMap = new HashMap<>();
    }

    public List<Room> getLists() {
        return lists;
    }

    public void setLists(List<Room> rooms) {
        this.lists = rooms;
        roomHashMap.clear();
        Iterator<Room> iterator = rooms.iterator();
        while (iterator.hasNext()) { // 값이 나올때까지 while문을 돈다
            Room room = iterator.next(); // 문자열 변수 s에 다음값을 넣는다
            roomHashMap.put(room.getNumber(),room);
        }


    }

    public HashMap<Integer, Room> getRoomHashMap() {
        return roomHashMap;
    }

    public void setRoomHashMap(HashMap<Integer, Room> roomHashMap) {
        this.roomHashMap = roomHashMap;
    }

    public Room getRoom(int roomNumber){
        return roomHashMap.get(roomNumber);

    }
}
