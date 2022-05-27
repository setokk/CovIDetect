package com.pasoftxperts.covidetect.statistics;

import com.pasoftxperts.covidetect.time.TimeStamp;
import com.pasoftxperts.covidetect.university.Room;

import java.util.ArrayList;

public class StatisticalAnalysis implements AttendanceStats, CovidCaseStats
{
    @Override
    public ArrayList<Double> calculateAttendanceRates(String startDate,
                                                      String endDate,
                                                      String option,
                                                      String statisticalMethod,
                                                      ArrayList<String> showByElements,
                                                      ArrayList<Room> roomList)
    {
        return null;
    }

    @Override
    public ArrayList<Integer> calculateCovidCases(String startDate,
                                                  String endDate,
                                                  String option,
                                                  String statisticalMethod,
                                                  ArrayList<String> showByElements,
                                                  ArrayList<Room> roomList)
    {
        return null;
    }
}
