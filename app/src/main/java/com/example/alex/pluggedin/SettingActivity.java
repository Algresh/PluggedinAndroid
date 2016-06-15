package com.example.alex.pluggedin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.Toast;

import static com.example.alex.pluggedin.constants.Constants.*;
public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    SwitchCompat switchNotifyPermission;
    SharedPreferences sharedPreferences;
    SwitchCompat switchSoundPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        String title = getResources().getString(R.string.settings);
        initToolbar(title, R.id.toolbarSettings);
        initNavigationView();

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);


        switchNotifyPermission = (SwitchCompat) findViewById(R.id.switchNotifyPermission);
        switchNotifyPermission.setOnCheckedChangeListener(this);

        switchSoundPermission = (SwitchCompat) findViewById(R.id.switchSoundPermission);
        switchSoundPermission.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchNotifyPermission:
                setPermissionNotify(isChecked);
                break;
            case R.id.switchSoundPermission:
                setPermissionSound(isChecked);
                break;
        }
    }

    private void setPermissionNotify(boolean permission) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_SENT_NOTIFY_PERMISSION, permission);
        editor.apply();
        String str;
        if (permission) {
            str = getResources().getString(R.string.onNotification);
        } else {
            str = getResources().getString(R.string.offNotification);
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void setPermissionSound(boolean permission) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_SOUND_NOTIFY_PERMISSION, permission);
        editor.apply();
        String str;
        if (permission) {
            str = getResources().getString(R.string.onSound);
        } else {
            str = getResources().getString(R.string.offSound);
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
