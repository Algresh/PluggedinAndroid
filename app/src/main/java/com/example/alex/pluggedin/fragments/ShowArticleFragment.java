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

public class ShowArticleFragment extends Fragment implements View.OnClickListener{

    protected ArticleAPI articleAPI;
    protected int idArticle;
    protected Article article;

    protected TextView titleTv;
    protected TextView authorTv;
    protected TextView dateTv;
    protected WebView textWV;
    protected FlowLayout layoutKeywords;
    protected Button tryAgainBtn;

    protected float fontSize = FONT_SIZE_NORMAL;
    protected boolean chromeTabsFlag = false;

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
                    Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                    intent.putExtra(KEYWORD_QUERY, keyword);
                    startActivity(intent);
                }
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
                String str = getActivity().getResources()
                        .getString(R.string.something_doesnt_work);
                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void initFields() {
        textWV.loadUrl(URL_TEXT_ARTICLE + idArticle + "/" + fontSize);
        titleTv.setText(article.getTitle());
        authorTv.setText(article.getAuthor());
        dateTv.setText(article.getDatePublish());
//        toolbar.setTitle(article.getTitle());!!!!!!!!!!!!!!!!!!!!!!


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

    private void openLinkInBrowser(Uri address) {
        Intent intent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(intent);
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
                        Intent intent = new Intent(getActivity(), ShowArticleActivity.class);
                        intent.putExtra(ID, idArticle);
                        startActivity(intent);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                String str = getActivity().getResources()
                        .getString(R.string.something_doesnt_work);
                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        });
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

    private class MyJavaInterface {
        @android.webkit.JavascriptInterface
        public void getGreeting(String str) {
            Intent intent = new Intent(getActivity(), ShowImageActivity.class);
            intent.putExtra(SRC_OF_IMAGE, str);
            startActivity(intent);
        }
    }
}
