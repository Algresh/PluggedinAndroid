package com.example.alex.pluggedin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.alex.pluggedin.fragments.ShowBaseFragment;
import com.example.alex.pluggedin.fragments.ShowReviewFragment;
import static com.example.alex.pluggedin.constants.Constants.*;

public class ShowReviewActivity extends AppCompatActivity
        implements ShowBaseFragment.ChangeableTitle {

    private Toolbar toolbar;
    private int idReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_review);
        idReview = getIntent().getIntExtra(ID_REVIEW, 0);
        if (idReview == 0) {
            finish();
        }

        initToolBar();
        initFragment();
    }

    private void initFragment() {
        ShowReviewFragment fragment = ShowReviewFragment.newInstance(idReview);
        getFragmentManager().beginTransaction().add(R.id.showReviewFragmentContainer, fragment).commit();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarShowReview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.copyLinkItem:
//                copyLink();
                break;
            case R.id.shareItem:
//                shareLink();
                break;
            case R.id.openInBrowserItem:
//                openArticleInBrowser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void changeTitleInToolbar(String title) {
        toolbar.setTitle(title);
    }
}
