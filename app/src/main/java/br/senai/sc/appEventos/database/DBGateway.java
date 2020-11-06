package br.senai.sc.appEventos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBGateway {

    private static DBGateway dbGateway;
    private SQLiteDatabase db;

    private DBGateway(Context context){
        DatabaseDBHelper dbHelper = new DatabaseDBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public static DBGateway getInstance(Context context){
        if (dbGateway == null){
            dbGateway = new DBGateway(context);
        }
        return dbGateway;
    }



    public SQLiteDatabase getDatabase(){
        return db;
    }
}
