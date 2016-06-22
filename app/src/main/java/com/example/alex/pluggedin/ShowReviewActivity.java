package com.example.alex.pluggedin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.alex.pluggedin.fragments.ShowReviewFragment;
import static com.example.alex.pluggedin.constants.Constants.*;

public class ShowReviewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private int idReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_review);
        idReview = getIntent().getIntExtra(ID_REVIEW, 0);
        Log.d(MY_TAG, idReview + "");
        if (idReview == 0) {
            finish();
        }

        initToolBar();
        initFragment();
    }

    private void initFragment() {
        ShowReviewFragment fragment = ShowReviewFragment.newInstance(idReview);
        getFragmentManager().beginTransaction().add(R.id.showReviewFragmentContainer, fragment).commit();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarShowReview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
