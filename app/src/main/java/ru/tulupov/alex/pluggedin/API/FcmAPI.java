package ru.tulupov.alex.pluggedin.API;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


public interface FcmAPI {
    @FormUrlEncoded
    @POST("/api/GCM/set/registrationId")
    void registrId(@Field("regId") String regId, Callback<Response> callback);
}
