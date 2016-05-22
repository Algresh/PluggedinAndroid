package com.example.alex.pluggedin.fragments;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.alex.pluggedin.R;
import com.example.alex.pluggedin.Review;
import com.example.alex.pluggedin.adapters.ArticleAdapter;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.alex.pluggedin.constants.Constants.FIRST_PAGE;


public class BasePageFragment extends Fragment {
    public static final int LAYOUT = R.layout.fragment_page;

    protected Button buttonTryAgain;

    protected LinearLayoutManager linearManager;

    protected RecyclerView recyclerView;

    protected int pages = FIRST_PAGE;
    protected int lastDownloadPages = FIRST_PAGE;

    protected Toast toast;

    protected void addNewItemsByScroll() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) {
                    int visibleItemCount = linearManager.getChildCount();
                    int totalItemCount   = linearManager.getItemCount();
                    int pastVisibleItems = linearManager.findFirstVisibleItemPosition();

//                    Log.d(MY_TAG, visibleItemCount + " " + totalItemCount + " " + pastVisibleItems);
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount && pages == lastDownloadPages){
                        pages++;
                        connectNetwork(pages);
                    }
                }
            }
        });
    }

    public void connectNetwork (final int page) {};

}
