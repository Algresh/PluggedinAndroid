package ru.tulupov.alex.pluggedin.API;


import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import ru.tulupov.alex.pluggedin.models.Calendar;

public interface CalendarAPI {

    @GET("/api/calendar/get/films")
    void getFilms(Callback<List<Calendar>> response);

    @GET("/api/calendar/get/games")
    void getGames(Callback<List<Calendar>> response);
}
