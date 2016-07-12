package ru.tulupov.alex.pluggedin.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ru.tulupov.alex.pluggedin.fragments.ArticleFragment;
import ru.tulupov.alex.pluggedin.fragments.ReviewFragment;

import ru.tulupov.alex.pluggedin.constants.Constants;


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
        newsFragment = ArticleFragment.getInstance(Constants.TYPE_NEWS);
        interestingFragment = ArticleFragment.getInstance(Constants.TYPE_INTERESTING);
        mediaFragment = ArticleFragment.getInstance(Constants.TYPE_MEDIA);
        articleFragment = ArticleFragment.getInstance(Constants.TYPE_ARTICLE);

        tabs = tabsTitle;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case Constants.TAB_REVIEW:
                return reviewFragment;
            case Constants.TAB_NEWS:
                return newsFragment;
            case Constants.TAB_INTERESTING:
                return interestingFragment;
            case Constants.TAB_ARTICLE :
                return articleFragment;
            case Constants.TAB_MEDIA:
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
