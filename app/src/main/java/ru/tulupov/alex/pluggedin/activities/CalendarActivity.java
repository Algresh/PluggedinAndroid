package ru.tulupov.alex.pluggedin.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.pluggedin.R;
import com.squareup.picasso.Picasso;

import ru.tulupov.alex.pluggedin.adapters.TabsPagerCalendarAdapter;
import ru.tulupov.alex.pluggedin.constants.Constants;
import ru.tulupov.alex.pluggedin.fragments.views.SliderView;
import ru.tulupov.alex.pluggedin.models.Slide;
import ru.tulupov.alex.pluggedin.presenters.SliderPresenter;

public class CalendarActivity extends BaseActivity implements SliderView {

    protected ImageView imageSlide;
    protected TextView textSlide;
    protected SliderPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initToolbar("", R.id.toolbarCalendar);
        initNavigationView();
        initTabs();

        imageSlide = (ImageView) findViewById(R.id.imageSlide);
        textSlide = (TextView) findViewById(R.id.sliderText);
        presenter = new SliderPresenter(this);
        presenter.downloadSlide(0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    @Override
    public void setupSlide(Slide slide) {
        Picasso.with(this).load(Constants.URL_IMAGES + slide.getFile())
                .into(imageSlide);
        textSlide.setText(slide.getText());
    }

    @Override
    public void failDownloading() {

    }
}
