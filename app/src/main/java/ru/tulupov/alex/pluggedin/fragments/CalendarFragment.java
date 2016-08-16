package ru.tulupov.alex.pluggedin.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.alex.pluggedin.R;

public class CalendarFragment extends Fragment implements View.OnClickListener {

    public static final int LAYOUT = R.layout.fragment_calendar;

    public int typeCalendar;
    Button buttonTryAgain;

    public static CalendarFragment getInstance(int type) {
        CalendarFragment fragment =  new CalendarFragment();
        fragment.setArguments(new Bundle());
        fragment.setTypeCalendar(type);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        buttonTryAgain = (Button) view.findViewById(R.id.buttonTryAgain);
        buttonTryAgain.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    public int getTypeCalendar() {
        return typeCalendar;
    }

    public void setTypeCalendar(int typeCalendar) {
        this.typeCalendar = typeCalendar;
    }
}
