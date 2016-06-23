package com.example.alex.pluggedin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.alex.pluggedin.fragments.ShowBaseFragment;
import com.example.alex.pluggedin.models.Article;

import static com.example.alex.pluggedin.constants.Constants.CLIP_LABEL;
import static com.example.alex.pluggedin.constants.Constants.TEXT_PLAIN;
import static com.example.alex.pluggedin.constants.Constants.URL_OPEN_ARTICLE;


public class ShowBaseActivity extends AppCompatActivity
        implements ShowBaseFragment.ChangeableTitle{

    protected String latinTitle;
    protected Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_show_article, menu);
        return true;
    }



    protected void copyLink(String title, String urlOpen) {
        if (title != null) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            final String successSaved = getResources().getString(R.string.savedToBuffer);
            clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    Toast.makeText(ShowBaseActivity.this, successSaved, Toast.LENGTH_SHORT).show();
                }
            });
            clipboardManager.setPrimaryClip(ClipData.newPlainText(CLIP_LABEL, urlOpen + title));
        }
    }

    protected void openArticleInBrowser(String title, String urlOpen) {
        if (title != null) {
            Uri uri = Uri.parse(urlOpen + title);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    protected void shareLink (String title, String urlOpen) {
        if (title != null) {
            Intent intentShare = new Intent(Intent.ACTION_SEND);
            intentShare.setType(TEXT_PLAIN);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intentShare.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            } else {
                intentShare.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            }

            String share = getResources().getString(R.string.share);
            intentShare.putExtra(Intent.EXTRA_TEXT, urlOpen + title);
            startActivity(Intent.createChooser(intentShare, share));

        }
    }

    protected void initToolBar(int idToolbar) {
        toolbar = (Toolbar) findViewById(idToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void changeTitleInToolbar(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void setLatinTitle(String latinTitle) {
        this.latinTitle = latinTitle;
    }

}
