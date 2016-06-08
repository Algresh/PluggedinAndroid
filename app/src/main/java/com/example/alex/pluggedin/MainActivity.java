package com.example.alex.pluggedin;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import static com.example.alex.pluggedin.constants.Constants.*;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem =  menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        if(searchManager != null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(getOnQueryTextListener());

        return true;
    }

    private SearchView.OnQueryTextListener getOnQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                intent.putExtra(SEARCH_QUERY, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
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
