package ru.itsmygame.newtacticalgenirator.view;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.itsmygame.newtacticalgenirator.controller.DBController;
import ru.itsmygame.newtacticalgenirator.controller.UIController;
import ru.itsmygame.newtacticalgenirator.model.AppContext;
import ru.itsmygame.tacticalgenirator.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by llobachev on 24.07.2017.
 */

public class AssigmentFragment extends Fragment implements View.OnClickListener {

    public List<Integer> getObjectivList() {
        return ObjectivList;
    }

    public TextView getTvObjective() {
        return tvObjective;
    }
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private final String LOG_TAG = "myLogs";

    public void setObjectivList(List<Integer> objectivList) {
        ObjectivList = objectivList;
    }

    private List<Integer> ObjectivList = new ArrayList<Integer>();
    private final int START_VALUE = 100;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView lvObjective;
    private TextView tvObjective;
    private Button btGenirate;
    private Cursor importCursor;

    private String sID ="Строка для ID";
    private String sName="Строка для имени";
    private String sDiscription="Строка для заданий";

    private static final int CM_DELETE_ID = 1;
    private static final int CM_REDACT_ID = 2;

    public void setPositionOfSpiner(int positionOfSpiner) {
        PositionOfSpiner = positionOfSpiner;
    }

    private int PositionOfSpiner = 0;

    public int getPositionOfSpiner() {
        return PositionOfSpiner;
    }


    public static Cursor getMainCursor() {
        return mainCursor;
    }

    private static Cursor mainCursor;
    private DBController dbController;
    private UIController uiController;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AssigmentFragment newInstance(int sectionNumber) {
        AssigmentFragment  fragment = new AssigmentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        tvObjective = (TextView) rootView.findViewById(R.id.tvObjective);
        uiController = new UIController();
        dbController = new DBController();
        importCursor = dbController.getAllData(AppContext.getDbAdapterImport().getmDB(), AppContext.getDbAdapterImport().getDbTable()[getPositionOfSpiner()]);
        mainCursor = dbController.getAllData(AppContext.getDbAdapterMain().getmDB(), AppContext.getDbAdapterMain().getDbTable());
        importCursor.moveToFirst();


        lvObjective = (ListView) rootView.findViewById(R.id.lvObjective);
        uiController.crListView(lvObjective, getActivity(), R.layout.db_item, mainCursor, getResources().getString(R.string.TactID), getResources().getString(R.string.NameObjective), R.id.tvTID, R.id.tvName);


        ObjectivList.add(START_VALUE);
        btGenirate = (Button) rootView.findViewById(R.id.btGenirate);
        btGenirate.setOnClickListener(this);

        registerForContextMenu(lvObjective);

        lvObjective.setOnItemClickListener(new   AdapterView.OnItemClickListener(){
            public  void   onItemClick(AdapterView<?> parent, View view ,
                                       int position, long id )   {
                uiController.createInfoDialog(getActivity(), mainCursor,
                        getResources().getString(R.string.dis_string),
                        getResources().getString(R.string.Discription),
                        getResources().getString(R.string.ok_string));

            }
        } );

        //getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER))
        return rootView;
    }


    public void MakeObjectiveInAnotherTable(int getPosition)
    {


        //берёт таблицу по нужому индифекатору

       List<Integer> tableNameId = new ArrayList<Integer>();

        tableNameId.add(R.string.TacticalTabel);
        tableNameId.add(R.string.SpaceMarines);
        tableNameId.add(R.string.GKObjective);
        tableNameId.add(R.string.ChaosSpaceMarines);
        tableNameId.add(R.string.AstraMilitarum);
        tableNameId.add(R.string.Cultmechanicum);
        tableNameId.add(R.string.DeathGuard);
        tableNameId.add(R.string.BloodAngelObjective);
        tableNameId.add(R.string.DarkAngels);
        tableNameId.add(R.string.OrksObjective);
        tableNameId.add(R.string.DarkEldarObjective);
        tableNameId.add(R.string.NecronObjective);
        tableNameId.add(R.string.SpaceWolfsObjective);
        tableNameId.add(R.string.KhornDemonkin);
        tableNameId.add(R.string.Eldar);
        tableNameId.add(R.string.HarlequinsObjective);

        ChangeCursorStats(getResources().getString(tableNameId.get(getPosition)));
    }


    public void ChangeCursorStats(String nameTable)
    {

        importCursor = dbController.getAllData(AppContext.getDbAdapterImport().getmDB(), nameTable);
        //startManagingCursor(Acursor);
        importCursor.requery();
        importCursor.moveToFirst();

    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, getResources().getString(R.string.con_menu_del));
        menu.add(0, CM_REDACT_ID, 0, getResources().getString(R.string.con_menu_co));
    }

    //контекстное меню для списа
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();;
        if (item.getItemId() == CM_DELETE_ID) {
            // удоляет эту позицию по id

            dbController.delRec(AppContext.getDbAdapterMain().getmDB(),
                                AppContext.getDbAdapterMain().getDbTable(),
                                AppContext.getDbAdapterMain().getColumnId(),
                                acmi.id);

            // Обновление курсора
            mainCursor.requery();

            return true;
        }
        //Перенос в журнал выполненых заданий
        if (item.getItemId() == CM_REDACT_ID) {
            //vpDialog();
            mainCursor = dbController.GenObjectiveToComplite(mainCursor, AppContext.getDbAdapterMain().getmDB(), CompliteFragment.getCcursor());
            //Log.d(LOG_TAG, ""+ AppContext.getDbAdapterMain().getVp());

            // удоляет эту позицию по id
            dbController.delRec(AppContext.getDbAdapterMain().getmDB(),
                    AppContext.getDbAdapterMain().getDbTable(),
                    AppContext.getDbAdapterMain().getColumnId(),
                    acmi.id);
            // Обновление курсора
            mainCursor.requery();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btGenirate:

                dbController.buttonGenirate(importCursor, ObjectivList, tvObjective);

                mainCursor.requery();

                break;
            default:
                break;
        }
    }


}