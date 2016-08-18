package com.example.su.zhihuribao.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by su on 16/5/13.
 */
public class StarDBHelper extends SQLiteOpenHelper{
    public static String TAG="StarDBHelper";

    public static final String CREATE_STAR="create table STAR("
            +"id text primary key,"
            +"url text,"
            +"image text,"
            +"title text)";

    Context mContext;

    public StarDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STAR);
        Log.w(TAG,"create db succeeded");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
