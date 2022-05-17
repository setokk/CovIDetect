package com.pasoftxperts.covidetect.time;

import com.pasoftxperts.covidetect.university.Seat;
import edu.uci.ics.jung.graph.UndirectedGraph;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeStamp
{
    public static final String DATE_FORMAT = "EEEE, MMMM d, yyyy";

    private int year;
    private Month month;
    private Day day;
    private UndirectedGraph<Seat, Integer> graph;

    public TimeStamp(int year, Month month, Day day, UndirectedGraph<Seat, Integer> graph)
    {
        this.year = year;
        this.month = month;
        this.day = day;
        this.graph = graph;
    }

    public TimeStamp(int year, Month month, Day day)
    {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() { return year; }

    public Month getMonth() { return month; }

    public Day getDay() { return day; }

    public UndirectedGraph<Seat, Integer> getGraph() { return graph; }

    public void addGraph(UndirectedGraph<Seat, Integer> graph) { this.graph = graph; }

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
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month.getValue() - 1, day.getDayNumber(), 0, 0, 0);

        Date date = calendar.getTime();
        Format f = new SimpleDateFormat("EEEE", Locale.US);

        return f.format(date);
    }

    public String getMonthName()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month.getValue() - 1, day.getDayNumber(), 0, 0, 0);

        Date date = calendar.getTime();
        Format f = new SimpleDateFormat("MMMM", Locale.US);

        return f.format(date);
    }
}