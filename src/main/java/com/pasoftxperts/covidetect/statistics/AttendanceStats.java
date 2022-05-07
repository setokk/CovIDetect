package com.pasoftxperts.covidetect.statistics;

import com.pasoftxperts.covidetect.time.TimeStamp;

import java.util.ArrayList;

public interface AttendanceStats
{
    public ArrayList<Double> calculateAttendanceRates(TimeStamp startTimeStamp,
                                                   TimeStamp endTimeStamp,
                                                   String option,
                                                   ArrayList<ArrayList<TimeStamp>> roomTimeStampList);
}