package com.example.alex.pluggedin;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.alex.pluggedin.adapters.TabsPagerAdapter;


public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String title = getResources().getString(R.string.categories);
        initToolbar(title, R.id.toolbarReview);
        initNavigationView();
        initTabs();
    }



    private void initTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        String[] tabsTitle = getResources().getStringArray(R.array.tabs_title);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager(), tabsTitle);
        if(viewPager != null && tabLayout != null ) {
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }

    }


}
