package com.pasoftxperts.covidetect.time;

import com.pasoftxperts.covidetect.university.Seat;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeStamp implements Serializable
{
    public static final String DATE_FORMAT = "EEEE, MMMM d, yyyy";

    private int year;
    private Month month;
    private Day day;
    private DefaultUndirectedGraph<Seat, Integer> seatGraph;

    public TimeStamp(int year, Month month, Day day, DefaultUndirectedGraph<Seat, Integer> graph)
    {
        this.year = year;
        this.month = month;
        this.day = day;
        this.seatGraph = graph;
    }

    public TimeStamp(int year, Month month, Day day)
    {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getDateToString()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month.getValue() - 1, day.getDayNumber(), 0, 0, 0);

        Date date = calendar.getTime();

        Format f = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        String dateToString = f.format(date);

        return dateToString;
    }

    public String getDayName()
    {
        return convertDayToText(year, month, day.getDayNumber());
    }

    public static String convertDayToText(int year, Month month, int dayNumber)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month.getValue() - 1, dayNumber, 0, 0, 0);

        Date date = calendar.getTime();
        Format f = new SimpleDateFormat("EEEE", Locale.US);

        return f.format(date);
    }

    public static int getDayValueOfWeek(int year, Month month, int dayNumber)
    {
        switch (convertDayToText(year, month, dayNumber))
        {
            case "Monday":
                return 0;
            case "Tuesday":
                return 1;
            case "Wednesday":
                return 2;
            case "Thursday":
                return 3;
            case "Friday":
                return 4;
            default:
                return -1;
        }
    }

    public String getMonthName()
    {
        return month.getDisplayName(TextStyle.FULL, Locale.US);
    }

    public int getYear() { return year; }

    public Month getMonth() { return month; }

    public Day getDay() { return day; }

    public DefaultUndirectedGraph<Seat, Integer> getSeatGraph() { return seatGraph; }

    public void addSeatGraph(DefaultUndirectedGraph<Seat, Integer> graph) { this.seatGraph = graph; }

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
}