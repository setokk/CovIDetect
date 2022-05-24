package com.pasoftxperts.covidetect.statistics;

import com.pasoftxperts.covidetect.time.TimeStamp;

import java.util.ArrayList;

public interface CovidCaseStats
{
    public ArrayList<Integer> calculateCovidCases(String startDate,
                                                  String endDate,
                                                  String option,
                                                  ArrayList<ArrayList<TimeStamp>> roomTimeStampList,
                                                  ArrayList<String> showByElements);
}
