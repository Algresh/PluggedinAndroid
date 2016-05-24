package com.example.alex.pluggedin.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

    public ReviewFragment() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(DOMAIN).build();
        reviewAPI = restAdapter.create(ReviewAPI.class);
    }

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
        pDialog.setMessage(getResources().getString(R.string.downloadingReviews));

        connectNetwork(FIRST_PAGE);
        addNewItemsByScroll();
        return view;
    }

    @Override
    public void connectNetwork (final int page) {
        if (page == FIRST_PAGE) {
            reviewAPI.getFirstListReviews(getCallbackReview(page));
        } else {
            ArticleAdapter adapter = (ArticleAdapter) recyclerView.getAdapter();

            if(page == UPDATE_PAGE) {
                int firstID = adapter.getIdFirstItem();
                reviewAPI.getListUpdateReviews(firstID, getCallbackReview(page));
            } else {
                int lastID = adapter.getIdLastItem();
                reviewAPI.getListReviews(lastID, getCallbackReview(page));
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
}
