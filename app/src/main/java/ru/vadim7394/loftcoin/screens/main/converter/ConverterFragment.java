package ru.vadim7394.loftcoin.screens.main.converter;


import android.arch.persistence.room.Database;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.vadim7394.loftcoin.App;
import ru.vadim7394.loftcoin.R;
import ru.vadim7394.loftcoin.data.db.DataBase;
import ru.vadim7394.loftcoin.data.db.modal.CoinEntity;
import ru.vadim7394.loftcoin.data.model.Currency;
import ru.vadim7394.loftcoin.screens.currencies.CurrenciesBottomSheet;
import ru.vadim7394.loftcoin.screens.currencies.CurrenciesBottomSheetListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConverterFragment extends Fragment {

    private static final String SOURCE_CURRENCY_BOTTOM_SHEET_TAG = "source_currency_bottom_sheet";
    private static final String DESTINATION_CURRENCY_BOTTOM_SHEET_TAG = "destination_currency_bottom_sheet";

    @BindView(R.id.converter_toolbar)
    Toolbar toolbar;

    @BindView(R.id.source_currency)
    ViewGroup sourceCurrency;

    @BindView(R.id.source_amount)
    EditText sourceAmount;

    @BindView(R.id.destination_currency)
    ViewGroup destinationCurrency;

    @BindView(R.id.destination_amount)
    TextView destinationAmount;
    TextView sourceCurrencySymbolText;
    ImageView sourceCurrencySymbolIcon;
    TextView sourceCurrencySymbolName;
    TextView destinationCurrencySymbolText;
    ImageView destinationCurrencySymbolIcon;
    TextView destinationCurrencySymbolName;

    private ConverterViewModel viewModel;
    private CompositeDisposable disposables = new CompositeDisposable();


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        viewModel.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_converter, container, false);
    }

    @Override
    public void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        toolbar.setTitle(R.string.converter_screen_title);
        DataBase database = ((App) getActivity().getApplication()).getDatabase();
        viewModel = new ConverterViewModelImpl(savedInstanceState, database);
        sourceCurrencySymbolText = sourceCurrency.findViewById(R.id.symbol_text);
        sourceCurrencySymbolIcon = sourceCurrency.findViewById(R.id.symbol_icon);
        sourceCurrencySymbolName = sourceCurrency.findViewById(R.id.currency_name);
        destinationCurrencySymbolText = destinationCurrency.findViewById(R.id.symbol_text);
        destinationCurrencySymbolIcon = destinationCurrency.findViewById(R.id.symbol_icon);
        destinationCurrencySymbolName = destinationCurrency.findViewById(R.id.currency_name);
        if (savedInstanceState == null) {
            sourceAmount.setText("1");
        }
        Fragment bottomSheetSource = getFragmentManager().findFragmentByTag(SOURCE_CURRENCY_BOTTOM_SHEET_TAG);
        if (bottomSheetSource != null) {
            ((CurrenciesBottomSheet) bottomSheetSource).setListener(sourceListener);
        }
        Fragment bottomSheetDestination = getFragmentManager().findFragmentByTag(DESTINATION_CURRENCY_BOTTOM_SHEET_TAG);
        if (bottomSheetDestination != null) {
            ((CurrenciesBottomSheet) bottomSheetDestination).setListener(destinationListner);
        }
        initOutputs();
        initInputs();
    }

    private void initOutputs() {
        Disposable disposable1 = RxTextView.afterTextChangeEvents(sourceAmount).subscribe(event ->
                viewModel.onSourceAmountChange(event.editable().toString())
        );
        sourceCurrency.setOnClickListener(v ->
                viewModel.onSourceCurrencyClick()
        );
        destinationCurrency.setOnClickListener(v ->
                viewModel.onDestinationCurrencyClick()
        );
        disposables.add(disposable1);
    }

    private void initInputs() {
        Disposable disposable1 = viewModel.sourceCurrency().subscribe(currency ->
                bindCurrency(currency, sourceCurrencySymbolIcon, sourceCurrencySymbolText, sourceCurrencySymbolName)
        );
        Disposable disposable2 = viewModel.destinationCurrency().subscribe(currency ->
                bindCurrency(currency, destinationCurrencySymbolIcon, destinationCurrencySymbolText, destinationCurrencySymbolName)
        );
        Disposable disposable3 = viewModel.destinationAmount().subscribe(s ->
                destinationAmount.setText(s)
        );
        Disposable disposable4 = viewModel.selectSourceCurrency().subscribe(o ->
                showCurrenciesBottomSheet(true)
        );
        Disposable disposable5 = viewModel.selectDestinationCurrency().subscribe(o ->
                showCurrenciesBottomSheet(false)
        );
        disposables.add(disposable1);
        disposables.add(disposable2);
        disposables.add(disposable3);
        disposables.add(disposable4);
        disposables.add(disposable5);
    }

    private void showCurrenciesBottomSheet(boolean source) {
        CurrenciesBottomSheet bottomSheet = new CurrenciesBottomSheet();
        if (source) {
            bottomSheet.show(getFragmentManager(), SOURCE_CURRENCY_BOTTOM_SHEET_TAG);
            bottomSheet.setListener(sourceListener);
        } else {
            bottomSheet.show(getFragmentManager(), DESTINATION_CURRENCY_BOTTOM_SHEET_TAG);
            bottomSheet.setListener(destinationListner);
        }
    }

    private CurrenciesBottomSheetListener sourceListener = new CurrenciesBottomSheetListener() {
        @Override
        public void onCurrencySelected(CoinEntity coin) {
            viewModel.onSourceCurrencySelected(coin);
        }
    };

    private CurrenciesBottomSheetListener destinationListner = new CurrenciesBottomSheetListener() {
        @Override
        public void onCurrencySelected(CoinEntity coin) {
            viewModel.onDestinationCurrencySelected(coin);
        }
    };

    private Random random = new Random();
    private static int[] colors = {
            0xFFF5FF30,
            0xFFFFFFFF,
            0xFF2ABDF5,
            0xFFFF7416,
            0xFFFF7416,
            0xFF534FFF,
    };

    private void bindCurrency(String curr, ImageView symbolIcon, TextView symbolText, TextView currencyName) {
        Currency currency = Currency.getCurrency(curr);
        if (currency != null) {
            symbolIcon.setVisibility(View.VISIBLE);
            symbolText.setVisibility(View.GONE);
            symbolIcon.setImageResource(currency.iconRes);
        } else {
            symbolIcon.setVisibility(View.GONE);
            symbolText.setVisibility(View.VISIBLE);
            Drawable background = symbolText.getBackground();
            Drawable wrapped = DrawableCompat.wrap(background);
            DrawableCompat.setTint(wrapped, colors[random.nextInt(colors.length)]);
            symbolText.setText(String.valueOf(curr.charAt(0)));
        }
        currencyName.setText(curr);
    }

}
