package ru.tulupov.alex.pluggedin.API;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import ru.tulupov.alex.pluggedin.models.Slide;

public interface SliderAPI {

    @GET("/api/slider/get")
    void getSlider(Callback<List<Slide>> response);

}
