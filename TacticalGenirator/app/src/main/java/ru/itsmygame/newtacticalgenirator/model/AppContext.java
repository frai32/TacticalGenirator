package ru.itsmygame.newtacticalgenirator.model;

/**
 * Created by llobachev on 29.06.2017.
 */


    import android.app.Application;

    public class AppContext extends Application {

        @Override
        public void onCreate() {
            // TODO Auto-generated method stub
            super.onCreate();
            //Фрагмент класса экспортируемой БД
            dbAdapterImport = new DBImportModel(this);
            //фрагмент класса основной БД куда добовляються записи из експортируемой
            dbAdapterMain = new DBModel(this);
        }


        private static DBImportModel dbAdapterImport;

        //Доступ к экземпляру класса экспортируемой бд
        public static DBImportModel getDbAdapterImport() {
            return dbAdapterImport;
        }

        private static DBModel dbAdapterMain;

        //Доступ к экземпляру класса основной бд
        public static DBModel getDbAdapterMain() {
            return dbAdapterMain;
        }



    }

