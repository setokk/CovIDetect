package com.pasoftxperts.covidetect.statistics;

import com.pasoftxperts.covidetect.time.TimeStamp;

import java.util.ArrayList;

public class StatisticalAnalysis implements AttendanceStats, CovidCaseStats
{
    @Override
    public ArrayList<Double> calculateAttendanceRates(String startDate,
                                                      String endDate,
                                                      String option,
                                                      ArrayList<ArrayList<TimeStamp>> roomTimeStampList,
                                                      ArrayList<String> showByElements)
    {
        return null;
    }

    @Override
    public ArrayList<Integer> calculateCovidCases(String startDate,
                                                  String endDate,
                                                  String option,
                                                  ArrayList<ArrayList<TimeStamp>> roomTimeStampList,
                                                  ArrayList<String> showByElements)
    {
        return null;
    }
}
