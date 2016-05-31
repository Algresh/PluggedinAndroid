package com.example.alex.pluggedin.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import static  com.example.alex.pluggedin.constants.Constants.*;
import com.example.alex.pluggedin.API.ArticleAPI;
import com.example.alex.pluggedin.R;
import com.example.alex.pluggedin.ShowArticleActivity;
import com.example.alex.pluggedin.adapters.ArticleAdapter;

import retrofit.RestAdapter;

public class ArticleFragment extends BasePageFragment {

    ArticleAPI articleAPI;
    private int type;

    public ArticleFragment() {
    }

    public static ArticleFragment getInstance(int type) {
        ArticleFragment fragment = new ArticleFragment(type);
        fragment.setArguments(new Bundle());

        return fragment;
    }

    public ArticleFragment(int type) {
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(DOMAIN).build();
        articleAPI = adapter.create(ArticleAPI.class);
        this.type = type;
//        switch (type) {
//            case TYPE_NEWS:
//                titleProgressMsg = getResources().getString(R.string.downloadingNews);
//                break;
//            case TYPE_INTERESTING:
//                titleProgressMsg = getResources().getString(R.string.downloadingInteresting);
//                break;
//            case TYPE_MEDIA:
//                titleProgressMsg = getResources().getString(R.string.downloadingMedia);
//                break;
//            case TYPE_ARTICLE:
//                titleProgressMsg = getResources().getString(R.string.downloadingArticle);
//                break;
//            default:
//                titleProgressMsg = "";
//        }

    }

    @Override
    public void connectNetwork(int page) {
        if (page == FIRST_PAGE) {
            articleAPI.getFirstListArticlesByType(type, getCallbackArticle(page));
        } else {
            ArticleAdapter adapter = (ArticleAdapter) recyclerView.getAdapter();

            if(page == UPDATE_PAGE) {
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
        connectNetwork(UPDATE_PAGE);
        linearManager.scrollToPosition(0);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonTryAgain) {
            connectNetwork(FIRST_PAGE);
        } else {
            Intent intent = new Intent(getContext(), ShowArticleActivity.class);
            int idArticle = (Integer) v.findViewById(R.id.cardView).getTag();
            intent.putExtra(ID, idArticle);
            getContext().startActivity(intent);
        }

    }
}
