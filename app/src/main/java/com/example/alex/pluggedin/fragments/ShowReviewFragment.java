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
import android.util.Log;
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

import com.example.alex.pluggedin.API.ReviewAPI;
import com.example.alex.pluggedin.R;
import com.example.alex.pluggedin.chrome.CustomTabActivityHelper;
import com.example.alex.pluggedin.models.Keyword;
import com.example.alex.pluggedin.models.Review;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.alex.pluggedin.constants.Constants.*;

public class ShowReviewFragment extends ShowBaseFragment{


    private ReviewAPI reviewAPI;
    private Review review;

    private TextView markOfAuthor;
    private WebView plusMinus;
    private WebView conclusion;

    public static ShowReviewFragment newInstance (int idReview) {
        ShowReviewFragment fragment = new ShowReviewFragment();

        Bundle args = new Bundle();
        args.putInt(ID_REVIEW, idReview);
        Log.d(MY_TAG, idReview + " newInstance");
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_review_fragment, null);

        idArticle = getArguments().getInt(ID_REVIEW, 0);
        Log.d(MY_TAG, idArticle + " onCreateView");

        SharedPreferences pref = getActivity()
                .getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        fontSize = pref.getFloat(APP_PREFERENCES_FONT_SIZE, FONT_SIZE_NORMAL);
        chromeTabsFlag = pref.getBoolean(APP_PREFERENCES_CHROME_TABS, false);

        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(DOMAIN).build();
        reviewAPI = adapter.create(ReviewAPI.class);
        initViewsForReview(view);
        getArticleById(idArticle);


        return view;
    }

    @Override
    protected void getArticleById (int idArticle) {
        Log.d(MY_TAG, "id article: " + idArticle);
        reviewAPI.getOpenReview(idArticle, new Callback<List<Review>>() {
            @Override
            public void success(List<Review> reviews, Response response) {
                review = reviews.get(0);
                if (review != null) {
                    initFields();
                }

                showAllElementHideBtn();
            }

            @Override
            public void failure(RetrofitError error) {
                hideAllElementsShowBtn();
                Toast.makeText(getActivity(), SOMETHING_DOESNT_WORK, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void initFields() {
        textWV.loadUrl(URL_TEXT_REVIEW + idArticle + "/" + fontSize);
        titleTv.setText(review.getTitle());
        authorTv.setText(review.getAuthor());
        dateTv.setText(review.getDatePublish());
        plusMinus.loadData(review.getPlusesMinuses(), "text/html", "UTF-8");
        conclusion.loadData(review.getConclusion(), "text/html", "UTF-8");


        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        List<TextView> list = new ArrayList<>(review.getKeywords().size());

        for(Keyword word: review.getKeywords()) {
            View view = layoutInflater.inflate(R.layout.keyword, layoutKeywords, false);
            view.setOnClickListener(this);
            TextView keyWord = (TextView) view.findViewById(R.id.keywordTV);
            keyWord.setText(word.getTitle());
            layoutKeywords.addView(view);
            list.add(keyWord);
        }
        changeFontSize(list);
    }

    protected void initViewsForReview(View v) {
        titleTv = (TextView) v.findViewById(R.id.showReviewTitle);
        authorTv = (TextView) v.findViewById(R.id.openReviewAuthor);
        textWV = (WebView) v.findViewById(R.id.webViewTextOpen);
        dateTv = (TextView) v.findViewById(R.id.openReviewDate);
        layoutKeywords = (FlowLayout) v.findViewById(R.id.layoutKeywords);
        tryAgainBtn = (Button) v.findViewById(R.id.openReviewTryAgainBtn);
        markOfAuthor = (TextView) v.findViewById(R.id.markOfAuthorDigit);
        plusMinus = (WebView) v.findViewById(R.id.webViewPlusMinus);
        conclusion = (WebView) v.findViewById(R.id.webConclusion);
        if (tryAgainBtn != null) {
            tryAgainBtn.setOnClickListener(this);
        }

        textWV.getSettings().setJavaScriptEnabled(true);
        textWV.addJavascriptInterface(new MyJavaInterface(), "test");

        textWV.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(DOMAIN)) {
//                    showArticleByURL(url);!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    // open link to site Pluggedin
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
                Toast.makeText(getActivity(), SOMETHING_DOESNT_WORK, Toast.LENGTH_SHORT).show();
            }
        });

    }



//    protected void showArticleByURL(String url) {
//        String[] arrUrl = url.split("/");
//        String latinTitle = arrUrl[arrUrl.length - 1];
//        latinTitle = convertHexSubStringsToNormalString(latinTitle);
//
//        reviewAPI.getArticleIdByLatinTitle(latinTitle, new Callback<Response>() {
//            @Override
//            public void success(Response response, Response another) {
//                InputStream inputStream;
//                int idArticle;
//                try {
//                    inputStream = response.getBody().in();
//                    idArticle = convertBytesArray(inputStream);
//
//                    if(idArticle > 0) {
//                        Intent intent = new Intent(ShowArticleActivity.this, ShowArticleActivity.class);
//                        intent.putExtra(ID, idArticle);
//                        startActivity(intent);
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Toast.makeText(ShowArticleActivity.this, SOMETHING_DOESNT_WORK, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    protected void changeFontSize(List<TextView> keywords) {
        try {
            if(fontSize != FONT_SIZE_NORMAL) {
                titleTv.setTextSize(titleTv.getTextSize() * fontSize) ;
                authorTv.setTextSize(authorTv.getTextSize() * fontSize);
                dateTv.setTextSize(dateTv.getTextSize() * fontSize);
                markOfAuthor.setTextSize(markOfAuthor.getTextSize() * fontSize);

                for(TextView tv: keywords) {
                    tv.setTextSize(tv.getTextSize() * fontSize);
                }
            }
        } catch (Exception e) {}
    }

}
