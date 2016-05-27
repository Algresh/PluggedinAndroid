package com.example.alex.pluggedin.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.alex.pluggedin.fragments.ArticleFragment;
import com.example.alex.pluggedin.fragments.ReviewFragment;

import static  com.example.alex.pluggedin.constants.Constants.*;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    private String[] tabs;

    private ReviewFragment reviewFragment;
    private ArticleFragment newsFragment;
    private ArticleFragment interestingFragment;
    private ArticleFragment mediaFragment;
    private ArticleFragment articleFragment;

    public TabsPagerAdapter(FragmentManager fm, String[] tabsTitle) {
        super(fm);
        reviewFragment = ReviewFragment.getInstance();
        newsFragment = ArticleFragment.getInstance(TYPE_NEWS);
        interestingFragment = ArticleFragment.getInstance(TYPE_INTERESTING);
        mediaFragment = ArticleFragment.getInstance(TYPE_MEDIA);
        articleFragment = ArticleFragment.getInstance(TYPE_ARTICLE);

        tabs = tabsTitle;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case TAB_REVIEW:
                return reviewFragment;
            case TAB_NEWS:
                return newsFragment;
            case TAB_INTERESTING:
                return interestingFragment;
            case TAB_ARTICLE :
                return articleFragment;
            case TAB_MEDIA:
                return mediaFragment;

        }

        return null;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
