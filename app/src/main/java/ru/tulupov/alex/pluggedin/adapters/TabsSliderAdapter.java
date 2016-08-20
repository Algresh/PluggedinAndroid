package ru.tulupov.alex.pluggedin.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ru.tulupov.alex.pluggedin.fragments.SlideFragment;
import ru.tulupov.alex.pluggedin.models.Slide;

public class TabsSliderAdapter extends FragmentPagerAdapter {

    private List<Slide> slidesData;
    private List<SlideFragment> slideViews;

    public TabsSliderAdapter(FragmentManager fm, List<Slide> slides) {
        super(fm);
        this.slidesData = slides;
        slideViews = new ArrayList<>(slides.size());

        for(Slide slide : slides) {
            SlideFragment fragment = SlideFragment.getInstance(slide);
            slideViews.add(fragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return slideViews.get(position);
    }

    @Override
    public int getCount() {
        return slidesData.size();
    }
}
