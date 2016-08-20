package ru.tulupov.alex.pluggedin.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.pluggedin.R;
import com.squareup.picasso.Picasso;

import ru.tulupov.alex.pluggedin.constants.Constants;
import ru.tulupov.alex.pluggedin.models.Slide;

public class SlideFragment extends Fragment implements View.OnClickListener {

    protected static final int LAYOUT = R.layout.slide_fragment;

    protected ImageView slideImage;
    protected TextView slideText;
    protected Slide dataSlide;
    protected ClickSlideImageListener clickListener;

    public interface ClickSlideImageListener {
        void onClickSlideImage(String link);
    }

    public static SlideFragment getInstance (Slide slide) {
        SlideFragment fragment = new SlideFragment();
        fragment.setArguments(new Bundle());
        fragment.setDataSlide(slide);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View slideView = inflater.inflate(LAYOUT, container, false);

        slideImage = (ImageView) slideView.findViewById(R.id.imageSlide);
        slideText = (TextView) slideView.findViewById(R.id.sliderText);
        slideImage.setOnClickListener(this);
        initViews();

        return slideView;
    }

    public Slide getDataSlide() {
        return dataSlide;
    }

    public void setDataSlide(Slide dataSlide) {
        this.dataSlide = dataSlide;
    }

    @Override
    public void onClick(View view) {
        clickListener.onClickSlideImage(dataSlide.getLink());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        clickListener = (ClickSlideImageListener) activity;
    }

    private void initViews() {
        Picasso.with(getContext()).load(Constants.URL_IMAGES + dataSlide.getFile()).into(slideImage);
        slideText.setText(dataSlide.getText());
    }


}
