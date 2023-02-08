/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class represents a day. It has a dayNumber in a month (can't be more than 31)
 | and an HourSpan (the hours at which a lecture of a course occurred)
 |
 |
*/

package com.pasoftxperts.covidetect.client.time;

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
