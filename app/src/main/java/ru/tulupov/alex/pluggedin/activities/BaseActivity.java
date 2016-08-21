package ru.tulupov.alex.pluggedin.activities;

import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;

import ru.tulupov.alex.pluggedin.R;
import static ru.tulupov.alex.pluggedin.constants.Constants.*;


public class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected Toolbar toolbar;


    private  NavigationView.OnNavigationItemSelectedListener selectMenuNavigationView() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                Intent intent;

                switch (item.getItemId()){
                    case R.id.listOfArticlesItem:
                        intent = new Intent(BaseActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.settingsItem:
                        intent = new Intent(BaseActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.calendarItem:
                        intent = new Intent(BaseActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.socialNetworksVk:
                        openSocialNetworks(URL_VK_GROUP);
                        break;
                    case R.id.socialNetworksInstagram:
                        openSocialNetworks(URL_INSTAGRAM_ACCOUNT);
                        break;
                    case R.id.socialNetworksYoutube:
                        openSocialNetworks(URL_YOUTUBE_CHANEL);
                        break;
                }
                return true;
            }
        };
    }

    protected void initToolbar (String title, int idToolbar) {
        toolbar = (Toolbar) findViewById(idToolbar);
        if(toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
        }
    }

    protected void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(selectMenuNavigationView());
    }

    protected boolean checkConnection() {
        ConnectivityManager connectChecker = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectChecker.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = connectChecker.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void openSocialNetworks(String srcUri) {
        Uri uri = Uri.parse(srcUri);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    protected int getWidthScreen() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }


}
