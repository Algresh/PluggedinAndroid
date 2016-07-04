package com.example.alex.pluggedin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import static com.example.alex.pluggedin.constants.Constants.LATIN_TITLE;
import static com.example.alex.pluggedin.constants.Constants.URL_COMMENT;

public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        String latinTitle = intent.getStringExtra(LATIN_TITLE);

        WebView commentWV = (WebView) findViewById(R.id.commentWebView);
        commentWV.getSettings().setJavaScriptEnabled(true);
        commentWV.loadUrl(URL_COMMENT + latinTitle);

        initToolbar();
    }

    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarComment);
        String title = getResources().getString(R.string.addComment);
        toolbar.setTitle(title);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
