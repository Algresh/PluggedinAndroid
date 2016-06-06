package com.example.alex.pluggedin.fragments;

import android.os.Bundle;
import android.view.View;

import com.example.alex.pluggedin.API.ArticleAPI;
import com.example.alex.pluggedin.API.ReviewAPI;
import com.example.alex.pluggedin.R;
import com.example.alex.pluggedin.adapters.ArticleAdapter;

import retrofit.RestAdapter;

import static com.example.alex.pluggedin.constants.Constants.*;

public class ReviewFragment extends BasePageFragment {


    private ReviewAPI reviewAPI;


    public static ReviewFragment getInstance() {
        ReviewFragment fragment = new ReviewFragment();
        fragment.setArguments(new Bundle());

        return fragment;
    }

    @Override
    protected void initAPI() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(DOMAIN).build();
        reviewAPI = restAdapter.create(ReviewAPI.class);
    }



    @Override
    public void connectNetwork (final int page) {
        if (page == FIRST_PAGE) {
            reviewAPI.getFirstListReviews(getCallbackArticle(page));
        } else {
            ArticleAdapter adapter = (ArticleAdapter) recyclerView.getAdapter();

            if(page == UPDATE_PAGE) {
                int firstID = adapter.getIdFirstItem();
                reviewAPI.getListUpdateReviews(firstID, getCallbackArticle(page));
            } else {
                int lastID = adapter.getIdLastItem();
                reviewAPI.getListReviews(lastID, getCallbackArticle(page));
            }
        }

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        connectNetwork(UPDATE_PAGE);
        linearManager.scrollToPosition(0);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonTryAgain) {
            connectNetwork(FIRST_PAGE);
        }
    }
}
