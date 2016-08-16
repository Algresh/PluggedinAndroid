package ru.tulupov.alex.pluggedin.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.alex.pluggedin.R;

import ru.tulupov.alex.pluggedin.adapters.TabsPagerCalendarAdapter;

public class CalendarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        String title = getResources().getString(R.string.calendar);
        initToolbar(title, R.id.toolbarCalendar);
        initNavigationView();
        initTabs();

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
        TabsPagerCalendarAdapter adapter = new TabsPagerCalendarAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

}
