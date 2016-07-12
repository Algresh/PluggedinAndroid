package ru.tulupov.alex.pluggedin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.alex.pluggedin.R;

import ru.tulupov.alex.pluggedin.view.TouchImageView;
import com.squareup.picasso.Picasso;

import static ru.tulupov.alex.pluggedin.constants.Constants.*;

public class ShowImageActivity extends AppCompatActivity {

    TouchImageView image;
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

        image = (TouchImageView) findViewById(R.id.ShowImageIV);

        Picasso.with(this).load(src).into(image);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
