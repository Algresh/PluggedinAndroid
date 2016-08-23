package ru.tulupov.alex.pluggedin.adapters.fontsizes;

import ru.tulupov.alex.pluggedin.adapters.CalendarAdapter;
import static ru.tulupov.alex.pluggedin.constants.Constants.*;

public class FontSizeParameterCalendar implements ChangeableFontSizeCalendar {

    protected float fontSize;

    public FontSizeParameterCalendar(float fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public void changeFontSize(CalendarAdapter.CalendarViewHolder holder) {

        if (fontSize == FONT_SIZE_NORMAL) {
            holder.getDateCalendar().setTextSize(NORMAL_DATE_CALENDAR);
            holder.getTextCalendar().setTextSize(NORMAL_TITLE_CALENDAR);
        }

        if (fontSize == FONT_SIZE_LARGE) {
            holder.getDateCalendar().setTextSize(LARGE_DATE_CALENDAR);
            holder.getTextCalendar().setTextSize(LARGE_TITLE_CALENDAR);
        }

        if (fontSize == FONT_SIZE_SMALL) {
            holder.getDateCalendar().setTextSize(SMALL_DATE_CALENDAR);
            holder.getTextCalendar().setTextSize(SMALL_TITLE_CALENDAR);
        }
    }
}
