package ru.vadim7394.loftcoin.screens.welcome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ru.vadim7394.loftcoin.R;

public class WelcomePagerAdapter extends FragmentPagerAdapter {

    private List<WelcomePage> pages;


    WelcomePagerAdapter(FragmentManager fm) {
        super(fm);
        pages = new ArrayList<>();
        pages.add(new WelcomePage(R.drawable.image_welcome_page_1, R.string.welcome_title_1, R.string.welcome_subtitle_1));
        pages.add(new WelcomePage(R.drawable.image_welcome_page_1, R.string.welcome_title_2, R.string.welcome_subtitle_2));
        pages.add(new WelcomePage(R.drawable.image_welcome_page_1, R.string.welcome_title_3, R.string.welcome_subtitle_3));
    }

    @Override
    public Fragment getItem(int position) {
        return WelcomeFragment.newInstance(pages.get(position));
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
