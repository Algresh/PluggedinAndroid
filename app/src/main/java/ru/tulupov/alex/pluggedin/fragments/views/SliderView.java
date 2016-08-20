package ru.tulupov.alex.pluggedin.fragments.views;

import ru.tulupov.alex.pluggedin.models.Slide;

public interface SliderView {

    void setupSlide(Slide slide);
    void failDownloading();
}
