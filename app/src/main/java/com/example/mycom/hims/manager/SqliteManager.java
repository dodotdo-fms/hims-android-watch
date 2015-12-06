package com.example.mycom.hims.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mycom.hims.model.Channel;
import com.example.mycom.hims.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Omjoon on 2015. 11. 27..
 */
public class SqliteManager extends SQLiteOpenHelper {
    protected final static String TAG="DB";
    protected final static String DATABASE_NAME = "HIMS_DB";
    protected final static int DATABASE_VERSION = 1;

    protected final static String TABLE_USER ="user";
    protected final static String TABLE_CHANEL ="chanel";

    protected final static String CONTETN_ID = "id";
    protected final static String CONTETN_POSITION = "position";
    protected final static String CONTETN_TEAM = "team";
    protected final static String CONTETN_NAME = "name";

    protected final static String CONTETN_HOST_ID = "hostId";
    protected final static String CONTETN_MEMBERS = "members";



    Context mContext;
    public SqliteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;



    }


        @Override
    public void onCreate(SQLiteDatabase db) {

            String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" +
                    CONTETN_ID + " TEXT," +
                    CONTETN_POSITION + " TEXT," +
                    CONTETN_NAME + " TEXT," +
                    CONTETN_TEAM + " Text" +
                    ")";
            db.execSQL(CREATE_USER_TABLE);


            String CREATE_CHANEL_TABLE = "CREATE TABLE " + TABLE_CHANEL + "(" +
                    CONTETN_ID + " TEXT," +
                    CONTETN_HOST_ID + " TEXT," +
                    CONTETN_NAME + " TEXT," +
                    CONTETN_MEMBERS + " Text" +
                    ")";
            db.execSQL(CREATE_CHANEL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void onInsertUsers(ArrayList<User> users){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            for(User user: users) {
                ContentValues values = new ContentValues();
                values.put(CONTETN_ID, user.getId());
                values.put(CONTETN_POSITION, user.getPosition());
                values.put(CONTETN_NAME, user.getName());
                values.put(CONTETN_TEAM, user.getTeam());


                long id = db.insertWithOnConflict(TABLE_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                if (id == -1) {
                    String condition = " id = " + user.getId();
                    db.update(TABLE_USER, values, condition, null);
                    Log.i(TAG, String.format("update user id : %d", user.getId()));
                } else {
                    Log.i(TAG, String.format("insert user id : %d", user.getId()));
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void onInsertChaners(ArrayList<Channel> chanels){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            for(Channel chanel: chanels) {
                ContentValues values = new ContentValues();
                values.put(CONTETN_ID, chanel.getId());
                values.put(CONTETN_HOST_ID, chanel.getHostId());
                values.put(CONTETN_NAME, chanel.getName());
                values.put(CONTETN_MEMBERS, chanel.getMembers().toString());


                long id = db.insertWithOnConflict(TABLE_CHANEL, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                if (id == -1) {
                    String condition = " id = " + chanel.getId();
                    db.update(TABLE_CHANEL, values, condition, null);
                    Log.i(TAG, String.format("update chanel id : %d", chanel.getId()));
                } else {
                    Log.i(TAG, String.format("insert chanel id : %d", chanel.getId()));
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
            db.close();
        }
    }



}
