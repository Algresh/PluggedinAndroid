package com.example.alex.pluggedin.API;


import com.example.alex.pluggedin.Review;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface ReviewAPI {
    @GET("/api/list/reviews/{page}")
    void getListReviews(@Path("page") int page, Callback<List<Review>> response);

}
