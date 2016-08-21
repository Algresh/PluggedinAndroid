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

import java.util.List;

import static ru.tulupov.alex.pluggedin.constants.Constants.*;
import ru.tulupov.alex.pluggedin.models.Calendar;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>{

    protected List<Calendar> listCalendar;
    protected View.OnClickListener clickListener;
    protected Context context;
    protected int widthScreen;

    public CalendarAdapter(List<Calendar> listCalendar, View.OnClickListener clickListener, Context context) {
        this.listCalendar = listCalendar;
        this.clickListener = clickListener;
        this.context = context;
        this.widthScreen = 0;
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

//    private int calculateImageWidth() {
//        if (widthScreen != 0) {
////            return widthScreen - 3 * 10 -
//        }
//    }

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
