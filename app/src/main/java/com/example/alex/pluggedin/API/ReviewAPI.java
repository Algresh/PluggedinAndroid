package com.example.alex.pluggedin.API;


import com.example.alex.pluggedin.models.Review;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface ReviewAPI {
    @GET("/api/list/reviews")
    void getFirstListReviews(Callback<List<Review>> response);

    @GET("/api/list/reviews/{id}")
    void getListReviews(@Path("id") int id, Callback<List<Review>> response);

    @GET("/api/list/update/reviews/{id}")
    void getListUpdateReviews(@Path("id") int id, Callback<List<Review>> response);

    @GET("/api/list/article/{page}/{type}")
    void getListArticles(@Path("page") int page,@Path("page") int type ,Callback<List<Review>> response);

}
