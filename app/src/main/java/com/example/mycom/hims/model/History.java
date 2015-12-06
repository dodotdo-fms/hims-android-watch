package com.example.mycom.hims.model;

/**
 * Created by Omjoon on 2015. 12. 2..
 */
public class History {

    String id;
    String filePath;

    public History(String id,String filePath){
        this.id = id;
        this.filePath = filePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
