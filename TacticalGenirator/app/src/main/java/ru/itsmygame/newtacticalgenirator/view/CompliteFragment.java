package ru.itsmygame.newtacticalgenirator.view;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import ru.itsmygame.newtacticalgenirator.controller.DBController;
import ru.itsmygame.newtacticalgenirator.controller.UIController;
import ru.itsmygame.newtacticalgenirator.model.AppContext;
import ru.itsmygame.tacticalgenirator.R;

/**
 * Created by llobachev on 02.08.2017.
 */

public class CompliteFragment extends Fragment {


    private ListView lvComplite;
    private SimpleCursorAdapter scAdapter;
    private static final int CF_RETURN_ID = 3;

    private static Cursor Ccursor;

    private DBController dbController;
    private UIController uiController;

    public static Cursor getCcursor() {
        return Ccursor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_complite, container, false);
        lvComplite = (ListView) v.findViewById(R.id.lvComplite);

        dbController = new DBController();
        uiController = new UIController();

        Ccursor = dbController.getAllData(AppContext.getDbAdapterMain().getmDB(), AppContext.getDbAdapterMain().getDbComp());
        Ccursor.moveToFirst();
        uiController.crListView(lvComplite, getActivity(), R.layout.db_item, Ccursor, getResources().getString(R.string.TactID), getResources().getString(R.string.NameObjective), R.id.tvTID, R.id.tvName);

        Ccursor.requery();

        registerForContextMenu(lvComplite);

        lvComplite.setOnItemClickListener(new   AdapterView.OnItemClickListener(){
            public  void   onItemClick( AdapterView<?> parent,   View view ,
                                        int position,   long id )   {
                uiController.createInfoDialog(getActivity(),  Ccursor,
                        getResources().getString(R.string.dis_string),
                        getResources().getString(R.string.Discription),
                        getResources().getString(R.string.ok_string));
            }
        } );

        return v;
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CF_RETURN_ID, 0, getResources().getString(R.string.return_str));

    }

    //контекстное меню для списа
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CF_RETURN_ID) {
            // Получает индифкатор элемента
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

           dbController.GenirateObjective(Ccursor,AppContext.getDbAdapterMain().getmDB());
            AssigmentFragment.getMainCursor().requery();

            // удоляет эту позицию по id
            dbController.delRec(AppContext.getDbAdapterMain().getmDB(),
                    AppContext.getDbAdapterMain().getDbComp(),
                    AppContext.getDbAdapterMain().getColumnId(),
                    acmi.id);
            // Обновление курсора
          Ccursor.requery();

            return true;
        }
        return super.onContextItemSelected(item);
    }

}
