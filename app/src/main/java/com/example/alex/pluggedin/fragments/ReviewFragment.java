package com.example.alex.pluggedin.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.alex.pluggedin.API.ReviewAPI;
import com.example.alex.pluggedin.MainActivity;
import com.example.alex.pluggedin.R;
import com.example.alex.pluggedin.Review;
import com.example.alex.pluggedin.adapters.ArticleAdapter;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.alex.pluggedin.constants.Constants.DOMAIN;
import static com.example.alex.pluggedin.constants.Constants.FIRST_PAGE;

public class ReviewFragment extends BasePageFragment {

    private ProgressDialog pDialog;
    private ReviewAPI reviewAPI;


    public static ReviewFragment getInstamce() {
        ReviewFragment fragment = new ReviewFragment();
        fragment.setArguments(new Bundle());



        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(LAYOUT, container, false);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(DOMAIN).build();
        reviewAPI = restAdapter.create(ReviewAPI.class);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(getResources().getString(R.string.downloadingReviews));

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        buttonTryAgain = (Button) view.findViewById(R.id.buttonTryAgain);
        connectNetwork(FIRST_PAGE);
        addNewItemsByScroll();
       return view;
    }

    @Override
    public void connectNetwork (final int page) {

        pDialog.show();
        reviewAPI.getListReviews(page, new Callback<List<Review>>() {
            @Override
            public void success(List<Review> reviews, Response response) {
                if (page == FIRST_PAGE) {
                    buttonTryAgain.setVisibility(View.GONE);

                    linearManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(linearManager);
                    recyclerView.setAdapter(new ArticleAdapter(reviews, getContext()));
                } else {
                    boolean success = ((ArticleAdapter) recyclerView.getAdapter()).addNewItems(reviews);
                    if( success ) {
                        lastDownloadPages++;
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {

                if(page == FIRST_PAGE) {
                    buttonTryAgain.setVisibility(View.VISIBLE);
//                    buttonTryAgain.setOnClickListener(getActivity());
                } else {
                    pages--;
                }

                pDialog.dismiss();
                if (toast == null) {
                    toast = Toast.makeText(getContext(), "Что то не работает!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }


}
