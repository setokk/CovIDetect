package com.pasoftxperts.covidetect;

import com.pasoftxperts.covidetect.simulation.Curriculum;
import com.pasoftxperts.covidetect.simulation.Simulation;
import com.pasoftxperts.covidetect.student.Student;
import com.pasoftxperts.covidetect.time.Day;
import com.pasoftxperts.covidetect.time.HourSpan;
import com.pasoftxperts.covidetect.time.TimeStamp;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
        TimeStamp timeStamp = new TimeStamp(2022, Month.MAY, new Day(1));
        System.out.println(timeStamp.getDayName());
    }
}
