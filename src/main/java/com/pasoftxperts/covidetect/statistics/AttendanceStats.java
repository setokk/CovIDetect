package com.pasoftxperts.covidetect.statistics;

import com.pasoftxperts.covidetect.time.TimeStamp;

import java.util.ArrayList;

public interface AttendanceStats
{
    public ArrayList<Double> calculateAttendanceRates(String startDate,
                                                      String endDate,
                                                      String option,
                                                      ArrayList<ArrayList<TimeStamp>> roomTimeStampList,
                                                      ArrayList<String> showByElements);
}