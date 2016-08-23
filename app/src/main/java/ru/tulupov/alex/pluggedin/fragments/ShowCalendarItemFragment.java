package ru.tulupov.alex.pluggedin.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ru.tulupov.alex.pluggedin.R;
import com.squareup.picasso.Picasso;

import ru.tulupov.alex.pluggedin.activities.SearchResultsActivity;
import ru.tulupov.alex.pluggedin.models.Calendar;

import static ru.tulupov.alex.pluggedin.constants.Constants.APP_PREFERENCES;
import static ru.tulupov.alex.pluggedin.constants.Constants.APP_PREFERENCES_FONT_SIZE;
import static ru.tulupov.alex.pluggedin.constants.Constants.FONT_SIZE_LARGE;
import static ru.tulupov.alex.pluggedin.constants.Constants.FONT_SIZE_NORMAL;
import static ru.tulupov.alex.pluggedin.constants.Constants.FONT_SIZE_SMALL;
import static ru.tulupov.alex.pluggedin.constants.Constants.LARGE_DATE_CALENDAR_SHOW;
import static ru.tulupov.alex.pluggedin.constants.Constants.LARGE_TITLE_CALENDAR_SHOW;
import static ru.tulupov.alex.pluggedin.constants.Constants.NORMAL_DATE_CALENDAR_SHOW;
import static ru.tulupov.alex.pluggedin.constants.Constants.NORMAL_TITLE_CALENDAR_SHOW;
import static ru.tulupov.alex.pluggedin.constants.Constants.SEARCH_QUERY;
import static ru.tulupov.alex.pluggedin.constants.Constants.SMALL_DATE_CALENDAR_SHOW;
import static ru.tulupov.alex.pluggedin.constants.Constants.SMALL_TITLE;
import static ru.tulupov.alex.pluggedin.constants.Constants.SMALL_TITLE_CALENDAR_SHOW;
import static ru.tulupov.alex.pluggedin.constants.Constants.URL_IMAGES;


public class ShowCalendarItemFragment extends DialogFragment implements View.OnClickListener {

    private Calendar calendar;

    private TextView titleTv;
    private TextView dateTv;
    private ImageView image;
    private Button btn;
    private Button btnClose;
    private int widthScreen;
    private float fontSize = FONT_SIZE_NORMAL;

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
        this.initFontSize();
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
    public void setWidthScreen(int widthScreen) {
        this.widthScreen = widthScreen / 10 * 9;
    }

    private void initFields(){
        if (calendar != null) {
            titleTv.setText(calendar.getTitle());
            dateTv.setText(calendar.getDate());
            Picasso.with(getContext()).load(URL_IMAGES + calendar.getFile()).resize(widthScreen, widthScreen / 4 * 3).into(image);
        }
    }

    private void initFontSize() {
        SharedPreferences pref = getActivity()
                .getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        fontSize = pref.getFloat(APP_PREFERENCES_FONT_SIZE, FONT_SIZE_NORMAL);

        if (fontSize == FONT_SIZE_LARGE) {
            titleTv.setTextSize(LARGE_TITLE_CALENDAR_SHOW);
            dateTv.setTextSize(LARGE_DATE_CALENDAR_SHOW);
        }

        if (fontSize == FONT_SIZE_SMALL) {
            titleTv.setTextSize(SMALL_TITLE_CALENDAR_SHOW);
            dateTv.setTextSize(SMALL_DATE_CALENDAR_SHOW);
        }

        if (fontSize == FONT_SIZE_NORMAL) {
            titleTv.setTextSize(NORMAL_TITLE_CALENDAR_SHOW);
            dateTv.setTextSize(NORMAL_DATE_CALENDAR_SHOW);
        }

    }



}
