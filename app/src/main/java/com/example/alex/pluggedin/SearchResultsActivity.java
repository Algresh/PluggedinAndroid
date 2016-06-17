package com.example.alex.pluggedin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.alex.pluggedin.API.ArticleAPI;
import com.example.alex.pluggedin.adapters.ArticleAdapter;
import com.example.alex.pluggedin.adapters.fontsizes.ChangeableFontSize;
import com.example.alex.pluggedin.adapters.fontsizes.FontSizesParameter;
import com.example.alex.pluggedin.models.Article;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.alex.pluggedin.constants.Constants.*;

public class SearchResultsActivity extends AppCompatActivity implements View.OnClickListener{

    protected ArticleAPI articleAPI;
    protected String query;
    protected RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        query = getIntent().getStringExtra(SEARCH_QUERY);
        if(query != null) {
            connectNetworkSearch(FIRST_PAGE);
        } else {
            query = getIntent().getStringExtra(KEYWORD_QUERY);
            connectNetworkKeyword(FIRST_PAGE);
        }
        initToolbar();
    }

    @Override
    public void onClick(View v) {
        int idArticle = (Integer) v.findViewById(R.id.cardView).getTag();
        if (idArticle > 0) {// если id отрицательное значит это обзор
            Intent intent = new Intent(this, ShowArticleActivity.class);
            intent.putExtra(ID, idArticle);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void connectNetworkKeyword(int pages) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(DOMAIN).build();
        articleAPI = restAdapter.create(ArticleAPI.class);
        articleAPI.getListArticlesByKeyword(query, pages, getCallable());
    }

    private void connectNetworkSearch(int pages) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(DOMAIN).build();
        articleAPI = restAdapter.create(ArticleAPI.class);
        articleAPI.getListArticlesBySearch(query, pages, getCallable());
    }

    private Callback<List<Article>> getCallable() {
        return new Callback<List<Article>>() {
            @Override
            public void success(List<Article> articles, Response response) {
                if(articles != null) {
                    successSearch(articles);
                } else {
                    failSearch();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                failSearch();
            }
        };
    }

    private void initToolbar () {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSearch);
        if (toolbar != null && query != null && !query.isEmpty()) {
            toolbar.setTitle(query);
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void successSearch(List<Article> articles){
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewSearch);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ArticleAdapter adapter = new ArticleAdapter(articles, this, this);
            adapter.setFontSizeParameter(getFontSizeParameter());
            recyclerView.setAdapter(adapter);
        }
    }



    private void failSearch(){
        TextView searchEmpty = (TextView) findViewById(R.id.search_empty);
        if (searchEmpty != null) {
            searchEmpty.setVisibility(View.VISIBLE);
        }
    }

    protected ChangeableFontSize getFontSizeParameter() {
        SharedPreferences pref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        float fontSize = pref.getFloat(APP_PREFERENCES_FONT_SIZE, FONT_SIZE_NORMAL);
        return new FontSizesParameter(fontSize);
    }
}
