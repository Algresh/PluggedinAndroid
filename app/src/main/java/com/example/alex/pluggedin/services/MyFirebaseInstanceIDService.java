package com.example.alex.pluggedin.services;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.alex.pluggedin.API.FcmAPI;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.alex.pluggedin.constants.Constants.*;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    SharedPreferences.Editor editor;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(MY_TAG, "Refreshed token: " + refreshedToken);

        editor = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE).edit();
        if (refreshedToken != null) {
            editor.putString(APP_PREFERENCES_FCM_TOKEN, refreshedToken);
            sendRegistrationToServer(refreshedToken);

        }


    }

    private void sendRegistrationToServer(String token) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(DOMAIN).build();
        FcmAPI api = restAdapter.create(FcmAPI.class);
        api.registrId(token, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(MY_TAG, "Refreshed token was send!!!: ");
                editor.putBoolean(APP_PREFERENCES_TOCEN_IS_SEND, true);
                editor.apply();
//                editor.putString(APP_PREFERENCES_PERSONAL_ID, )
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(MY_TAG, "refresh fail token");
                editor.apply();
            }
        });
    }
}
