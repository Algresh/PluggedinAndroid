package com.example.alex.pluggedin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.pluggedin.API.ArticleAPI;
import com.example.alex.pluggedin.models.Article;
import com.example.alex.pluggedin.models.Keyword;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    protected TextView dateTv;
    protected Article article;
    protected WebView textWV;
    protected FlowLayout layoutKeywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);
        idArticle = getIntent().getIntExtra(ID, -1);

        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(DOMAIN).build();
        articleAPI = adapter.create(ArticleAPI.class);

        initToolbar();
        initViewsForArticle();

        getArticleById(idArticle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void getArticleById (int idArticle) {
        articleAPI.getOpenArticle(idArticle, new Callback<List<Article>>() {
            @Override
            public void success(List<Article> articles, Response response) {
                article = articles.get(0);
                initFields();
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    protected void initFields() {
        titleTv.setText(article.getTitle());
        authorTv.setText(article.getAuthor());

        dateTv.setText(article.getDatePublish());

        LayoutInflater layoutInflater = getLayoutInflater();

        for(Keyword word: article.getKeywords()) {
            View view = layoutInflater.inflate(R.layout.keyword, layoutKeywords, false);
            TextView keyWord = (TextView) view.findViewById(R.id.keywordTV);
            keyWord.setText(word.getTitle());
            layoutKeywords.addView(view);
        }
    }

    protected void initViewsForArticle() {
        titleTv = (TextView) findViewById(R.id.showArticleTitle);
        authorTv = (TextView) findViewById(R.id.openArticleAuthor);
        textWV = (WebView) findViewById(R.id.webViewTextOpen);
        dateTv = (TextView) findViewById(R.id.openArticleDate);
        layoutKeywords = (FlowLayout) findViewById(R.id.layoutKeywords);

        textWV.getSettings().setJavaScriptEnabled(true);
        textWV.loadUrl(URL_TEXT_ARTICLE + idArticle);

        textWV.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {//!!!!!!!!!!!!!!!!!!!!!!!!

                if (url.contains(DOMAIN)) {
                    showArticleByURL(url);
                    return true;
                } else  {
                    Uri address = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, address);
                    startActivity(intent);
                    return true;
                }

            }
        });
    }


    protected void showArticleByURL(String url) {
        String[] arrUrl = url.split("/");
        String latinTitle = arrUrl[arrUrl.length - 1];
        latinTitle = convertHexSubStringsToNormalString(latinTitle);

        articleAPI.getArticleIdByLatinTitle(latinTitle, new Callback<Response>() {
            @Override
            public void success(Response response, Response another) {
                InputStream inputStream;
                int idArticle;
                try {
                    inputStream = response.getBody().in();
                    idArticle = convertBytesArray(inputStream);

                    if(idArticle > 0) {
                        Intent intent = new Intent(ShowArticleActivity.this, ShowArticleActivity.class);
                        intent.putExtra(ID, idArticle);
                        startActivity(intent);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ShowArticleActivity.this, SOMETHING_DOESNT_WORK, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarShowArticle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    protected int convertBytesArray(InputStream inputStream) throws IOException {
        int idArticle = 0;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int read;
            while ((read = inputStream.read()) != -1) {
                bos.write(read);
            }
            byte[] result = bos.toByteArray();
            bos.close();
            String data = new String(result);
            JSONObject jsonObj = new JSONObject(data);
            idArticle = jsonObj.getInt(ID_ARTICLE);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
        }

        return idArticle;
    }

    protected String convertHexSubStringsToNormalString(String latinTitle) {
        /**
         * @TODO:  create function for convert hex digit to String (or char)
         */
        latinTitle = latinTitle.replace("%C2%AB", "«");//!
        latinTitle = latinTitle.replace("%C2%BB", "»");//!
        latinTitle = latinTitle.replace("%22", "\"");//!
        latinTitle = latinTitle.replace("%27", "\'");//!
        latinTitle = latinTitle.replace("%E2%80%94", "—");//!
        return latinTitle;

    }
}
