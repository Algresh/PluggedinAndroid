package ru.tulupov.alex.pluggedin.presenters;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.tulupov.alex.pluggedin.API.SliderAPI;
import ru.tulupov.alex.pluggedin.constants.Constants;
import ru.tulupov.alex.pluggedin.fragments.views.SliderView;
import ru.tulupov.alex.pluggedin.models.Slide;

public class SliderPresenter {

    private SliderAPI api;
    private SliderView view;
    private List<Slide> dataSlider;

    public SliderPresenter(SliderView view) {
        this.view = view;
        this.initAPI();
    }

    public void downloadSlide(int numSlide) {
        if (view != null) {
            if (dataSlider != null) {
                view.setupSlide(dataSlider.get(numSlide));
            } else {
                this.downloadSliderData(numSlide);
            }
        }
    }

    public void onDestroy() {
        view = null;
    }

    protected void initAPI() {
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.DOMAIN).build();
        api = adapter.create(SliderAPI.class);
    }

    protected void downloadSliderData (final int numSlideToShow) {
        api.getSlider(new Callback<List<Slide>>() {
            @Override
            public void success(List<Slide> slides, Response response) {
                if (view != null){
                    if (slides != null) {
                        dataSlider = slides;
                        view.setupSlide(dataSlider.get(numSlideToShow));
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
}
