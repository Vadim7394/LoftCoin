package ru.vadim7394.loftcoin.data.perfs;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsImpl implements Prefs {

    private static final String PREFS_NAME = "prefs";
    private static final String KEY_FIRST_LAUNCH = "first_launch";

    private Context context;


    public PrefsImpl(Context context) {
        this.context = context;
    }

    private SharedPreferences getPrefs() {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isFirstLaunch() {
        return getPrefs().getBoolean(KEY_FIRST_LAUNCH, true);
    }

    @Override
    public void setFirstLaunch(Boolean firstLaunch) {
        getPrefs().edit().putBoolean(KEY_FIRST_LAUNCH, firstLaunch).apply();
    }
}
