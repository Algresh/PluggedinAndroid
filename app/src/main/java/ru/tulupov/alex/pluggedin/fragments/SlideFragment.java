package ru.tulupov.alex.pluggedin.fragments;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.tulupov.alex.pluggedin.R;
import com.squareup.picasso.Picasso;

import static ru.tulupov.alex.pluggedin.constants.Constants.MY_TAG;
import static ru.tulupov.alex.pluggedin.constants.Constants.URL_IMAGES;
import ru.tulupov.alex.pluggedin.models.Slide;

public class SlideFragment extends Fragment implements View.OnClickListener {

    protected static final int LAYOUT = R.layout.slide_fragment;

    protected ImageView slideImage;
    protected TextView slideText;
    protected Slide dataSlide;
    protected ClickSlideImageListener clickListener;

    protected int widthScreen = 0;

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
        Picasso.with(getContext()).load(URL_IMAGES + dataSlide.getFile()).into(slideImage);
        try {
            String str = dataSlide.getText();
            int maxWidth = maxLengthSlideText(str);
            if (maxWidth < str.length()) {
                str = str.substring(0, maxWidth) + "...";
            }

            slideText.setText(str);
        } catch (NullPointerException e) {
            slideText.setVisibility(View.GONE);
        }

    }

    protected int maxLengthSlideText (String str) {
        int width = getWidthScreen();
        width =  pxToDp(((width - 32) / 18) * 2);

        if (str.length() > width) {
            for (; width >= 0; width--) {
                if (str.charAt(width) == ' ') break;
            }
        }

        return width;
    }

    protected int getWidthScreen() {
        if (widthScreen == 0) {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            widthScreen = size.x;
        }

        return widthScreen;
    }

    protected int pxToDp(int px) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }



}
