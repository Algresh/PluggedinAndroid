package ru.tulupov.alex.pluggedin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.alex.pluggedin.R;

import ru.tulupov.alex.pluggedin.fragments.ShowBaseFragment;

import ru.tulupov.alex.pluggedin.constants.Constants;


public class ShowBaseActivity extends AppCompatActivity
        implements ShowBaseFragment.ChangeableTitle {

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
            clipboardManager.setPrimaryClip(ClipData.newPlainText(Constants.CLIP_LABEL, urlOpen + title));
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
            intentShare.setType(Constants.TEXT_PLAIN);

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

    protected void addComment () {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(Constants.LATIN_TITLE, latinTitle);
        startActivity(intent);
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
