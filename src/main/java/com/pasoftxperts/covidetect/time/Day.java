package com.pasoftxperts.covidetect.time;

import java.time.DayOfWeek;

public class Day
{
    private DayOfWeek dayOfWeek;
    private HourSpan hourSpan;

    public Day(DayOfWeek dayOfWeek, HourSpan hourSpan)
    {
        this.dayOfWeek = dayOfWeek;
        this.hourSpan = hourSpan;
    }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }

    public void setDay(DayOfWeek day) { this.dayOfWeek = dayOfWeek; }

    public HourSpan getHourSpan() { return hourSpan; }

    public void setHourSpan(HourSpan hourSpan) { this.hourSpan = hourSpan; }
}
