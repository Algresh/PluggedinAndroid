package ru.tulupov.alex.pluggedin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.tulupov.alex.pluggedin.API.ReviewAPI;
import ru.tulupov.alex.pluggedin.R;
import ru.tulupov.alex.pluggedin.activities.ShowReviewActivity;
import ru.tulupov.alex.pluggedin.adapters.ArticleAdapter;

import retrofit.RestAdapter;

import static ru.tulupov.alex.pluggedin.constants.Constants.*;

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
        }else {
            Intent intent = new Intent(getContext(), ShowReviewActivity.class);
            int idReview = (Integer) v.findViewById(R.id.cardView).getTag();

            intent.putExtra(ID_REVIEW, idReview * (-1));
            getContext().startActivity(intent);
        }
    }
}
