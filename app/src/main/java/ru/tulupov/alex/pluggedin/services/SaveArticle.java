package ru.tulupov.alex.pluggedin.services;

import android.app.IntentService;
import android.content.Intent;

import static ru.tulupov.alex.pluggedin.constants.Constants.*;


public class SaveArticle extends IntentService {



    public SaveArticle(String name) {
        super(name);
    }

    public SaveArticle() {
        super(DEFAULT_THREAD);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

//
//        String article =  intent.getStringExtra("article");
//        if (article != null) {
//            Log.d(MY_TAG, article);
//        } else {
//            Log.d(MY_TAG,"author is null");
//        }
    }
}
