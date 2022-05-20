package com.pasoftxperts.covidetect.time;

public class Day
{
    private int dayNumber;
    private HourSpan hourSpan;

    public Day(int dayNumber, HourSpan hourSpan)
    {
        this.dayNumber = dayNumber;
        this.hourSpan = hourSpan;
    }

    public Day(int dayNumber)
    {
        this.dayNumber = dayNumber;
    }

    public int getDayNumber() { return dayNumber; }

    public void setDayNumber(int dayNumber) { this.dayNumber = dayNumber; }

    public HourSpan getHourSpan() { return hourSpan; }

    public void setHourSpan(HourSpan hourSpan) { this.hourSpan = hourSpan; }
}
