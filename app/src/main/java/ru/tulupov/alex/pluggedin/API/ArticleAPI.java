package ru.tulupov.alex.pluggedin.API;

import ru.tulupov.alex.pluggedin.models.Article;

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

    @GET("/api/search/articles/{title}/{page}")
    void getListArticlesBySearch(@Path("title") String title, @Path("page") int page, Callback<List<Article>> response);

    @GET("/api/search/keywords/{keyword}/{page}")
    void getListArticlesByKeyword(@Path("keyword") String keyword, @Path("page") int page, Callback<List<Article>> response);
}
