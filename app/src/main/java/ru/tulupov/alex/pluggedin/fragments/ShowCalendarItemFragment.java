package ru.tulupov.alex.pluggedin.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.pluggedin.R;
import com.squareup.picasso.Picasso;

import ru.tulupov.alex.pluggedin.activities.SearchResultsActivity;
import ru.tulupov.alex.pluggedin.constants.Constants;
import ru.tulupov.alex.pluggedin.models.Calendar;

import static ru.tulupov.alex.pluggedin.constants.Constants.SEARCH_QUERY;
import static ru.tulupov.alex.pluggedin.constants.Constants.URL_IMAGES;


public class ShowCalendarItemFragment extends DialogFragment implements View.OnClickListener {

    private Calendar calendar;

    private TextView titleTv;
    private TextView dateTv;
    private ImageView image;
    private Button btn;
    private Button btnClose;
    private int widthScreen;

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.show_calendar_item_fragment, null);

        titleTv = (TextView) view.findViewById(R.id.showItemCalendarTitle);
        dateTv = (TextView) view.findViewById(R.id.showItemCalendarDate);
        image = (ImageView) view.findViewById(R.id.showItemCalendarImage);
        btn = (Button) view.findViewById(R.id.showItemCalendarBtn);
        btnClose = (Button) view.findViewById(R.id.showItemCalendarBtnClose);
        btn.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        initFields();
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.showItemCalendarBtn) {
            Intent intent = new Intent(getContext(), SearchResultsActivity.class);
            intent.putExtra(SEARCH_QUERY, calendar.getTitle());
            startActivity(intent);
        } else if (view.getId() == R.id.showItemCalendarBtnClose) {
            dismiss();
        }

    }

    private void initFields(){
        if (calendar != null) {
            titleTv.setText(calendar.getTitle());
            dateTv.setText(calendar.getDate());
            Picasso.with(getContext()).load(URL_IMAGES + calendar.getFile()).resize(widthScreen, widthScreen / 4 * 3).into(image);
        }
    }

    public void setWidthScreen(int widthScreen) {
        this.widthScreen = widthScreen / 10 * 9;
    }
}