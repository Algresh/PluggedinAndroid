package ru.tulupov.alex.pluggedin.fragments.views;

import java.util.List;

import ru.tulupov.alex.pluggedin.models.Calendar;

public interface CalendarView {

    void showListCalendars(List<Calendar> calendars);
    void showCalendar(Calendar calendar);
    void failDownload();
}
