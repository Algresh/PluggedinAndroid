package ru.tulupov.alex.pluggedin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
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
import ru.tulupov.alex.pluggedin.fragments.CalendarFragment;
import ru.tulupov.alex.pluggedin.fragments.SlideFragment;
import ru.tulupov.alex.pluggedin.fragments.views.SliderView;
import ru.tulupov.alex.pluggedin.models.Slide;
import ru.tulupov.alex.pluggedin.presenters.SliderPresenter;

import static ru.tulupov.alex.pluggedin.constants.Constants.ID;
import static ru.tulupov.alex.pluggedin.constants.Constants.ID_REVIEW;
import static ru.tulupov.alex.pluggedin.constants.Constants.MY_TAG;

public class CalendarActivity extends BaseActivity implements SliderView,
        SlideFragment.ClickSlideImageListener, CalendarFragment.NoConnectable {

    protected SliderPresenter presenter;
    private TextView[] dots;
    private List<Slide> sliderData;
    private LinearLayout layoutDots;
    private TextView dotActive;
    private ViewPager viewPager;

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

        viewPager = (ViewPager) findViewById(R.id.viewPagerSlider);
        layoutDots = (LinearLayout) findViewById(R.id.layoutDots);
        presenter = new SliderPresenter(this);
        presenter.downloadSlider();

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
        flagSliderIsDownloaded = true;

        viewPager.setVisibility(View.VISIBLE);
        layoutDots.setVisibility(View.VISIBLE);

        TabsSliderAdapter adapter = new TabsSliderAdapter(getSupportFragmentManager(), slides);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
        addSliderDots(0);
    }

    @Override
    public void failDownloading() {
        viewPager.setVisibility(View.GONE);
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

    @Override
    public void tryAccessAgain() {
        if (!flagSliderIsDownloaded ) {
            presenter.downloadSlider();
        }
    }
}
