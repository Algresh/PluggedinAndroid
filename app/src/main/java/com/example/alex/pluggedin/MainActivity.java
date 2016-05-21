package com.example.alex.pluggedin;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.alex.pluggedin.API.ReviewAPI;
import com.example.alex.pluggedin.adapters.ArticleAdapter;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import static  com.example.alex.pluggedin.constants.Constants.*;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;

    private Button buttonTryAgain;

    private RestAdapter restAdapter;

    private ReviewAPI reviewAPI;

    private LinearLayoutManager linearManager;

    private int pages = FIRST_PAGE;
    private int lastDownloadPages = FIRST_PAGE;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonTryAgain = (Button) findViewById(R.id.buttonTryAgain);

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(DOMAIN).build();
        reviewAPI = restAdapter.create(ReviewAPI.class);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        String title = getResources().getString(R.string.review);

        initToolbar(title, R.id.toolbarReview);
        initNavigationView();

        connectNetwork(FIRST_PAGE);
        addNewItemsByScroll();
    }

    public void connectNetwork (final int page) {

        reviewAPI.getListReviews(page, new Callback<List<Review>>() {
            @Override
            public void success(List<Review> reviews, Response response) {
                if (page == FIRST_PAGE) {
                    buttonTryAgain.setVisibility(View.GONE);

                    linearManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(linearManager);
                    recyclerView.setAdapter(new ArticleAdapter(reviews, MainActivity.this));
                } else {
                    boolean success = ((ArticleAdapter) recyclerView.getAdapter()).addNewItems(reviews);
                    if( success ) {
                        lastDownloadPages++;
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

                if(page == FIRST_PAGE) {
                    buttonTryAgain.setVisibility(View.VISIBLE);
                    buttonTryAgain.setOnClickListener(MainActivity.this);
                } else {
                    pages--;
                }

                if (toast == null) {
                    toast = Toast.makeText(MainActivity.this, "Что то не работает!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonTryAgain) {
            connectNetwork(FIRST_PAGE);
        }
    }

    // метод добавляющий новые элементы когда скрол достиг последнего элемента
    private void addNewItemsByScroll() {
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

}
