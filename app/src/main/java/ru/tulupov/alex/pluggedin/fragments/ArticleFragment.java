package ru.tulupov.alex.pluggedin.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tulupov.alex.pluggedin.API.ArticleAPI;
import com.example.alex.pluggedin.R;
import ru.tulupov.alex.pluggedin.activities.ShowArticleActivity;
import ru.tulupov.alex.pluggedin.adapters.ArticleAdapter;

import retrofit.RestAdapter;
import ru.tulupov.alex.pluggedin.constants.Constants;

public class ArticleFragment extends BasePageFragment {

    ArticleAPI articleAPI;
    private int type;

    public static ArticleFragment getInstance(int type) {
        ArticleFragment fragment = new ArticleFragment();
        fragment.setType(type);
        fragment.setArguments(new Bundle());

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            type = savedInstanceState.getInt(Constants.TYPE);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void initAPI() {
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.DOMAIN).build();
        articleAPI = adapter.create(ArticleAPI.class);
    }

    @Override
    public void connectNetwork(int page) {
        if (page == Constants.FIRST_PAGE) {
            articleAPI.getFirstListArticlesByType(type, getCallbackArticle(page));
        } else {
            ArticleAdapter adapter = (ArticleAdapter) recyclerView.getAdapter();

            if(page == Constants.UPDATE_PAGE) {
                int firstID = adapter.getIdFirstItem();
                articleAPI.getListUpdateArticlesByType(firstID, type, getCallbackArticle(page));
            } else {
                int lastID = adapter.getIdLastItem();
                articleAPI.getListArticlesByType(type, lastID, getCallbackArticle(page));
            }
        }
    }


    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        connectNetwork(Constants.UPDATE_PAGE);
        linearManager.scrollToPosition(0);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonTryAgain) {
            connectNetwork(Constants.FIRST_PAGE);
        } else {
            Intent intent = new Intent(getContext(), ShowArticleActivity.class);
            int idArticle = (Integer) v.findViewById(R.id.cardView).getTag();
            intent.putExtra(Constants.ID, idArticle);
            getContext().startActivity(intent);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.TYPE, type);
    }
}
