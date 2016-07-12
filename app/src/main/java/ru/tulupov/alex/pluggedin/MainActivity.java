package ru.tulupov.alex.pluggedin;

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

import ru.tulupov.alex.pluggedin.API.FcmAPI;

import com.example.alex.pluggedin.R;

import ru.tulupov.alex.pluggedin.adapters.TabsPagerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static ru.tulupov.alex.pluggedin.constants.Constants.*;


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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(MY_TAG, "onNewIntent");
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
            String token = null;

            if (checkPlayService()) {
                token =  FirebaseInstanceId.getInstance().getToken();
                Log.d(MY_TAG, "token is: " + token);
            }

            SharedPreferences.Editor editor = mSettings.edit();
            //разрешение на уведомления
            if( mSettings.getBoolean(APP_PREFERENCES_SENT_NOTIFY_PERMISSION, true) ){
                editor.putBoolean(APP_PREFERENCES_SENT_NOTIFY_PERMISSION, true);
            }
            //разрешение на звук уведомления
            if( mSettings.getBoolean(APP_PREFERENCES_SOUND_NOTIFY_PERMISSION, false) ){
                editor.putBoolean(APP_PREFERENCES_SOUND_NOTIFY_PERMISSION, false);
            }

            //разрешение chrome tabs
            if( mSettings.getBoolean(APP_PREFERENCES_CHROME_TABS, false) ){
                editor.putBoolean(APP_PREFERENCES_CHROME_TABS, false);
            }

            //размер шрифта
            if( mSettings.getFloat(APP_PREFERENCES_FONT_SIZE,  FONT_SIZE_NORMAL) == FONT_SIZE_NORMAL ){
                editor.putFloat(APP_PREFERENCES_FONT_SIZE, FONT_SIZE_NORMAL);
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

    private boolean checkPlayService() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int result = apiAvailability.isGooglePlayServicesAvailable(this);

        if (result != ConnectionResult.SUCCESS) {
            String noGPS = getResources().getString(R.string.googlePlayServicesNotInstall);
            Log.d(MY_TAG, noGPS);
            Toast.makeText(this, noGPS, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}
