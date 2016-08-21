package ru.tulupov.alex.pluggedin.activities;

import android.os.Bundle;
import android.view.MenuItem;

import ru.tulupov.alex.pluggedin.R;

import ru.tulupov.alex.pluggedin.constants.Constants;
import ru.tulupov.alex.pluggedin.fragments.ShowReviewFragment;


public class ShowReviewActivity extends ShowBaseActivity {

    private int idReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_review);
        idReview = getIntent().getIntExtra(Constants.ID_REVIEW, 0);
        if (idReview == 0) {
            finish();
        }

        initToolBar(R.id.toolbarShowReview);
        initFragment(savedInstanceState);
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            ShowReviewFragment fragment = ShowReviewFragment.newInstance(idReview);
            getFragmentManager().beginTransaction().add(R.id.showReviewFragmentContainer, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.copyLinkItem:
                copyLink(latinTitle, Constants.URL_OPEN_REVIEW);
                break;
            case R.id.shareItem:
                shareLink(latinTitle, Constants.URL_OPEN_REVIEW);
                break;
            case R.id.openInBrowserItem:
                openArticleInBrowser(latinTitle, Constants.URL_OPEN_REVIEW);
                break;
            case R.id.commentItem:
                addComment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
