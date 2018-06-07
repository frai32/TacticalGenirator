package ru.itsmygame.newtacticalgenirator.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import ru.itsmygame.newtacticalgenirator.view.AssigmentFragment;

/**
 * Created by llobachev on 13.07.2017.
 */

public class UIController {

    public ListView crListView(ListView lvObjective, Activity activyty, int IDListView, Cursor cursor, String TactID, String NameObjective, int tvTID, int tvName)
    {
        String[] from1 = new String[] { TactID, NameObjective};
        int[] to1 = new int[] {tvTID, tvName};
        SimpleCursorAdapter scAdapter = new SimpleCursorAdapter(activyty, IDListView, cursor, from1, to1);
        Log.d("LOGLCREATER", "" + scAdapter.getCount());
        lvObjective.setAdapter(scAdapter);

        return lvObjective;
    }

    //Диалог сброса
    public void discardDialog(Activity activyty, final  AssigmentFragment assigmentFragment, String discard, String yes, String no)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activyty);
        builder.setTitle(discard);
        //Если нет ничего не делаем
        builder.setNegativeButton(no,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        //Если да стираем содержимое с таблиц
        builder.setPositiveButton(yes, new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {

                DBController dbController  = new DBController();
                dbController.discard(assigmentFragment);
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void createInfoDialog(Activity activity, Cursor cursor, String TitleD, String description, String Ok)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(TitleD);
        String s =(cursor.getString(cursor.getColumnIndex(description)));
        builder.setMessage(s);

        builder.setPositiveButton(Ok, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {}

        });

        AlertDialog alert = builder.create();
        alert.show();
    }



}
