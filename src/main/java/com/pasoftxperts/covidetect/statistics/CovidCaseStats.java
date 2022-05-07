package com.pasoftxperts.covidetect.statistics;

import com.pasoftxperts.covidetect.time.TimeStamp;

import java.util.ArrayList;

public interface CovidCaseStats
{
    public ArrayList<Integer> calculateCovidCases(TimeStamp startTimeStamp,
                                             TimeStamp endTimeStamp,
                                             String option,
                                             ArrayList<ArrayList<TimeStamp>> roomTimeStampList);
}
