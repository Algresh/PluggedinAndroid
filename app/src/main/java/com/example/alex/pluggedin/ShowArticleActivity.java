package com.example.alex.pluggedin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
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


public class ShowArticleActivity extends AppCompatActivity implements View.OnClickListener {

    protected ArticleAPI articleAPI;
    protected int idArticle;
    protected Article article;

    protected TextView titleTv;
    protected TextView authorTv;
    protected TextView dateTv;
    protected WebView textWV;
    protected FlowLayout layoutKeywords;
    protected Button tryAgainBtn;
    protected Toolbar toolbar;

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openArticleTryAgainBtn:
                getArticleById(idArticle);
                break;
            default:
                TextView textView = (TextView) v.findViewById(R.id.keywordTV);
                if(textView != null) {
                    String keyword = textView.getText().toString();
                    Intent intent = new Intent(this, SearchResultsActivity.class);
                    intent.putExtra(KEYWORD_QUERY, keyword);
                    startActivity(intent);
                }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.copyLinkItem:
                copyLink();
                break;
            case R.id.shareItem:
                shareLink();
                break;
            case R.id.openInBrowserItem:
                openArticleInBrowser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void copyLink() {
        String title = article.getLatinTitle();
        if (title != null) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            final String successSaved = getResources().getString(R.string.savedToBuffer);
            clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    Toast.makeText(ShowArticleActivity.this, successSaved, Toast.LENGTH_SHORT).show();
                }
            });
            clipboardManager.setPrimaryClip(ClipData.newPlainText(CLIP_LABEL, URL_OPEN_ARTICLE + title));
        }
    }

    protected void openArticleInBrowser() {
        String title = article.getLatinTitle();
        if (title != null) {
            Uri uri = Uri.parse(URL_OPEN_ARTICLE + title);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    protected void shareLink () {
        String title = article.getLatinTitle();
        if (title != null) {
            Intent intentShare = new Intent(Intent.ACTION_SEND);
            intentShare.setType(TEXT_PLAIN);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intentShare.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            } else {
                intentShare.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            }

            String share = getResources().getString(R.string.share);
            intentShare.putExtra(Intent.EXTRA_TEXT, URL_OPEN_ARTICLE + title);
            startActivity(Intent.createChooser(intentShare, share));

        }
    }



    protected void getArticleById (int idArticle) {
        articleAPI.getOpenArticle(idArticle, new Callback<List<Article>>() {
            @Override
            public void success(List<Article> articles, Response response) {
                article = articles.get(0);
                initFields();
                showAllElementHideBtn();
            }

            @Override
            public void failure(RetrofitError error) {
                hideAllElementsShowBtn();
                Toast.makeText(ShowArticleActivity.this, SOMETHING_DOESNT_WORK, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void initFields() {
        textWV.loadUrl(URL_TEXT_ARTICLE + idArticle);
        titleTv.setText(article.getTitle());
        authorTv.setText(article.getAuthor());
        dateTv.setText(article.getDatePublish());
        toolbar.setTitle(article.getTitle());

        LayoutInflater layoutInflater = getLayoutInflater();

        for(Keyword word: article.getKeywords()) {
            View view = layoutInflater.inflate(R.layout.keyword, layoutKeywords, false);
            view.setOnClickListener(this);
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
        tryAgainBtn = (Button) findViewById(R.id.openArticleTryAgainBtn);
        if (tryAgainBtn != null) {
            tryAgainBtn.setOnClickListener(this);
        }

        textWV.getSettings().setJavaScriptEnabled(true);
        textWV.addJavascriptInterface(new MyJavaInterface(), "test");

        textWV.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(DOMAIN)) {
                    showArticleByURL(url);
                } else  {
                    Uri address = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, address);
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                hideAllElementsShowBtn();
                Toast.makeText(ShowArticleActivity.this, SOMETHING_DOESNT_WORK, Toast.LENGTH_SHORT).show();
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
        toolbar = (Toolbar) findViewById(R.id.toolbarShowArticle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_show_article, menu);
        return true;
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
        latinTitle = latinTitle.replace("%C2%AB", "«");//!
        latinTitle = latinTitle.replace("%C2%BB", "»");//!
        latinTitle = latinTitle.replace("%22", "\"");//!
        latinTitle = latinTitle.replace("%27", "\'");//!
        latinTitle = latinTitle.replace("%E2%80%94", "—");//!
        return latinTitle;

    }

    protected void hideAllElementsShowBtn() {
        showOrHideElements(View.GONE);
    }

    protected void showAllElementHideBtn() {
        showOrHideElements(View.VISIBLE);
    }

    protected void showOrHideElements(int visibility) {
         titleTv.setVisibility(visibility);
         authorTv.setVisibility(visibility);
         dateTv.setVisibility(visibility);
         textWV.setVisibility(visibility);
         layoutKeywords.setVisibility(visibility);
         int oppositeVisibility = visibility == View.GONE ? View.VISIBLE : View.GONE;
         tryAgainBtn.setVisibility(oppositeVisibility);
    }

    private class MyJavaInterface {
        @android.webkit.JavascriptInterface
        public void getGreeting(String str) {
            Intent intent = new Intent(ShowArticleActivity.this, ShowImageActivity.class);
            intent.putExtra(SRC_OF_IMAGE, str);
            startActivity(intent);
        }
    }


}
