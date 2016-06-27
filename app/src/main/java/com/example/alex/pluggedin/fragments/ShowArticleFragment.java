package com.example.alex.pluggedin.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.pluggedin.API.ArticleAPI;
import com.example.alex.pluggedin.API.ReviewAPI;
import com.example.alex.pluggedin.R;
import com.example.alex.pluggedin.SearchResultsActivity;
import com.example.alex.pluggedin.ShowArticleActivity;
import com.example.alex.pluggedin.ShowImageActivity;
import com.example.alex.pluggedin.chrome.CustomTabActivityHelper;
import com.example.alex.pluggedin.models.Article;
import com.example.alex.pluggedin.models.Keyword;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.alex.pluggedin.constants.Constants.*;

public class ShowArticleFragment extends ShowBaseFragment{

    protected Article article;
    private ArticleAPI articleAPI;


    public static ShowArticleFragment newInstance(int idArticle) {
        ShowArticleFragment fragment = new ShowArticleFragment();

        Bundle args = new Bundle();
        args.putInt(ID_ARTICLE, idArticle);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_article_fragment, container, false);

        if (container == null) {
            return null;
        }

        idArticle = getArguments().getInt(ID_ARTICLE, 0);

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        fontSize = sharedPreferences.getFloat(APP_PREFERENCES_FONT_SIZE, FONT_SIZE_NORMAL);
        chromeTabsFlag = sharedPreferences.getBoolean(APP_PREFERENCES_CHROME_TABS, false);

        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(DOMAIN).build();
        articleAPI = adapter.create(ArticleAPI.class);

        initViewsForArticle(view);

        getArticleById(idArticle);

        return view;
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
                String str;
                if (checkConnection()) {
                    str = getActivity().getResources()
                            .getString(R.string.something_doesnt_work);
                } else {
                    str = getActivity().getResources()
                            .getString(R.string.no_internet);
                }
                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void changeFontSize(List<TextView> keywords) {
        try {
            if(fontSize != FONT_SIZE_NORMAL) {
                titleTv.setTextSize(titleTv.getTextSize() * fontSize) ;
                authorTv.setTextSize(authorTv.getTextSize() * fontSize);
                dateTv.setTextSize(dateTv.getTextSize() * fontSize);

                for(TextView tv: keywords) {
                    tv.setTextSize(tv.getTextSize() * fontSize);
                }
            }
        } catch (Exception e) {}
    }

    protected void initFields() {
        textWV.loadUrl(URL_TEXT_ARTICLE + idArticle + "/" + fontSize);
        titleTv.setText(article.getTitle());
        authorTv.setText(article.getAuthor());
        dateTv.setText(article.getDatePublish());
        changeableTitle.changeTitleInToolbar(article.getTitle());
        changeableTitle.setLatinTitle(article.getLatinTitle());


        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        List<TextView> list = new ArrayList<>(article.getKeywords().size());

        for(Keyword word: article.getKeywords()) {
            View view = layoutInflater.inflate(R.layout.keyword, layoutKeywords, false);
            view.setOnClickListener(this);
            TextView keyWord = (TextView) view.findViewById(R.id.keywordTV);
            keyWord.setText(word.getTitle());
            layoutKeywords.addView(view);
            list.add(keyWord);
        }
        changeFontSize(list);
    }

    protected void initViewsForArticle(View view) {
        titleTv = (TextView) view.findViewById(R.id.showArticleTitle);
        authorTv = (TextView) view.findViewById(R.id.openArticleAuthor);
        textWV = (WebView) view.findViewById(R.id.webViewTextOpen);
        dateTv = (TextView) view.findViewById(R.id.openArticleDate);
        layoutKeywords = (FlowLayout) view.findViewById(R.id.layoutKeywords);
        tryAgainBtn = (Button) view.findViewById(R.id.openArticleTryAgainBtn);
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

                    if (!chromeTabsFlag) { //открыть ссылку в браузере
                        openLinkInBrowser(address);
                    } else {//открыть ссылку в Chrome Custom Tabs
                        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                        CustomTabActivityHelper.openCustomTab(getActivity(), customTabsIntent, address,
                                new CustomTabActivityHelper.CustomTabFallback() {
                                    @Override
                                    public void openUri(Activity activity, Uri uri) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }
                                });
                    }
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                hideAllElementsShowBtn();
                String str = getActivity().getResources()
                        .getString(R.string.something_doesnt_work);
                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        });

    }


    protected void showArticleByURL(String url) {
        String[] arrUrl = url.split("/");
        String latinTitle = arrUrl[arrUrl.length - 1];
        latinTitle = convertHexSubStringsToNormalString(latinTitle);

        articleAPI.getArticleIdByLatinTitle(latinTitle, getCallbackRedirectLinkToApp());
    }
}
