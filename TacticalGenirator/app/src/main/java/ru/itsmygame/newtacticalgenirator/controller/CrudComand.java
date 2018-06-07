package ru.itsmygame.newtacticalgenirator.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import java.util.List;

/**
 * Created by llobachev on 29.06.2017.
 */

public interface CrudComand {
    public Cursor getAllData(SQLiteDatabase mDB, String s);

    public void delRec(SQLiteDatabase mDB, String tableName, String columName, long id);

    public void delRec(SQLiteDatabase mDB, String tableName, String columName) ;

    public ContentValues addRec(int id, String name, String discription, int oid);

    public void addRecTJ(SQLiteDatabase mDB,int id, String name, String discription, int oid);

    public void addRecT(SQLiteDatabase mDB,int id, String name, String discription, int oid);

    public void addRecToComplite(SQLiteDatabase mDB,int id, String name, String discription, int oid, Cursor cursor);



}
