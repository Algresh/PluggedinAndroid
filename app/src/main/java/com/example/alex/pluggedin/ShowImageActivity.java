package com.example.alex.pluggedin;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import static com.example.alex.pluggedin.constants.Constants.*;

public class ShowImageActivity extends AppCompatActivity {

    ImageView image;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        toolbar = (Toolbar) findViewById(R.id.toolbarShowImage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String src = getIntent().getStringExtra(SRC_OF_IMAGE);

        image = (ImageView) findViewById(R.id.ShowImageIV);

        int maxHeight = (int) ( getHeightScreen() * 0.7);
        Picasso.with(this).load(src).into(image);
        image.setMaxHeight(maxHeight);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getHeightScreen() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.y;
    }

}
