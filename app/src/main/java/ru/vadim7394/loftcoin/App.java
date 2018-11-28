package ru.vadim7394.loftcoin;

import android.app.Application;

import ru.vadim7394.loftcoin.data.api.Api;
import ru.vadim7394.loftcoin.data.api.ApiInitializer;
import ru.vadim7394.loftcoin.data.db.DataBase;
import ru.vadim7394.loftcoin.data.db.DatabaseInitializer;
import ru.vadim7394.loftcoin.data.db.realm.DatabaseImplRealm;
import ru.vadim7394.loftcoin.data.perfs.Prefs;
import ru.vadim7394.loftcoin.data.perfs.PrefsImpl;


public class App extends Application {

    private Api api;

    private Prefs prefs;

    private DataBase database;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = new PrefsImpl(this);

        api = new ApiInitializer().init();

        new DatabaseInitializer().init(this);
    }

    public Prefs getPrefs() {
        return prefs;
    }

    public  Api getApi() {
        return api;
    }

    public DataBase getDatabase() {
        return new DatabaseImplRealm();
    }
}
