package com.example.alex.pluggedin;

import android.content.Intent;
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
import com.example.alex.pluggedin.models.Article;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.alex.pluggedin.constants.Constants.*;

public class SearchResultsActivity extends AppCompatActivity implements View.OnClickListener{

    ArticleAPI articleAPI;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        query = getIntent().getStringExtra(SEARCH_QUERY);
        initToolbar();
        connectNetwork();

    }

    @Override
    public void onClick(View v) {
        /**
         * check review or article
         */
        Intent intent = new Intent(this, ShowArticleActivity.class);
        int idArticle = (Integer) v.findViewById(R.id.cardView).getTag();
        intent.putExtra(ID, idArticle);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void connectNetwork() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(DOMAIN).build();
        articleAPI = restAdapter.create(ArticleAPI.class);
        articleAPI.getListArticlesBySearch(query, getCallable());
    }

    private Callback<List<Article>> getCallable(){
        return new Callback<List<Article>>() {
            @Override
            public void success(List<Article> articles, Response response) {
                if(articles != null) {
                    successSearch(articles);
                } else {
                    successFail();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                successFail();
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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleViewSearch);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new ArticleAdapter(articles, this, this));
        }
    }

    private void successFail(){
        TextView searchEmpty = (TextView) findViewById(R.id.search_empty);
        if (searchEmpty != null) {
            searchEmpty.setVisibility(View.VISIBLE);
        }
    }
}
