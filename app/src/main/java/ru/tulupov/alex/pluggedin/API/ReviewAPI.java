package ru.tulupov.alex.pluggedin.API;


import ru.tulupov.alex.pluggedin.models.Article;
import ru.tulupov.alex.pluggedin.models.Review;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

public interface ReviewAPI {
    @GET("/api/list/reviews")
    void getFirstListReviews(Callback<List<Article>> response);

    @GET("/api/list/reviews/{id}")
    void getListReviews(@Path("id") int id, Callback<List<Article>> response);

    @GET("/api/list/update/reviews/{id}")
    void getListUpdateReviews(@Path("id") int id, Callback<List<Article>> response);

    @GET("/api/show/review/{idReview}")
    void getOpenReview(@Path("idReview") int idReview, Callback<List<Review>> response);

    @GET("/api/latin/title/article/{latinTitle}")
    void getArticleIdByLatinTitle(@Path("latinTitle") String latinTitle, Callback<Response> idArticle);
}
