package ru.vadim7394.loftcoin.screens.welcome;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.vadim7394.loftcoin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment {

    private static final String KEY_PAGE = "page";

    public static WelcomeFragment newInstance(WelcomePage page) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_PAGE, page);
        WelcomeFragment fragment = new WelcomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public WelcomeFragment() {
    }

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.subtitle)
    TextView subtitle;

    private Unbinder unbinder;
    private WelcomePage page;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            page = getArguments().getParcelable(KEY_PAGE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        icon.setImageResource(page.getIcon());
        title.setText(page.getTitle());
        subtitle.setText(page.getSubtitle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
