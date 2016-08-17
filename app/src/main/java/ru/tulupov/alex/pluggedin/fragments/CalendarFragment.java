package ru.tulupov.alex.pluggedin.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.alex.pluggedin.R;

import java.util.List;


import ru.tulupov.alex.pluggedin.adapters.CalendarAdapter;
import ru.tulupov.alex.pluggedin.constants.Constants;
import ru.tulupov.alex.pluggedin.fragments.views.CalendarView;
import ru.tulupov.alex.pluggedin.models.Calendar;
import ru.tulupov.alex.pluggedin.presenters.CalendarPresenter;

public class CalendarFragment extends Fragment implements View.OnClickListener, CalendarView {

    protected static final int LAYOUT = R.layout.fragment_calendar;

    protected int typeCalendar;

    protected Button buttonTryAgain;
    protected RecyclerView recyclerView;

    protected CalendarPresenter presenter;

    public static CalendarFragment getInstance(int type) {
        CalendarFragment fragment =  new CalendarFragment();
        fragment.setArguments(new Bundle());
        fragment.setTypeCalendar(type);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            typeCalendar = savedInstanceState.getInt(Constants.TYPE);
        }

        View view = inflater.inflate(LAYOUT, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewCalendar);
        buttonTryAgain = (Button) view.findViewById(R.id.buttonTryAgain);

        buttonTryAgain.setOnClickListener(this);
        presenter = new CalendarPresenter(this);
        presenter.downloadCalendar(typeCalendar);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonTryAgain) {
            presenter.downloadCalendar(typeCalendar);
        }
    }

    public int getTypeCalendar() {
        return typeCalendar;
    }

    public void setTypeCalendar(int typeCalendar) {
        this.typeCalendar = typeCalendar;
    }

    @Override
    public void showListCalendars(List<Calendar> calendars) {
        recyclerView.setVisibility(View.VISIBLE);
        buttonTryAgain.setVisibility(View.GONE);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new CalendarAdapter(calendars, this, getContext()));
    }

    @Override
    public void showCalendar(Calendar calendar) {

    }

    @Override
    public void failDownload() {
        recyclerView.setVisibility(View.GONE);
        buttonTryAgain.setVisibility(View.VISIBLE);

        String str;
        if (checkConnection()) {
            str = getResources().getString(R.string.something_doesnt_work);
        } else {
            str = getActivity().getResources().getString(R.string.no_internet);
        }
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.TYPE, typeCalendar);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    protected boolean checkConnection() {
        ConnectivityManager connectChecker = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectChecker.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = connectChecker.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
