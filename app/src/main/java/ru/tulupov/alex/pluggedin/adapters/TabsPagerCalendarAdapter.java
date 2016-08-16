package ru.tulupov.alex.pluggedin.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import ru.tulupov.alex.pluggedin.constants.Constants;
import ru.tulupov.alex.pluggedin.fragments.CalendarFragment;


public class TabsPagerCalendarAdapter extends FragmentPagerAdapter {

    public TabsPagerCalendarAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Constants.TAB_FILMS:
                return CalendarFragment.getInstance(Constants.TYPE_FILM);
            case Constants.TAB_GAMES:
                return CalendarFragment.getInstance(Constants.TYPE_GAME);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
