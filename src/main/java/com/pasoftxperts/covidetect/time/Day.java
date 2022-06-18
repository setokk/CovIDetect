package com.pasoftxperts.covidetect.time;

import java.io.Serializable;

public class Day implements Serializable
{
    private int dayNumber;
    private HourSpan hourSpan;

    public Day(int dayNumber, HourSpan hourSpan)
    {
        if (dayNumber > 31)
            throw new IllegalArgumentException("Day Number can't be greater than 31");

        this.dayNumber = dayNumber;
        this.hourSpan = hourSpan;
    }

    public Day(int dayNumber)
    {
        if (dayNumber > 31)
            throw new IllegalArgumentException("Day Number can't be greater than 31");

        this.dayNumber = dayNumber;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public HourSpan getHourSpan() {
        return hourSpan;
    }

    public void setHourSpan(HourSpan hourSpan) {
        this.hourSpan = hourSpan;
    }
}
