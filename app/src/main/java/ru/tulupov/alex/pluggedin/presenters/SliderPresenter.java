package ru.tulupov.alex.pluggedin.presenters;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.tulupov.alex.pluggedin.API.ArticleAPI;
import ru.tulupov.alex.pluggedin.API.SliderAPI;
import ru.tulupov.alex.pluggedin.constants.Constants;
import ru.tulupov.alex.pluggedin.fragments.views.SliderView;
import ru.tulupov.alex.pluggedin.models.Slide;

import static ru.tulupov.alex.pluggedin.constants.Constants.ID_ARTICLE;
import static ru.tulupov.alex.pluggedin.constants.Constants.MY_TAG;
import static ru.tulupov.alex.pluggedin.constants.Constants.TYPE;
import static ru.tulupov.alex.pluggedin.constants.Constants.TYPE_REVIEW;

public class SliderPresenter {

    private SliderAPI api;
    private ArticleAPI apiArticle;
    private SliderView view;
    private List<Slide> dataSlider;

    public SliderPresenter(SliderView view) {
        this.view = view;
        this.initAPI();
    }

    public void clickSlideImage(String url) {
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

    public void downloadSlider() {
        if (view != null) {
            if (dataSlider != null) {
                view.initSlider(dataSlider);
            } else {
                this.downloadSliderData();
            }
        }
    }

    public void onDestroy() {
        view = null;
    }

    protected void initAPI() {
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.DOMAIN).build();
        api = adapter.create(SliderAPI.class);
        apiArticle = adapter.create(ArticleAPI.class);
    }

    protected void downloadSliderData () {
        api.getSlider(new Callback<List<Slide>>() {
            @Override
            public void success(List<Slide> slides, Response response) {
                if (view != null){
                    if (slides != null) {
                        dataSlider = slides;
                        view.initSlider(slides);
                    } else {
                        view.failDownloading();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (view != null) {
                    view.failDownloading();
                }
            }
        });
    }

    protected String convertHexSubStringsToNormalString(String latinTitle) {
        latinTitle = latinTitle.replace("%C2%AB", "«");//!
        latinTitle = latinTitle.replace("%C2%BB", "»");//!
        latinTitle = latinTitle.replace("%22", "\"");//!
        latinTitle = latinTitle.replace("%27", "\'");//!
        latinTitle = latinTitle.replace("%28", "(");//!
        latinTitle = latinTitle.replace("%29", ")");//!
        latinTitle = latinTitle.replace("%E2%80%94", "—");//!
        return latinTitle;

    }

    protected Map<String, Integer> convertBytesArray(InputStream inputStream) throws IOException {
        Map<String, Integer> map = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int read;
            while ((read = inputStream.read()) != -1) {
                bos.write(read);
            }
            byte[] result = bos.toByteArray();
            bos.close();
            String data = new String(result);
            JSONObject jsonObj = new JSONObject(data);
            map = new HashMap<>();
            map.put(ID_ARTICLE, jsonObj.getInt(ID_ARTICLE));
            map.put(TYPE, jsonObj.getInt(TYPE));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
        }

        return map;
    }
}
