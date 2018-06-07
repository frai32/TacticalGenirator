package ru.itsmygame.newtacticalgenirator.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by llobachev on 28.06.2017.
 */

public class DBModel {

    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "mytab";
    private  static final String DB_JOURNAL = "Journal";
    private  static final String DB_COMP = "Complite";

    private static final String COLUMN_ID  = "_id";
    private static final String COLUMN_NAME = "NameObjective";
    private static final String COLUMN_DS = "Discription";
    private static final String COLUMN_TID = "TactID";


    private static  final String[] COLUMN  = {COLUMN_NAME,COLUMN_ID, COLUMN_TID};

    private static final String DB_CREATE =
            "create table " + DB_TABLE + "( " +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_NAME + " text, " +
                    COLUMN_DS + " text, " +
                    COLUMN_TID + " numeric " +
                    "); " ;

    private static final String DB_CREATE1 =
            "create table " + DB_JOURNAL + "( " +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_NAME + " text, " +
                    COLUMN_DS + " text, " +
                    COLUMN_TID + " numeric " +
                    "); " ;

    private static final String DB_CREATE2 =
            "create table " + DB_COMP + "( " +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_NAME + " text, " +
                    COLUMN_DS + " text, " +
                    COLUMN_TID + " numeric " +
                    "); " ;

    private final Context mCtx;

    public static String getDbJournal() {
        return DB_JOURNAL;
    }

    public static String getDbComp() {
        return DB_COMP;
    }

    public static String getColumnId() {
        return COLUMN_ID;
    }

    public static String getColumnName() {
        return COLUMN_NAME;
    }

    public static String getColumnDs() {
        return COLUMN_DS;
    }

    public static String getDbTable() {
        return DB_TABLE;
    }

    public static String getColumnTid() {
        return COLUMN_TID;
    }

    public static String[] getCOLUMN() {
        return COLUMN;
    }

    private DBHelper mDBHelper;

    public SQLiteDatabase getmDB() {
        return mDB;
    }

    private SQLiteDatabase mDB;

    public DBModel (Context ctx) {
        mCtx = ctx;
    }

    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            //db.execSQL("DROP TABLE IF EXISTS +" + DB_TABLE);
            db.execSQL(DB_CREATE);
            db.execSQL(DB_CREATE1);
            db.execSQL(DB_CREATE2);


	     /* for (int i = 1; i < 5; i++) {
	        cv.put(COLUMN_TXT, "sometext " + i);
	        cv.put(COLUMN_KP, "0");
	        db.insert(DB_TABLE, null, cv);
	      }*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

}

