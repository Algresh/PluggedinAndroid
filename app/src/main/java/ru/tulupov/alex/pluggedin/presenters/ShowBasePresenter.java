package ru.tulupov.alex.pluggedin.presenters;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.tulupov.alex.pluggedin.API.ArticleAPI;
import ru.tulupov.alex.pluggedin.constants.Constants;
import ru.tulupov.alex.pluggedin.fragments.views.ShowBaseView;

import static ru.tulupov.alex.pluggedin.constants.Constants.ID_ARTICLE;
import static ru.tulupov.alex.pluggedin.constants.Constants.MY_TAG;
import static ru.tulupov.alex.pluggedin.constants.Constants.TYPE;
import static ru.tulupov.alex.pluggedin.constants.Constants.TYPE_REVIEW;

public class ShowBasePresenter extends BasePresenter{

    private ArticleAPI apiArticle;
    private ShowBaseView view;

    public ShowBasePresenter(ShowBaseView view) {
        this.view = view;
        this.initAPI();
    }

    public void showArticleOrReviewByURL(String url) {
        String[] arrUrl = url.split("/");
        String latinTitle = arrUrl[arrUrl.length - 1];
        latinTitle = convertHexSubStringsToNormalString(latinTitle);
        Log.d(MY_TAG, latinTitle);

        apiArticle.getArticleIdByLatinTitle(latinTitle, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                InputStream inputStream;
                int idArticle = -1;
                int type = -1;
                try {
                    try {
                        inputStream = response.getBody().in();
                        Map<String, Integer> map = convertBytesArray(inputStream);
                        idArticle = map.get(ID_ARTICLE);
                        type = map.get(TYPE);
                    } catch (NullPointerException e) {

                    }

                    if(view != null && idArticle > 0) {
                        if (type == TYPE_REVIEW) {
                            view.showReview(idArticle);
                        } else {
                            view.showArticle(idArticle);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(view != null){
                    view.failShow();
                }
            }
        });
    }

    protected void initAPI() {
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.DOMAIN).build();
        apiArticle = adapter.create(ArticleAPI.class);
    }
}
