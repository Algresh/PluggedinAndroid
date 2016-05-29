package com.example.alex.pluggedin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.alex.pluggedin.API.ArticleAPI;
import com.example.alex.pluggedin.models.Article;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static  com.example.alex.pluggedin.constants.Constants.*;


public class ShowArticleActivity extends AppCompatActivity {

    protected ArticleAPI articleAPI;
    protected int idArticle;

    protected TextView titleTv;
    protected TextView authorTv;
    protected Article article;
    protected WebView textWV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarShowArticle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titleTv = (TextView) findViewById(R.id.showArticleTitle);
        authorTv = (TextView) findViewById(R.id.openArticleAuthor);
        textWV = (WebView) findViewById(R.id.webViewTextOpen);

        idArticle = getIntent().getIntExtra("id", -1);
        getArticleById(idArticle);

    }

    protected void getArticleById (int idArticle) {
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(DOMAIN).build();
        articleAPI = adapter.create(ArticleAPI.class);
        articleAPI.getOpenArticle(idArticle, new Callback<List<Article>>() {
            @Override
            public void success(List<Article> articles, Response response) {
                article = articles.get(0);
                initFields();
            }

            @Override
            public void failure(RetrofitError error) {
                int y = 0;
            }
        });
    }

    protected void initFields() {
        titleTv.setText(article.getTitle());
        authorTv.setText(article.getAuthor());
        textWV.getSettings().setJavaScriptEnabled(true);
        textWV.loadUrl(URL_TEXT_ARTICLE + idArticle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
