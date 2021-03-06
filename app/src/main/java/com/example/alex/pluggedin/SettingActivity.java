package com.example.alex.pluggedin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.alex.pluggedin.fragments.ChoiceFontDialogFragment;

import static com.example.alex.pluggedin.constants.Constants.*;
public class SettingActivity extends BaseActivity
        implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    SwitchCompat switchNotifyPermission;
    SharedPreferences sharedPreferences;
    SwitchCompat switchSoundPermission;
    SwitchCompat switchChromeTabsPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        String title = getResources().getString(R.string.settings);
        initToolbar(title, R.id.toolbarSettings);
        initNavigationView();

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        boolean permissionNotify =
                sharedPreferences.getBoolean(APP_PREFERENCES_SENT_NOTIFY_PERMISSION, true);

        boolean permissionSound =
                sharedPreferences.getBoolean(APP_PREFERENCES_SOUND_NOTIFY_PERMISSION, false);

        boolean permissionChromeCustomTabs =
                sharedPreferences.getBoolean(APP_PREFERENCES_CHROME_TABS, false);

        switchNotifyPermission = (SwitchCompat) findViewById(R.id.switchNotifyPermission);
        if (switchNotifyPermission != null) {
            switchNotifyPermission.setChecked(permissionNotify);
            switchNotifyPermission.setOnCheckedChangeListener(this);
        }
        switchSoundPermission = (SwitchCompat) findViewById(R.id.switchSoundPermission);
        if (switchSoundPermission != null) {
            switchSoundPermission.setChecked(permissionSound);
            switchSoundPermission.setOnCheckedChangeListener(this);
        }
        switchChromeTabsPermission = (SwitchCompat) findViewById(R.id.switchChromeTabs);
        if (switchChromeTabsPermission != null) {
            switchChromeTabsPermission.setChecked(permissionChromeCustomTabs);
            switchChromeTabsPermission.setOnCheckedChangeListener(this);
        }

        findViewById(R.id.fontSize).setOnClickListener(this);
        findViewById(R.id.emailDevelopers).setOnClickListener(this);

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
            case R.id.switchChromeTabs:
                setPermissionChromeTabs(isChecked);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fontSize) {
            FragmentManager manager = getSupportFragmentManager();
            ChoiceFontDialogFragment dialog = new ChoiceFontDialogFragment();
            dialog.show(manager, "dialog");
        } else {
            if (v.getId() == R.id.emailDevelopers) {
                String email = getResources().getString(R.string.emailDevelopers);
                sendEmailToDevelopers(email);
            }
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

    private void setPermissionChromeTabs(boolean permission) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_CHROME_TABS, permission);
        editor.apply();
        String str;
        if (permission) {
            str = getResources().getString(R.string.chromeTabsOn);
        } else {
            str = getResources().getString(R.string.chromeTabsOff);
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void sendEmailToDevelopers(String email) {
        Intent sendEmail = new Intent(Intent.ACTION_SEND);
        sendEmail.setType("message/rfc822");
        sendEmail.putExtra(Intent.EXTRA_EMAIL, email);
        sendEmail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(sendEmail);
        }catch (android.content.ActivityNotFoundException e) {
            String noApp =  getResources().getString(R.string.noEmailApps);
            Toast.makeText(this, noApp, Toast.LENGTH_SHORT).show();
        }


    }
}
