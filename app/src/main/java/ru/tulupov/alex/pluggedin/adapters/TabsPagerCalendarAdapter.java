package ru.tulupov.alex.pluggedin.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import static ru.tulupov.alex.pluggedin.constants.Constants.*;
import ru.tulupov.alex.pluggedin.fragments.CalendarFragment;


public class TabsPagerCalendarAdapter extends FragmentPagerAdapter {

    private String tabs[];

    private CalendarFragment filmFragment;
    private CalendarFragment gamesFragment;

    public TabsPagerCalendarAdapter(FragmentManager fm, String[] tabs) {
        super(fm);
        this.tabs = tabs;
        filmFragment = CalendarFragment.getInstance(TYPE_FILM);
        gamesFragment = CalendarFragment.getInstance(TYPE_GAME);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TAB_FILMS:
                return filmFragment;
            case TAB_GAMES:
                return gamesFragment;
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
