package ru.vadim7394.loftcoin;

import android.app.Application;

import ru.vadim7394.loftcoin.data.perfs.Prefs;
import ru.vadim7394.loftcoin.data.perfs.PrefsImpl;


public class App extends Application {

    private Prefs prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = new PrefsImpl(this);
    }
    public Prefs getPrefs() {
        return prefs;
    }
}
