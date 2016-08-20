package ru.tulupov.alex.pluggedin.fragments.views;

import java.util.List;

import ru.tulupov.alex.pluggedin.models.Slide;

public interface SliderView {

    void initSlider(List<Slide> slides);
    void failDownloading();
}
