package ru.tulupov.alex.pluggedin.presenters;


import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.tulupov.alex.pluggedin.API.CalendarAPI;
import static ru.tulupov.alex.pluggedin.constants.Constants.*;
import ru.tulupov.alex.pluggedin.fragments.views.CalendarView;
import ru.tulupov.alex.pluggedin.models.Calendar;

public class CalendarPresenter {

    private CalendarView view;
    private List<Calendar> dataCalendar;
    private CalendarAPI calendarAPI;

    public CalendarPresenter(CalendarView view) {
        this.view = view;
        initApi();
    }

    // type is film or game
    public void downloadCalendar(int type) {

        if (view != null) {
            if (type == TYPE_FILM) {
                calendarAPI.getFilms(this.getCallback());
            } else if(type == TYPE_GAME) {
                calendarAPI.getGames(this.getCallback());
            }
        }
    }

    public void showCalendarByPosition(int position) {
        if (dataCalendar != null) {
            if (view != null) {
                view.showCalendar(dataCalendar.get(position));
            }
        }
    }

    public void onDestroy() {
        view = null;
    }

    private void initApi() {
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(DOMAIN).build();
        calendarAPI = adapter.create(CalendarAPI.class);
    }

    private Callback<List<Calendar>> getCallback() {
        return new Callback<List<Calendar>>() {
            @Override
            public void success(List<Calendar> calendars, Response response) {
                if (view != null) {
                    dataCalendar = calendars;
                    view.showListCalendars(calendars);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (view != null) {
                    view.failDownload();
                }
            }
        };
    }
}
