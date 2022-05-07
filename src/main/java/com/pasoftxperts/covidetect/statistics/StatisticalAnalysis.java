package com.pasoftxperts.covidetect.statistics;

import com.pasoftxperts.covidetect.time.TimeStamp;

import java.util.ArrayList;

public class StatisticalAnalysis implements AttendanceStats, CovidCaseStats
{

    @Override
    public ArrayList<Double> calculateAttendanceRates(TimeStamp startTimeStamp,
                                                      TimeStamp endTimeStamp,
                                                      String option,
                                                      ArrayList<ArrayList<TimeStamp>> roomTimeStampList)
    {
        return null;
    }

    @Override
    public ArrayList<Integer> calculateCovidCases(TimeStamp startTimeStamp,
                                                  TimeStamp endTimeStamp,
                                                  String option,
                                                  ArrayList<ArrayList<TimeStamp>> roomTimeStampList)
    {
        return null;
    }
}
