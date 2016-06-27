package com.example.alex.pluggedin.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.alex.pluggedin.R;
import com.example.alex.pluggedin.adapters.fontsizes.ChangeableFontSize;
import com.example.alex.pluggedin.adapters.fontsizes.FontSizesParameter;
import com.example.alex.pluggedin.models.Article;
import com.example.alex.pluggedin.adapters.ArticleAdapter;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import static  com.example.alex.pluggedin.constants.Constants.*;

import static com.example.alex.pluggedin.constants.Constants.FIRST_PAGE;


public abstract class BasePageFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    public static final int LAYOUT = R.layout.fragment_page;
    public static int countToast = 0;

    protected Button buttonTryAgain;

    protected LinearLayoutManager linearManager;

    protected RecyclerView recyclerView;

    protected int pages = FIRST_PAGE;
    protected int lastDownloadPages = FIRST_PAGE;


    protected SwipeRefreshLayout refreshLayout;


    public abstract void connectNetwork (final int page);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        initAPI();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        buttonTryAgain = (Button) view.findViewById(R.id.buttonTryAgain);

        buttonTryAgain.setOnClickListener(this);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshRecycleView);
        if(refreshLayout != null) {
            refreshLayout.setOnRefreshListener(this);
        }

        connectNetwork(FIRST_PAGE);
        addNewItemsByScroll();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ArticleAdapter adapter = (ArticleAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setFontSizeParameter(getFontSizeParameter());
        }
    }

    protected void addNewItemsByScroll() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) {
                    int visibleItemCount = linearManager.getChildCount();
                    int totalItemCount   = linearManager.getItemCount();
                    int pastVisibleItems = linearManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount && pages == lastDownloadPages){
                        pages++;
                        connectNetwork(pages);
                    }
                }
            }
        });
    }

    protected Callback<List<Article>> getCallbackArticle (final int page) {

        return new Callback<List<Article>>() {
            @Override
            public void success(List<Article> reviews, Response response) {
                successArticle(page, reviews);
            }

            @Override
            public void failure(RetrofitError error) {
                failureArticle(page);
            }
        };
    }

    protected void successArticle(final int page, List<Article> reviews) {
        countToast = 0;
        if (page == FIRST_PAGE) {
            buttonTryAgain.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);

            linearManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearManager);
            ArticleAdapter adapter = new ArticleAdapter(reviews, getContext(), this);
            adapter.setFontSizeParameter(getFontSizeParameter());
            adapter.setWidthScreen(getWidthScreen());
            recyclerView.setAdapter(adapter);
        } else {
            ArticleAdapter adapter = (ArticleAdapter) recyclerView.getAdapter();
            if (page == UPDATE_PAGE) {
                if (reviews != null && reviews.size() > 0) {
                    adapter.addToTop(reviews);
                }
            } else {
                boolean success = adapter.addNewItems(reviews);
                if( success ) {
                    lastDownloadPages++;
                }
            }
        }
    }

    protected void failureArticle(final int page) {
        if(page == FIRST_PAGE) {
            refreshLayout.setVisibility(View.GONE);
            buttonTryAgain.setVisibility(View.VISIBLE);
        } else {
            pages--;
        }

        String str;
        if (countToast == 0) {
            countToast++;
            if (checkConnection()) {
                str = getActivity().getResources()
                        .getString(R.string.something_doesnt_work);
            } else {
                str = getActivity().getResources()
                        .getString(R.string.no_internet);
            }
            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        } else if(countToast > 1){
            countToast++;
            if (checkConnection()) {
                str = getActivity().getResources()
                        .getString(R.string.something_doesnt_work);
            } else {
                str = getActivity().getResources()
                        .getString(R.string.no_internet);
            }
            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        } else {
            countToast++;
        }


    }



    protected ChangeableFontSize getFontSizeParameter() {
        SharedPreferences pref =
                getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        float fontSize = pref.getFloat(APP_PREFERENCES_FONT_SIZE, FONT_SIZE_NORMAL);
        return new FontSizesParameter(fontSize);
    }

    protected abstract void initAPI();

    private int getWidthScreen() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }

    protected boolean checkConnection() {
        ConnectivityManager connectChecker = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectChecker.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = connectChecker.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
