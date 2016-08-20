package ru.tulupov.alex.pluggedin.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.pluggedin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import ru.tulupov.alex.pluggedin.adapters.TabsPagerCalendarAdapter;
import ru.tulupov.alex.pluggedin.adapters.TabsSliderAdapter;
import ru.tulupov.alex.pluggedin.constants.Constants;
import ru.tulupov.alex.pluggedin.fragments.SlideFragment;
import ru.tulupov.alex.pluggedin.fragments.views.SliderView;
import ru.tulupov.alex.pluggedin.models.Slide;
import ru.tulupov.alex.pluggedin.presenters.SliderPresenter;

public class CalendarActivity extends BaseActivity implements SliderView,
        SlideFragment.ClickSlideImageListener {

    protected SliderPresenter presenter;
    private TextView[] dots;
    private List<Slide> sliderData;
    private LinearLayout layoutDots;
    private TextView dotActive;

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

        presenter = new SliderPresenter(this);
        presenter.downloadSlider();

        layoutDots = (LinearLayout) findViewById(R.id.layoutDots);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void initSlider(List<Slide> slides) {
        this.sliderData = slides;
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerSlider);

        TabsSliderAdapter adapter = new TabsSliderAdapter(getSupportFragmentManager(), slides);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
        addSliderDots(0);
    }

    @Override
    public void failDownloading() {

    }

    @Override
    public void onClickSlideImage(int position) {

    }

    protected void initTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerCalendar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayoutCalendar);

        String[] tabTitles = getResources().getStringArray(R.array.tab_titles_calendar);
        TabsPagerCalendarAdapter adapter =
                new TabsPagerCalendarAdapter(getSupportFragmentManager(), tabTitles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(Constants.TAB_FILMS).setIcon(R.drawable.filmstrip);
        tabLayout.getTabAt(Constants.TAB_GAMES).setIcon(R.drawable.gamepad_variant);
    }

    protected void addSliderDots(int currentPage) {
        dots = new TextView[sliderData.size()];

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
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
}
