package com.example.alex.pluggedin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.alex.pluggedin.fragments.ShowReviewFragment;
import static com.example.alex.pluggedin.constants.Constants.*;

public class ShowReviewActivity extends ShowBaseActivity {

    private int idReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_review);
        idReview = getIntent().getIntExtra(ID_REVIEW, 0);
        if (idReview == 0) {
            finish();
        }

        initToolBar(R.id.toolbarShowReview);
        initFragment();
    }

    private void initFragment() {
        ShowReviewFragment fragment = ShowReviewFragment.newInstance(idReview);
        getFragmentManager().beginTransaction().add(R.id.showReviewFragmentContainer, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.copyLinkItem:
                copyLink(latinTitle, URL_OPEN_REVIEW);
                break;
            case R.id.shareItem:
                shareLink(latinTitle, URL_OPEN_REVIEW);
                break;
            case R.id.openInBrowserItem:
                openArticleInBrowser(latinTitle, URL_OPEN_REVIEW);
                break;
            case R.id.commentItem:
                addComment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
