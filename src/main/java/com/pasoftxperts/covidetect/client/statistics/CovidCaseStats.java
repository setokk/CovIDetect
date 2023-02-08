package com.pasoftxperts.covidetect.client.statistics;

import com.pasoftxperts.covidetect.client.university.Room;

import java.util.ArrayList;

public interface CovidCaseStats
{
    public ArrayList<Double> calculateCovidCases(String startDate,
                                                  String endDate,
                                                  String showByOption,
                                                  ArrayList<String> showByElements,
                                                  ArrayList<Room> roomList);
}
