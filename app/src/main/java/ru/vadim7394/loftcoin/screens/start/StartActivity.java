package ru.vadim7394.loftcoin.screens.start;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.vadim7394.loftcoin.App;
import ru.vadim7394.loftcoin.R;
import ru.vadim7394.loftcoin.data.api.Api;
import ru.vadim7394.loftcoin.data.db.DataBase;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntityMapper;
import ru.vadim7394.loftcoin.data.perfs.Prefs;
import ru.vadim7394.loftcoin.screens.main.MainActivity;

public class StartActivity extends AppCompatActivity implements StartView {

    private static final String TAG = "StartActivity";

    private StartPresenter presenter;

    public static void startInNewTask(Context context) {
        Intent starter = new Intent(context, StartActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    @BindView(R.id.start_top_corner)
    ImageView topCorner;

    @BindView(R.id.start_bottom_corner)
    ImageView bottomCorner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        Api api = ((App) getApplication()).getApi();
        Prefs prefs = ((App) getApplication()).getPrefs();

        DataBase dataBase = ((App) getApplication()).getDatabase();
        CoinEntityMapper mapper = new CoinEntityMapper();

        presenter = new StartPresenterImpl(api, prefs, dataBase, mapper);
        presenter.attachView(this);
        presenter.loadRate();
    }


    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startAnimations();
    }

    private void startAnimations() {
        ObjectAnimator innerAnimator = ObjectAnimator.ofFloat(topCorner, "rotation", 0, 360);
        innerAnimator.setDuration(30000);
        innerAnimator.setRepeatMode(ValueAnimator.RESTART);
        innerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        innerAnimator.setInterpolator(new LinearInterpolator());

        ObjectAnimator outerAnimator = ObjectAnimator.ofFloat(bottomCorner, "rotation", 0, -360);
        outerAnimator.setDuration(60000);
        outerAnimator.setRepeatMode(ValueAnimator.RESTART);
        outerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        outerAnimator.setInterpolator(new LinearInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.play(innerAnimator).with(outerAnimator);
        set.start();

    }

    @Override
    public void navigateToMainScreen() {
        MainActivity.startInNewTask(this);
    }
}
