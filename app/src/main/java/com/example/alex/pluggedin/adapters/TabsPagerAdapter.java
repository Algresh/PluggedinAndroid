package com.example.alex.pluggedin.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.alex.pluggedin.fragments.ReviewFragment;

import static  com.example.alex.pluggedin.constants.Constants.*;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    private ReviewFragment reviewFragment;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        reviewFragment = ReviewFragment.getInstamce();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case TAB_REVIEW:
                return reviewFragment;
        }

        return null;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return "Обзоры";
    }

    @Override
    public int getCount() {
        return 1;//!!!!!!!!!!!!!!!!!!!!
    }
}
