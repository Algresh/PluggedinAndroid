package ru.tulupov.alex.pluggedin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.pluggedin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import ru.tulupov.alex.pluggedin.constants.Constants;
import ru.tulupov.alex.pluggedin.models.Calendar;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>{

    protected List<Calendar> listCalendar;
    protected View.OnClickListener clickListener;
    protected Context context;

    public CalendarAdapter(List<Calendar> listCalendar, View.OnClickListener clickListener, Context context) {
        this.listCalendar = listCalendar;
        this.clickListener = clickListener;
        this.context = context;
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item, parent, false);
        view.setOnClickListener(clickListener);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder holder, int position) {
        Calendar item = listCalendar.get(position);

        String file = item.getFile();
        String title = item.getTitle();

        holder.textCalendar.setText(title);

        Picasso.with(context).load(Constants.URL_IMAGES + file).into(holder.imageCalendar);
    }

    @Override
    public int getItemCount() {
        return listCalendar.size();
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder{

        TextView textCalendar;
        ImageView imageCalendar;

        public CalendarViewHolder(View itemView) {
            super(itemView);
            textCalendar = (TextView) itemView.findViewById(R.id.calendarText);
            imageCalendar = (ImageView) itemView.findViewById(R.id.calendarImage);
        }
    }
}
