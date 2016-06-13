package com.example.alex.pluggedin;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import static com.example.alex.pluggedin.constants.Constants.*;

import com.example.alex.pluggedin.API.FcmAPI;
import com.example.alex.pluggedin.adapters.TabsPagerAdapter;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String title = getResources().getString(R.string.categories);
        initToolbar(title, R.id.toolbarReview);
        initNavigationView();
        initTabs();
        initSettings();
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
        searchView.setOnQueryTextListener(getOnQueryTextListener());

        return true;
    }

    private SearchView.OnQueryTextListener getOnQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() > 1) {
                    Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                    intent.putExtra(SEARCH_QUERY, query);
                    startActivity(intent);
                }
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

    private void initSettings() {
        SharedPreferences mSettings;
        mSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        boolean isSend = mSettings.getBoolean(APP_PREFERENCES_TOCEN_IS_SEND, false);

        if (!isSend) {
            String token =  FirebaseInstanceId.getInstance().getToken();
            Log.d(MY_TAG, "token is: " + token);

            SharedPreferences.Editor editor = mSettings.edit();
            if( mSettings.getBoolean(APP_PREFERENCES_SENT_NOTIFY_PERMISSION, true) ){
                editor.putBoolean(APP_PREFERENCES_SENT_NOTIFY_PERMISSION, true);
            }
            if (token != null) {
                editor.putString(APP_PREFERENCES_FCM_TOKEN, token);
                sendRegistrationToServer(token, editor);
            } else {
                editor.apply();
            }
        }
    }

    private void sendRegistrationToServer(String token, final SharedPreferences.Editor editor) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(DOMAIN).build();
        FcmAPI api = restAdapter.create(FcmAPI.class);
        api.registrId(token, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(MY_TAG, "new token was send!!!: ");
                editor.putBoolean(APP_PREFERENCES_TOCEN_IS_SEND, true);
                editor.apply();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(MY_TAG, "fail token");
                editor.putBoolean(APP_PREFERENCES_TOCEN_IS_SEND, false);
                editor.apply();
            }
        });
    }


}
