package ru.itsmygame.newtacticalgenirator.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ru.itsmygame.tacticalgenirator.R;
import ru.itsmygame.newtacticalgenirator.controller.DBController;
import ru.itsmygame.newtacticalgenirator.controller.UIController;
import ru.itsmygame.newtacticalgenirator.model.AppContext;

/**
 * Created by llobachev on 03.08.2017.
 */

public class JournalFragment  extends Fragment {


    private ListView lvJournal;
    private SimpleCursorAdapter scAdapter;

    private static final int FJ_RETURN_ID = 4;

    private Cursor Jcursor;

    private DBController dbController;
    private UIController uiController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_journal, container, false);

        lvJournal = (ListView) v.findViewById(R.id.lvJournal);

        dbController = new DBController();
        uiController = new UIController();

        Jcursor = dbController.getAllData(AppContext.getDbAdapterMain().getmDB(), AppContext.getDbAdapterMain().getDbJournal());
        Jcursor.moveToFirst();

        uiController.crListView(lvJournal, getActivity(), R.layout.db_item, Jcursor, getResources().getString(R.string.TactID), getResources().getString(R.string.NameObjective), R.id.tvTID, R.id.tvName);

        Jcursor.requery();

        registerForContextMenu(lvJournal);

        return v;
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, FJ_RETURN_ID, 0, getResources().getString(R.string.return_str));

    }

    //контекстное меню для списа
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == FJ_RETURN_ID) {
            // Получает индифкатор элемента
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            //Добавляем елемнт в БД с заданиями в 1 табе
           dbController.GenObjectiveMain(Jcursor,AppContext.getDbAdapterMain().getmDB());
//            dbController.addRecT(AppContext.getDbAdapterMain().getmDB(),Jcursor.getInt(Jcursor.getColumnIndex(getResources().getString(R.string._id))),
//                    Jcursor.getString(Jcursor.getColumnIndex(getResources().getString(R.string.NameObjective))),
//                    Jcursor.getString(Jcursor.getColumnIndex(getResources().getString(R.string.Discription))),
//                    Jcursor.getInt(Jcursor.getColumnIndex(getResources().getString(R.string.TactID))));


            // Обновление курсора
            Jcursor.requery();

            return true;
        }
        return super.onContextItemSelected(item);
    }

}
