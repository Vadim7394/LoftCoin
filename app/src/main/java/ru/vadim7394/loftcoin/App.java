package ru.vadim7394.loftcoin;

import android.app.Application;

import ru.vadim7394.loftcoin.data.api.Api;
import ru.vadim7394.loftcoin.data.api.ApiInitializer;
import ru.vadim7394.loftcoin.data.perfs.Prefs;
import ru.vadim7394.loftcoin.data.perfs.PrefsImpl;


public class App extends Application {

    private Api api;

    private Prefs prefs;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = new PrefsImpl(this);

        api = new ApiInitializer().init();
    }

    public Prefs getPrefs() {
        return prefs;
    }

    public  Api getApi() {
        return api;
    }
}
