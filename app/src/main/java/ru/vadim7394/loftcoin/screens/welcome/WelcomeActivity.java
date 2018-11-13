package ru.vadim7394.loftcoin.screens.welcome;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.vadim7394.loftcoin.App;
import ru.vadim7394.loftcoin.R;
import ru.vadim7394.loftcoin.data.perfs.Prefs;
import ru.vadim7394.loftcoin.screens.start.StartActivity;

public class WelcomeActivity extends AppCompatActivity {

    public static void startInNewTask(Context context) {
        Intent starter = new Intent(context, WelcomeActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.start_btn)
    Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

        final Prefs prefs = ((App) getApplication()).getPrefs();

        pager.setAdapter(new WelcomePagerAdapter(getSupportFragmentManager()));
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.setFirstLaunch(false);
                StartActivity.startInNewTask(WelcomeActivity.this);
            }
        });
    }
}
