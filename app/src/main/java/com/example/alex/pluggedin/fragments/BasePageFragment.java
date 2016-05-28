package com.example.alex.pluggedin.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.alex.pluggedin.R;
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

    protected Button buttonTryAgain;

    protected LinearLayoutManager linearManager;

    protected RecyclerView recyclerView;

    protected int pages = FIRST_PAGE;
    protected int lastDownloadPages = FIRST_PAGE;

    protected Toast toast;

    protected ProgressDialog pDialog;

    protected SwipeRefreshLayout refreshLayout;


    public abstract void connectNetwork (final int page);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        buttonTryAgain = (Button) view.findViewById(R.id.buttonTryAgain);

        buttonTryAgain.setOnClickListener(this);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshRecycleView);
        if(refreshLayout != null) {
            refreshLayout.setOnRefreshListener(this);
        }

        pDialog = new ProgressDialog(getContext());
//        pDialog.setMessage(titleProgressMsg);

        connectNetwork(FIRST_PAGE);
        addNewItemsByScroll();
        return view;
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

        if (page == FIRST_PAGE) {
            pDialog.show();
        }

        return new Callback<List<Article>>() {
            @Override
            public void success(List<Article> reviews, Response response) {
                successArticle(page, reviews);
                pDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                failureArticle(page);
            }
        };
    }

    protected void successArticle(final int page, List<Article> reviews) {
        if (page == FIRST_PAGE) {
            buttonTryAgain.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);

            linearManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearManager);
            recyclerView.setAdapter(new ArticleAdapter(reviews, getContext(), this));
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

        pDialog.dismiss();
        if (toast == null) {
            toast = Toast.makeText(getContext(), "Что то не работает!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
