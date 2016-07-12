package ru.tulupov.alex.pluggedin;


import android.os.Bundle;

import android.view.MenuItem;

import com.example.alex.pluggedin.R;

import ru.tulupov.alex.pluggedin.fragments.ShowArticleFragment;
import static ru.tulupov.alex.pluggedin.constants.Constants.*;


public class ShowArticleActivity extends ShowBaseActivity {
    private int idArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);
        idArticle = getIntent().getIntExtra(ID, 0);
        if (idArticle == 0) {
            finish();
        }

        initToolBar(R.id.toolbarShowArticle);
        initFragment(savedInstanceState);
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            ShowArticleFragment fragment = ShowArticleFragment.newInstance(idArticle);
            getFragmentManager().beginTransaction().add(R.id.showArticleFragmentContainer, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.copyLinkItem:
                copyLink(latinTitle, URL_OPEN_ARTICLE);
                break;
            case R.id.shareItem:
                shareLink(latinTitle, URL_OPEN_ARTICLE);
                break;
            case R.id.openInBrowserItem:
                openArticleInBrowser(latinTitle, URL_OPEN_ARTICLE);
                break;
            case R.id.commentItem:
                addComment();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
