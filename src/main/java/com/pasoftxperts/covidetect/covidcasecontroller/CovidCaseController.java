package com.pasoftxperts.covidetect.covidcasecontroller;

import com.pasoftxperts.covidetect.time.TimeStamp;
import com.pasoftxperts.covidetect.university.Room;

import java.util.ArrayList;

public class CovidCaseController
{
    public void updateCaseById(String studentId, TimeStamp date, ArrayList<Room> roomList)
    {
        for (int i=0; i<roomList.size(); i++)
        {
            Room r = roomList.get(i);

            ArrayList<TimeStamp> timeStampList = r.getTimeStampList();

            for (int j=0; j<timeStampList.size(); j++)
            {
                TimeStamp timeStamp = timeStampList.get(j);
            }
        }
    }
}
