package ru.tulupov.alex.pluggedin.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import static ru.tulupov.alex.pluggedin.constants.Constants.*;
import ru.tulupov.alex.pluggedin.fragments.CalendarFragment;


public class TabsPagerCalendarAdapter extends FragmentPagerAdapter {

    private String tabs[];

    public TabsPagerCalendarAdapter(FragmentManager fm, String[] tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TAB_FILMS:
                return CalendarFragment.getInstance(TYPE_FILM);
            case TAB_GAMES:
                return CalendarFragment.getInstance(TYPE_GAME);
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return tabs[position];
//    }
}
