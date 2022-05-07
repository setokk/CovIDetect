package com.pasoftxperts.covidetect.time;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeStamp
{
    private int year;
    private Month month;
    private Day day;

    public TimeStamp(int year, Month month, Day day)
    {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() { return year; }

    public Month getMonth() { return month; }

    public Day getDay() { return day; }

    @Override
    public String toString()
    {
        return day.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + ", " +
                month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + ", " +
                year;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof TimeStamp)
        {
            TimeStamp temp = (TimeStamp) o;

            return (this.toString().equals(temp.toString()) &&
                    this.getDay().getHourSpan().getStartHour() == temp.getDay().getHourSpan().getStartHour());
        }

        return false;
    }

    public String displayTimeStamp()
    {
        return "";
    }

    // TEMP
    public void initialize()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, 0, 1, 0, 0, 0);
        Date date = calendar.getTime();
        System.out.println(date);

        Format f = new SimpleDateFormat("EEEE", Locale.US);
        String text = f.format(date);
        System.out.println(text);
    }
}