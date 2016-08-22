package ru.tulupov.alex.pluggedin.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.tulupov.alex.pluggedin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static ru.tulupov.alex.pluggedin.constants.Constants.*;
import ru.tulupov.alex.pluggedin.models.Calendar;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>{

    protected List<Calendar> listCalendar;
    protected List<Calendar> listCalendarUnchanged;
    protected View.OnClickListener clickListener;
    protected Context context;
    protected int widthScreen;


    public CalendarAdapter(List<Calendar> listCalendar, View.OnClickListener clickListener, Context context) {
        this.listCalendarUnchanged = listCalendar;
        this.clickListener = clickListener;
        this.context = context;
        this.widthScreen = 0;

        this.listCalendar = new ArrayList<>(listCalendar.size());

        for (Calendar c : listCalendar) {
            this.listCalendar.add(c);
        }
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder holder, int position) {
        Calendar item = listCalendar.get(position);

        String file = item.getFile();
        String title = item.getTitle();
        String date = item.getDate();

        holder.textCalendar.setText(title);
        holder.dateCalendar.setText(date);
        holder.cardView.setOnClickListener(clickListener);

        Picasso.with(context).load(URL_IMAGES + file).resize(320, 240).into(holder.imageCalendar);
    }

    @Override
    public int getItemCount() {
        return listCalendar.size();
    }

    public int getWidthScreen() {
        return widthScreen;
    }

    public void setWidthScreen(int widthScreen) {
        this.widthScreen = widthScreen;
    }

    public void showItemsByDate(java.util.Calendar date) {
        int selectDay = date.get(java.util.Calendar.DAY_OF_MONTH);
        int selectMonth = date.get(java.util.Calendar.MONTH) + 1;
        int selectYear = date.get(java.util.Calendar.YEAR);

        listCalendar = new ArrayList<>();

        for (int i = 0; i < listCalendarUnchanged.size(); i++) {
            Calendar note = listCalendarUnchanged.get(i);
            String[] dates = note.getDate().split("\\.");

            int day = Integer.parseInt(dates[0]);
            int month = Integer.parseInt(dates[1]);
            int year = Integer.parseInt(dates[2]);

            if (selectYear < year || ( selectYear == year && ( selectMonth < month ||
                    ( selectMonth == month && selectDay <= day) ) ) ) {
                listCalendar.add(note);
            }
        }

        notifyDataSetChanged();
    }


    public static class CalendarViewHolder extends RecyclerView.ViewHolder{

        TextView textCalendar;
        TextView dateCalendar;
        ImageView imageCalendar;
        CardView cardView;

        public CalendarViewHolder(View itemView) {
            super(itemView);
            textCalendar = (TextView) itemView.findViewById(R.id.calendarText);
            dateCalendar = (TextView) itemView.findViewById(R.id.calendarDate);
            imageCalendar = (ImageView) itemView.findViewById(R.id.calendarImage);
            cardView = (CardView) itemView.findViewById(R.id.cardViewCalendar);
        }
    }
}
