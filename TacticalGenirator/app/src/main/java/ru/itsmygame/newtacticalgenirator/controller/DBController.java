package ru.itsmygame.newtacticalgenirator.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.itsmygame.newtacticalgenirator.model.AppContext;
import ru.itsmygame.newtacticalgenirator.view.AssigmentFragment;

/**
 * Created by llobachev on 29.06.2017.
 */

public class DBController implements CrudComand  {


    @Override
    public Cursor getAllData(SQLiteDatabase mDB, String tableName) {

        return mDB.query(tableName, null, null, null, null, null, null);
    }

    @Override
    public void delRec(SQLiteDatabase mDB, String tableName, String columName, long id) {
        mDB.delete(tableName, columName + " = " + id, null);
    }

    @Override
    public void delRec(SQLiteDatabase mDB, String tableName, String columName) {
        mDB.delete(tableName, columName, null);
    }


    @Override
    public ContentValues addRec(int id, String name, String discription, int oid) {
        ContentValues cv = new ContentValues();
        cv.put(AppContext.getDbAdapterMain().getColumnId(), id);
        cv.put(AppContext.getDbAdapterMain().getColumnName(), name);
        cv.put(AppContext.getDbAdapterMain().getColumnDs(), discription);
        cv.put(AppContext.getDbAdapterMain().getColumnTid(), oid);

        return cv;

    }

    @Override
    public void addRecTJ(SQLiteDatabase mDB,int id, String name, String discription, int oid) {
        mDB.insert(AppContext.getDbAdapterMain().getDbTable(), null, addRec(id, name, discription, oid));
        mDB.insert(AppContext.getDbAdapterMain().getDbJournal(), null, addRec(id, name, discription, oid));
    }

    @Override
    public void addRecT(SQLiteDatabase mDB,int id, String name, String discription, int oid) {
        mDB.insert(AppContext.getDbAdapterMain().getDbTable(), null, addRec(id, name, discription, oid));
    }

    @Override
    public void addRecToComplite(SQLiteDatabase mDB,int id, String name, String discription, int oid, Cursor cursor) {
        mDB.insert(AppContext.getDbAdapterMain().getDbComp(), null, addRec(id, name, discription, oid));
        cursor.requery();
    }


    public void GenirateObjective(Cursor cursor,SQLiteDatabase mDB) {

        addRecTJ(mDB,cursor.getInt(cursor.getColumnIndex("_id")),
                cursor.getString(cursor.getColumnIndex("NameObjective")),
                cursor.getString(cursor.getColumnIndex("Discription")),
                cursor.getInt(cursor.getColumnIndex("TactID")));

//        AppContext.getDbAdapterMain().addRecTJ(cursor.getInt(cursor.getColumnIndex(getResources().getString(R.string._id))),
//                cursor.getString(cursor.getColumnIndex(getResources().getString(R.string.NameObjective))),
//                cursor.getString(cursor.getColumnIndex(getResources().getString(R.string.Discription))),
//                cursor.getInt(cursor.getColumnIndex(getResources().getString(R.string.TactID))),
//                cursor.getInt(cursor.getColumnIndex(getResources().getString(R.string.VP))));

        cursor.requery();
    }



    public Cursor GenObjectiveToComplite(Cursor cursor,SQLiteDatabase mDB, Cursor anotherCursor)
    {
       // String tid = getResources().getString(R.string.TactID);

        addRecToComplite(mDB,cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("NameObjective")),
                        cursor.getString(cursor.getColumnIndex("Discription")),
                        cursor.getInt(cursor.getColumnIndex("TactID")) , cursor );

        anotherCursor.requery();
        return cursor;
    }

    public void GenObjectiveMain(Cursor cursor,SQLiteDatabase mDB)
    {
        // String tid = getResources().getString(R.string.TactID);
        addRecT(mDB,cursor.getInt(cursor.getColumnIndex("_id")),
                cursor.getString(cursor.getColumnIndex("NameObjective")),
                cursor.getString(cursor.getColumnIndex("Discription")),
                cursor.getInt(cursor.getColumnIndex("TactID")));

        cursor.requery();


    }


    public void genirator(int Res,  Cursor cursor, List<Integer> List, TextView tvObjective) {

        //перемещаем курсор на 1 место в таблице
        cursor.moveToFirst();
        //пробигаемся по счётчику
        for(int i = 0; i<List.size(); i++)
        {
            //Если совпадений нет
            if ( !List.contains(Res))
            {

                List.add(Res);//добавляем номер в счётчик
                cursor.moveToFirst();
                cursor.moveToPosition(Res);//переходим на позицию случайного числа
                //присваиваем значение из БД полям на Активити
               String sID = cursor.getString(cursor.getColumnIndex("TactID"));
               String sName = cursor.getString(cursor.getColumnIndex("NameObjective"));
               String sDiscription = cursor.getString(cursor.getColumnIndex("Discription"));
                //добавляем задание
                GenirateObjective(cursor, AppContext.getDbAdapterMain().getmDB());

               tvObjective.setText(sID +" : "+sName);

            }
        }

    }

    public void buttonGenirate(Cursor Acursor, List<Integer> ObjectivList, TextView tvObjective)
    {
        Random random = new Random();
        int Res = random.nextInt(36);//получаем случайное число

        //ищем было ли уже такое задание
        for(int i = 0; i < ObjectivList.size(); i++ )
        {
            if(ObjectivList.contains(Res))//если да
            {
                Res = random.nextInt(36);// создаём по новой
            }
        }
        //добавляем в список задание
        genirator(Res, Acursor,ObjectivList, tvObjective);
            //Log.d("tag",""+"Res "+ (Res-1) + " RES " + Res);
        Collections.sort(ObjectivList);
        for(int i = 0; i<ObjectivList.size(); i++)
        {
            Log.d("tag", "" + ObjectivList.get(i));
        }
    }


    public void genirator(int Res, Cursor cursor, List<Integer> List) {
        //перемещаем курсор на 1 место в таблице
        cursor.moveToFirst();
        //пробигаемся по счётчику
        for(int i = 0; i<List.size(); i++)
        {
            //Если совпадений нет
            if ( !List.contains(Res))
            {

                List.add(Res);//добавляем номер в счётчик
                cursor.moveToFirst();
                cursor.moveToPosition(Res);//переходим на позицию случайного числа
                //присваиваем значение из БД полям на Активити

                GenirateObjective(cursor, AppContext.getDbAdapterMain().getmDB());

                // tvObjective.setText(sID +" : "+sName);

            }
        }
    }

    //Функция стирания
    public void discard(AssigmentFragment assigmentFragment)
    {
        delRec(AppContext.getDbAdapterMain().getmDB(), AppContext.getDbAdapterMain().getDbTable(), AppContext.getDbAdapterMain().getColumnId());
        //стирает  пока есть элементы в счётчике
        for(int i = assigmentFragment.getObjectivList().size(); i >= 0; i--)
        {
            delRec(AppContext.getDbAdapterMain().getmDB(), AppContext.getDbAdapterMain().getDbJournal(), AppContext.getDbAdapterMain().getColumnId());
            delRec(AppContext.getDbAdapterMain().getmDB(), AppContext.getDbAdapterMain().getDbComp(), AppContext.getDbAdapterMain().getColumnId());

        }
        //Чистит счётчик
        for(int i = assigmentFragment.getObjectivList().size()-1; i>=0; i--)
        {
            assigmentFragment.getObjectivList().remove(i);
        }
        assigmentFragment.getObjectivList().add(100);

        assigmentFragment.getMainCursor().requery();
        //Устонавливаем начальные значения
       // assigmentFragment.rec();

        //Показывает начальный диалог
        //createStartDialog();
    }


}
