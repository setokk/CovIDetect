package com.pasoftxperts.covidetect.covidcaseupdater;

import com.pasoftxperts.covidetect.time.Day;
import com.pasoftxperts.covidetect.time.TimeStamp;
import org.junit.jupiter.api.Test;

import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class CovidCaseUpdaterTest {

    @Test
    void dayDistanceBetween()
    {

        // TEST 1 //


        // We create two timestamps (dates)
        // We want to see how close we are to the targetTimeStamp from the current day
        TimeStamp targetTimeStamp = new TimeStamp(2022, Month.APRIL, new Day(12));
        TimeStamp currentTimeStamp = new TimeStamp(2022, Month.APRIL, new Day(10));

        // The day difference should be 2 and not negative
        //assertEquals(2, CovidCaseUpdater.dayDistanceBetween(targetTimeStamp, currentTimeStamp));


        // TEST 2 //


        // We create two timestamps (dates)
        // We want to see how close we are to the targetTimeStamp from the current day
        targetTimeStamp = new TimeStamp(2023, Month.APRIL, new Day(12));
        currentTimeStamp = new TimeStamp(2022, Month.APRIL, new Day(10));

        // The day difference should be negative (the years are not the same)
        assertEquals(-1, CovidCaseUpdater.dayDistanceBetween(targetTimeStamp, currentTimeStamp));
    }
}