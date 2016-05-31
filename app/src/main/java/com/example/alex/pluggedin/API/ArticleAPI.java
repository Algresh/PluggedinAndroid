package com.example.alex.pluggedin.API;

import com.example.alex.pluggedin.models.Article;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;


public interface ArticleAPI {

    @GET("/api/list/articles/{type}")
    void getFirstListArticlesByType(@Path("type") int type, Callback<List<Article>> response);

    @GET("/api/list/articles/{type}/{id}")
    void getListArticlesByType(@Path("type") int type, @Path("id") int id, Callback<List<Article>> response);

    @GET("/api/list/update/articles/{id}/{type}")
    void getListUpdateArticlesByType(@Path("id") int id, @Path("type") int type, Callback<List<Article>> response);

    @GET("/api/show/article/{idArticle}")
    void getOpenArticle(@Path("idArticle") int idArticle, Callback<List<Article>> response);

    @GET("/api/latin/title/article/{latinTitle}")
    void getArticleIdByLatinTitle(@Path("latinTitle") String latinTitle, Callback<Response> idArticle);
}
