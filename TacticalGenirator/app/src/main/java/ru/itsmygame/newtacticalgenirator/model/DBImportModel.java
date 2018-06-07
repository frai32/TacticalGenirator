package ru.itsmygame.newtacticalgenirator.model;

import android.content.Context;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by llobachev on 28.06.2017.
 */

public class DBImportModel {

    private final  String LOGTAG = "myLogs";
    private  String DATABASE_NAME = "TacticalObjective.db";
    private final String DATABASE_PATH = "data/data/ru.itsmygame.newtacticalgenirator/databases/";
    private final int DATABASE_VERSION = 3;

    private static final  String[] DB_TABLE = { "TacticalTabel",
                                                 "SpaceMarines",
                                                 "GKObjective",
                                                 "ChaosSpaceMarines",
                                                 "AstraMilitarum",
                                                 "[Cult mechanicum]",
                                                 "DeathGuard",
                                                 "SpaceWolfsObjective",
                                                 "KhornDemonkin",
                                                 "Skitarii",
                                                 "Eldar",
                                                 "HarlequinsObjective"};

    private static final String COLUMN_ID  = "_id";
    private static final String COLUMN_NAME = "NameObjective";

    private static final String COLUMN_DS = "Discription";
    private static final String COLUMN_TID = "TactID";
    private static final String DB_TACT = "TacticalTabel";

    private static  final String[] COLUMN  = {COLUMN_NAME,COLUMN_ID, COLUMN_TID};


    private final Context mCtx;


    private DBHelper mDBHelper;

    public SQLiteDatabase getmDB() {
        return mDB;
    }

    private SQLiteDatabase mDB;

    public DBImportModel(Context ctx) {
        mCtx = ctx;
    }


    public void open() {
        mDBHelper = new DBHelper(mCtx);
        mDB = mDBHelper.getWritableDatabase();
    }


    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    public void Check()
    {
        Log.d ("LOGDBIMPORT", "" + mDBHelper.checkDatabase());
    }

    public void OpenDATABASE()
    {
        mDBHelper.openDataBase();
    }

    public String getLOGTAG() {
        return LOGTAG;
    }

    public static String getDbTact() {
        return DB_TACT;
    }

    public static String[] getDbTable() {

        return DB_TABLE;
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

    public static String getColumnTid() {
        return COLUMN_TID;
    }

    class DBHelper extends SQLiteOpenHelper {

        private SQLiteDatabase myDataBase;
        private Context myContext;

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.myContext = context;

            boolean dbExists = checkDatabase();
            if (dbExists) {
                Log.i(LOGTAG, "DbHelper.constructor: database exists");
            }else {
                try{
                    createDatabase();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


        private boolean checkDatabase() {
            boolean checkdb = false;
            try {
                String myPath = DATABASE_PATH + DATABASE_NAME;
                File dbFile = new File(myPath);
                checkdb = dbFile.exists();

            } catch (SQLiteException e) {
                Log.e(LOGTAG, "checkDatabase(): Database doesn't exist");
            }

            return checkdb;
        }


        private void copyDatabase() throws IOException {

            // Open your local db as the input stream
            InputStream input = myContext.getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            @SuppressWarnings("unused")
            String outFilename = DATABASE_PATH + DATABASE_NAME;

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(
                    outFilename);

            // transfer byte to input file to output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            input.close();

        }

        public void openDataBase() throws SQLException {

            //Копирование
            // базы
            String myPath = DATABASE_PATH + DATABASE_NAME;
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        }

        private void createDatabase() throws IOException {
            boolean dbExists = checkDatabase();
            if (dbExists){
                Log.i(LOGTAG, "createDatabase(): database exists");
            }else {
                this.getReadableDatabase();
                try{
                    copyDatabase();
                } catch (IOException e) {
                    e.printStackTrace();
                   // throw new Error("Error copying database");
                }
            }
        }
        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.i(LOGTAG, "version"+db.getVersion());


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Запишем в журнал
              Log.w(LOGTAG, "Обновляемся с версии " + oldVersion + " на версию " + newVersion);
              // Удаляем старую таблицу и создаём новую

          if(oldVersion < newVersion)
          {
              try {
                  copyDatabase();
              } catch (IOException e) {
                  e.printStackTrace();
              }

          }

        }
    }
}
