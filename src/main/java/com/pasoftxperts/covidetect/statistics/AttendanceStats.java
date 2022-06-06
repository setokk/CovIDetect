package com.pasoftxperts.covidetect.statistics;

import com.pasoftxperts.covidetect.time.TimeStamp;
import com.pasoftxperts.covidetect.university.Room;

import java.util.ArrayList;

public interface AttendanceStats
{
    public ArrayList<Double> calculateAttendanceRates(String startDate,
                                                      String endDate,
                                                      ArrayList<Double> minMaxAverage,
                                                      String showByOption,
                                                      ArrayList<String> showByElements,
                                                      ArrayList<Room> roomList);
}