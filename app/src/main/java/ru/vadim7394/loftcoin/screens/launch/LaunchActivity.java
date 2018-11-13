package ru.vadim7394.loftcoin.screens.launch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.vadim7394.loftcoin.App;
import ru.vadim7394.loftcoin.data.perfs.Prefs;
import ru.vadim7394.loftcoin.R;
import ru.vadim7394.loftcoin.screens.start.StartActivity;
import ru.vadim7394.loftcoin.screens.welcome.WelcomeActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launch);

        final Prefs prefs = ((App) getApplication()).getPrefs();

        if (prefs.isFirstLaunch()) {
            WelcomeActivity.startInNewTask(this);
        } else {
            StartActivity.startInNewTask(this);
        }
    }
}
