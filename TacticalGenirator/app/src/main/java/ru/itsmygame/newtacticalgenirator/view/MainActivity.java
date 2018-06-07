package ru.itsmygame.newtacticalgenirator.view;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ru.itsmygame.newtacticalgenirator.controller.DBController;
import ru.itsmygame.tacticalgenirator.R;

import ru.itsmygame.newtacticalgenirator.controller.UIController;
import ru.itsmygame.newtacticalgenirator.model.AppContext;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */


    private AssigmentFragment assigmentFragment;
    private CompliteFragment  compliteFragment;
    private JournalFragment journalFragment;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;
    private Spinner ArmyChose;

    private ArrayAdapter<String> adapter;

    //курсоры для работы с БД
    private  Cursor importCursor;
    private  Cursor mainCursor;
    private DBController dbController;
    private UIController uiController;

    private final String SAVED_TEXT = "saved_text";
    private final String SAVED_POSITION = "spainer_position";
    private int loadedSize = 0;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        assigmentFragment = new AssigmentFragment();
        compliteFragment = new CompliteFragment();
        journalFragment = new JournalFragment();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setupViewPager(mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //setupTabIcons();

        dbController = new DBController();
        uiController = new UIController();

        AppContext.getDbAdapterImport().open();
        AppContext.getDbAdapterImport().Check();
        AppContext.getDbAdapterMain().open();

        loadText();

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(assigmentFragment, getResources().getString(R.string.obj_string));
        adapter.addFragment(compliteFragment, getResources().getString(R.string.co_string));
        adapter.addFragment(journalFragment, getResources().getString(R.string.jur_string));
        viewPager.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //dbController.discard(assigmentFragment);
            createStartDialog();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void createStartDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.common_string));
        builder.setMessage(getResources().getString(R.string.start_string));
        builder.setCancelable(false);
        final View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        builder.setView(view);

        ArmyChose = (Spinner)   view.findViewById(R.id.spArmy);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.data));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArmyChose.setAdapter(adapter);
        //////
        //получаем порядковый номер из спайнера
        clickOnItem();

        builder.setPositiveButton(getResources().getString(R.string.ok_string), new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {

                dbController.discard(assigmentFragment);

                assigmentFragment.MakeObjectiveInAnotherTable(assigmentFragment.getPositionOfSpiner());

            }

        });

        builder.setNegativeButton(getResources().getString(R.string.cansel_str),new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {

            }

        });
        AlertDialog alert = builder.create();

        alert.show();
    }

    public void  clickOnItem()
    {
        ArmyChose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               assigmentFragment.setPositionOfSpiner((int) adapterView.getItemIdAtPosition(i));
               // savePosition();
                Log.d("tag","" + assigmentFragment.getPositionOfSpiner());

            }
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    void saveText() {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(SAVED_TEXT, assigmentFragment.getObjectivList().size());

        for (int i = 0; i <assigmentFragment.getObjectivList().size() ; i++) {
            ed.remove("Status_" + i);
            ed.putInt("Status_" + i, assigmentFragment.getObjectivList().get(i));
        }

        ed.putInt(SAVED_POSITION, assigmentFragment.getPositionOfSpiner());

        ed.commit();

    }

    void loadText() {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);

        assigmentFragment.getObjectivList().clear();
        int savedText = sPref.getInt(SAVED_TEXT, 0);

        for(int i=0;i<savedText;i++)
        {
            assigmentFragment.getObjectivList().add(sPref.getInt("Status_" + i, 0));

        }

        assigmentFragment.setPositionOfSpiner(sPref.getInt(SAVED_POSITION,0));

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return AssigmentFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        // При закрытии приложения
        //Закрыть подключение к БД
        saveText();

        AppContext.getDbAdapterImport().close();
        AppContext.getDbAdapterMain().close();
    }
}
