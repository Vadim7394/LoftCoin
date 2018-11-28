package ru.vadim7394.loftcoin.screens.main.wallets;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.vadim7394.loftcoin.App;
import ru.vadim7394.loftcoin.R;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;
import ru.vadim7394.loftcoin.data.perfs.Prefs;
import ru.vadim7394.loftcoin.screens.currencies.CurrenciesBottomSheet;
import ru.vadim7394.loftcoin.screens.currencies.CurrenciesBottomSheetListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletsFragment extends Fragment implements CurrenciesBottomSheetListener {

    private static final String TAG = "WalletsFragment";

    private static final String VIEW_PAGER_POS = "view_page_pos";

    @BindView(R.id.wallets_toolbar)
    Toolbar toolbar;

    @BindView(R.id.transactions_recycler)
    RecyclerView transactionsRecycler;

    @BindView(R.id.wallets_pager)
    ViewPager walletsPager;

    @BindView(R.id.new_wallet)
    ViewGroup newWallet;

    private WalletsPagerAdapter walletsPagerAdapter;
    private WalletsViewModel viewModel;
    private TransactionsAdapter transactionsAdapter;

    private Integer restoredViewPagerPos;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(WalletsViewModelImpl.class);
        Prefs prefs = ((App) getActivity().getApplication()).getPrefs();
        walletsPagerAdapter = new WalletsPagerAdapter(prefs);
        transactionsAdapter = new TransactionsAdapter(prefs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        toolbar.setTitle(R.string.accounts_screen_title);
        toolbar.inflateMenu(R.menu.menu_wallets);

        transactionsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        transactionsRecycler.setHasFixedSize(true);
        transactionsRecycler.setAdapter(transactionsAdapter);

        int screenWidth = getScreenWidth();
        int walletItemWidth = getResources().getDimensionPixelOffset(R.dimen.item_wallet_width);
        int walletItemMargin = getResources().getDimensionPixelOffset(R.dimen.item_wallet_margin);
        int pageMargin = (screenWidth - walletItemWidth) - walletItemMargin;
        walletsPager.setPageMargin(-pageMargin);
        walletsPager.setOffscreenPageLimit(5);
        walletsPager.setAdapter(walletsPagerAdapter);
        walletsPager.setPageTransformer(false, new ZoomOutPageTransformer());

        Fragment bottomSheet = getFragmentManager().findFragmentByTag(CurrenciesBottomSheet.TAG);
        if (bottomSheet != null) {
            ((CurrenciesBottomSheet) bottomSheet).setListener(this);
        }

        if (savedInstanceState != null) {
            restoredViewPagerPos = savedInstanceState.getInt(VIEW_PAGER_POS, 0);
        }

        viewModel.getWallets();
        initOutputs();
        initInputs();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(VIEW_PAGER_POS, walletsPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    private void initInputs() {
        viewModel.wallets().observe(this, wallets -> {
            walletsPagerAdapter.setWallets(wallets);
            if (restoredViewPagerPos != null) {
                walletsPager.setCurrentItem(restoredViewPagerPos);
                restoredViewPagerPos = null;
            }
        });

        viewModel.scrollToNewWallet().observe(this, o -> {
            Log.d(TAG, "scrollToNewWallet");
            walletsPager.setCurrentItem(walletsPagerAdapter.getCount() - 1, true);
        });

        viewModel.transactions().observe(this, transactionModels -> {
            transactionsAdapter.setTransactions(transactionModels);
        });

        viewModel.walletsVisible().observe(this, visible ->
                walletsPager.setVisibility(visible ? View.VISIBLE : View.GONE)
        );

        viewModel.newWalletVisible().observe(this, visible ->
                newWallet.setVisibility(visible ? View.VISIBLE : View.GONE)
        );

        viewModel.selectCurrency().observe(this, o -> {
            showCurrenciesBottomSheet();
        });
    }

    private void showCurrenciesBottomSheet() {
        CurrenciesBottomSheet bottomSheet = new CurrenciesBottomSheet();
        bottomSheet.show(getFragmentManager(), CurrenciesBottomSheet.TAG);
        bottomSheet.setListener(this);
    }

    private void initOutputs() {
        newWallet.setOnClickListener(view -> viewModel.onNewWalletClick());
        toolbar.getMenu().findItem(R.id.menu_item_add_wallet).setOnMenuItemClickListener(menuItem -> {
            viewModel.onNewWalletClick();
            return true;
        });

        walletsPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                viewModel.onWalletChanged(position);
            }
        });
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return width;
    }

    @Override
    public void onCurrencySelected(CoinEntity coin) {
        viewModel.onCurrencySelected(coin);
    }


    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);
            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
