package ru.tulupov.alex.pluggedin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.tulupov.alex.pluggedin.R;

import java.util.Calendar;
import java.util.List;

import ru.tulupov.alex.pluggedin.adapters.TabsPagerCalendarAdapter;
import ru.tulupov.alex.pluggedin.adapters.TabsSliderAdapter;
import ru.tulupov.alex.pluggedin.constants.Constants;
import ru.tulupov.alex.pluggedin.fragments.CalendarFragment;
import ru.tulupov.alex.pluggedin.fragments.DatePickerCalendarFragment;
import ru.tulupov.alex.pluggedin.fragments.SlideFragment;
import ru.tulupov.alex.pluggedin.fragments.views.SliderView;
import ru.tulupov.alex.pluggedin.models.Slide;
import ru.tulupov.alex.pluggedin.presenters.SliderPresenter;

import static ru.tulupov.alex.pluggedin.constants.Constants.*;

public class CalendarActivity extends BaseActivity implements SliderView,
        SlideFragment.ClickSlideImageListener, CalendarFragment.NoConnectable,
        DatePickerCalendarFragment.DatePickerListener {

    protected SliderPresenter presenter;
    private TextView[] dots;
    private List<Slide> sliderData;
    private LinearLayout layoutDots;
    private TextView dotActive;
    private ViewPager viewPagerSlider;
    private ViewPager viewPagerCalendars;

    protected TabsPagerCalendarAdapter adapter;

    private boolean flagSliderIsDownloaded = false;

    protected ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            changeActiveDot(position);
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initToolbar("", R.id.toolbarCalendar);
        initNavigationView();
        initTabs();

        viewPagerSlider = (ViewPager) findViewById(R.id.viewPagerSlider);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.sliderContainer);
        relativeLayout.getLayoutParams().height = getWidthScreen() / 2;

        layoutDots = (LinearLayout) findViewById(R.id.layoutDots);
        presenter = new SliderPresenter(this);
        presenter.downloadSlider();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerCalendarFragment fragment = new DatePickerCalendarFragment();
                fragment.show(getFragmentManager(), DIALOG_DATE_CALENDAR);
            }
        });
    }

    @Override
    public void initSlider(List<Slide> slides) {
        this.sliderData = slides;
        flagSliderIsDownloaded = true;

        viewPagerSlider.setVisibility(View.VISIBLE);
        layoutDots.setVisibility(View.VISIBLE);

        TabsSliderAdapter adapter = new TabsSliderAdapter(getSupportFragmentManager(), slides);
        viewPagerSlider.setAdapter(adapter);
        viewPagerSlider.addOnPageChangeListener(pageChangeListener);
        addSliderDots(0);
    }

    @Override
    public void failDownloading() {
        viewPagerSlider.setVisibility(View.GONE);
        layoutDots.setVisibility(View.GONE);
    }

    @Override
    public void showArticle(int id) {
        Intent intent = new Intent(this, ShowArticleActivity.class);
        intent.putExtra(ID, id);
        startActivity(intent);
    }

    @Override
    public void showReview(int id) {
        Intent intent = new Intent(this, ShowReviewActivity.class);
        intent.putExtra(ID_REVIEW, id);
        startActivity(intent);
    }

    @Override
    public void failShow() {

    }

    @Override
    public void onClickSlideImage(String link) {
        presenter.clickSlideImage(link);
    }

    protected void initTabs() {
        viewPagerCalendars = (ViewPager) findViewById(R.id.viewPagerCalendar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayoutCalendar);

        String[] tabTitles = getResources().getStringArray(R.array.tab_titles_calendar);
        adapter = new TabsPagerCalendarAdapter(getSupportFragmentManager(), tabTitles);
        viewPagerCalendars.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPagerCalendars);
        tabLayout.getTabAt(Constants.TAB_FILMS).setIcon(R.drawable.filmstrip);
        tabLayout.getTabAt(Constants.TAB_GAMES).setIcon(R.drawable.gamepad_variant);
    }

    protected void addSliderDots(int currentPage) {
        dots = new TextView[sliderData.size()];

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml(DOT_HTML));
            dots[i].setTextSize(50);
            int color;
            if (currentPage == i) {
                color = getResources().getColor(R.color.colorPrimary);
                dotActive = dots[i];
            } else {
                color = getResources().getColor(R.color.white);
            }

            dots[i].setTextColor(color);
            layoutDots.addView(dots[i]);
        }
    }

    protected void changeActiveDot(int newActiveDotNum) {
        int colorRed = getResources().getColor(R.color.colorPrimary);
        int colorWhite = getResources().getColor(R.color.white);

        this.dotActive.setTextColor(colorWhite);
        dots[newActiveDotNum].setTextColor(colorRed);
        this.dotActive = dots[newActiveDotNum];
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void tryAccessAgain() {
        if (!flagSliderIsDownloaded ) {
            presenter.downloadSlider();
        }
    }

    @Override
    public void onDatePicked(Calendar date) {
        try {
            int item = viewPagerCalendars.getCurrentItem();
            CalendarFragment fragment = (CalendarFragment) adapter.getItem(item);
            fragment.onDateSet(date);
        } catch (ClassCastException e) {
            String str = getResources().getString(R.string.something_doesnt_work);
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        }
    }
}
