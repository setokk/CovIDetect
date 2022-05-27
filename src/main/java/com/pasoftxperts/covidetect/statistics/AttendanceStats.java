package com.pasoftxperts.covidetect.statistics;

import com.pasoftxperts.covidetect.time.TimeStamp;
import com.pasoftxperts.covidetect.university.Room;

import java.util.ArrayList;

public interface AttendanceStats
{
    public ArrayList<Double> calculateAttendanceRates(String startDate,
                                                      String endDate,
                                                      String option,
                                                      String statisticalMethod,
                                                      ArrayList<String> showByElements,
                                                      ArrayList<Room> roomList);
}