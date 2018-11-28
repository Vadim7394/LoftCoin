package ru.vadim7394.loftcoin.screens.main.rate;


import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.vadim7394.loftcoin.App;
import ru.vadim7394.loftcoin.R;
import ru.vadim7394.loftcoin.data.api.Api;

import ru.vadim7394.loftcoin.data.db.DataBase;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntityMapper;
import ru.vadim7394.loftcoin.data.model.Fiat;
import ru.vadim7394.loftcoin.data.perfs.Prefs;

/**
 * A simple {@link Fragment} subclass.
 */
public class RateFragment extends Fragment implements RateView, Toolbar.OnMenuItemClickListener, CurrencyDialog.CurrencyDialogListener {

    private static final String LAYOUT_MANAGER_STATE = "layout_manager_state";

    @BindView(R.id.rate_recycler)
    RecyclerView recycler;

    @BindView(R.id.rate_toolbar)
    Toolbar toolbar;

    @BindView(R.id.rate_refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.rate_content)
    ViewGroup content;

    @BindView(R.id.progress)
    ViewGroup progress;

    private RatePresenterImpl presenter;
    private RateAdapter adapter;
    private Unbinder unbinder;

    private Parcelable layoutMangerState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        Api api = ((App) getActivity().getApplication()).getApi();
        Prefs prefs = ((App) getActivity().getApplication()).getPrefs();
        DataBase mainDatabase = ((App) getActivity().getApplication()).getDatabase();
        DataBase workerDatabase = ((App) getActivity().getApplication()).getDatabase();
        CoinEntityMapper mapper = new CoinEntityMapper();
        presenter = new RatePresenterImpl(api, prefs, mainDatabase, workerDatabase, mapper);
        adapter = new RateAdapter(prefs);
        adapter.setHasStableIds(true);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.rate_screen_title);

        toolbar.inflateMenu(R.menu.menu_rate);
        toolbar.setOnMenuItemClickListener(this);

        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
            }
        });

        if (savedInstanceState != null) {
            layoutMangerState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
        }

        Fragment fragment = getFragmentManager().findFragmentByTag(CurrencyDialog.TAG);

        if (fragment != null) {
            ((CurrencyDialog) fragment).setListener(this);
        }

        presenter.attachView(this);
        presenter.getRate();


    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(LAYOUT_MANAGER_STATE, recycler.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setCoins(List<CoinEntity> coins) {

        adapter.setCoins(coins);
        if (layoutMangerState != null) {
            recycler.getLayoutManager().onRestoreInstanceState(layoutMangerState);
            layoutMangerState = null;
        }
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        refresh.setRefreshing(refreshing);
    }

    @Override
    public void showCurrencyDialog() {
        CurrencyDialog dialog = new CurrencyDialog();
        dialog.setListener(this);
        dialog.show(getFragmentManager(), CurrencyDialog.TAG);
    }

    @Override
    public void onCurrencySelected(Fiat currency) {
        presenter.onFiatCurrencySelected(currency);
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_currency:
                presenter.onMenuItemCurrencyClick();
                return true;
        }
        return false;
    }
}
