/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used to update a covid case of a student.
 |
 |
 | Design:
 | - The system looks 2 days back (DAYS_TO_LOOK_BACK) from a specific date in every room to update the student's health indicator
 |
 |
 | Method Documentation:
 |     [*] public static boolean updateStudentCovidCase(String studentId,
 |                                               String targetDate,
 |                                               List<Room> rooms,
 |                                               List<Room> modifiedRooms)
 |         Takes a student id, a target date, a list of rooms and an empty list of modified rooms as input.
 |         Updates the status of a student and returns true or false whether they were found or not
 |         The modifiedRooms are needed to optimize the updating and writing of the files process as we only need
 |         to update the rooms at which the student was found at.
 |
 |     [*] public static int dayDistanceBetween(TimeStamp targetTimeStamp, TimeStamp currentTimeStamp)
 |         Takes a target time stamp (date) and a current time stamp (date) as input.
 |         Calculates the backwards day distance between currentTimeStamp and targetTimeStamp
 |         Returns -1 (arbitrary) if the days before are more than a month's length.
 |         (we only care about 1 or 0 month/year difference between the dates)
 |
 |
*/

package com.pasoftxperts.covidetect.client.covidcaseupdater;

import com.pasoftxperts.covidetect.client.graphanalysis.SingleCaseNeighbourCalculator;
import com.pasoftxperts.covidetect.client.time.TimeStamp;
import com.pasoftxperts.covidetect.client.university.Room;
import com.pasoftxperts.covidetect.client.university.Seat;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class CovidCaseUpdater
{
    // Number of days to look back for possible cases after finding a covid case
    public static final int DAYS_TO_LOOK_BACK = 2;


    // Update the status of a student and returns true or false whether they were found or not
    public static boolean updateStudentCovidCase(String studentId,
                                                 String targetDate,
                                                 List<Room> rooms,
                                                 List<Room> modifiedRooms)
    {
        // Indicates whether the student was found or not
        boolean found = false;

        // Convert date to timestamp
        TimeStamp targetTimeStamp = new TimeStamp(targetDate);

        // Seat list
        List<Seat> seats;

        // We now check for the student for the past 2 days in every room
        for (Room room : rooms)
        {
            ArrayList<TimeStamp> timeStamps = room.getTimeStampList();

            for (int j = 0; j < timeStamps.size(); j++)
            {
                // Calculate how many days we are before targetTimeStamp
                int daysBefore = dayDistanceBetween(targetTimeStamp, timeStamps.get(j));

                // We are 2 days or less before
                if (daysBefore <= DAYS_TO_LOOK_BACK && daysBefore >= 0)
                {
                    seats = new ArrayList<>(timeStamps.get(j).getSeatGraph().vertexSet());

                    for (int k = 0; k < seats.size(); k++)
                    {
                        if (seats.get(k).isOccupied() && seats.get(k).getStudent().getStudentId().equals(studentId))
                        {
                            found = true;

                            if (!seats.get(k).getStudent().isCovidCase())
                            {
                                seats.get(k).getStudent().setHealthIndicator(1);

                                DefaultUndirectedGraph<Seat, Integer> graph = SingleCaseNeighbourCalculator.calculateSingleCaseNeighbours
                                               (seats.get(k),
                                                seats,
                                                room.getSeatRows(),
                                                room.getCapacity() / room.getSeatRows());

                                timeStamps.get(j).addSeatGraph(graph);

                                // Check if room is already in modified rooms list (used to only save the rooms that were modified)
                                if (!modifiedRooms.contains(room))
                                    modifiedRooms.add(room);
                            }
                        }
                    }

                    seats.clear();
                }

                // If we are after the target date, break
                if (timeStamps.get(j).isAfter(targetTimeStamp))
                    break;
            }
        }

        return found;
    }


    /*
    |  Calculates the backwards day distance between currentTimeStamp and targetTimeStamp
    |  Returns -1 (arbitrary) if the days before are more than a month's length. (We only care about 1 or 0 month/year difference between the dates)
    |  We use it to see if we are 2 days before the target timestamp to properly update the
    |  covid status of the student, if they exist in that room and time.
    */
    public static int dayDistanceBetween(TimeStamp targetTimeStamp, TimeStamp currentTimeStamp)
    {
        int daysBefore = -1;

        int currYear = currentTimeStamp.getYear();
        Month currMonth = currentTimeStamp.getMonth();
        int currDay = currentTimeStamp.getDay().getDayNumber();

        int targetYear = targetTimeStamp.getYear();
        Month targetMonth = targetTimeStamp.getMonth();
        int targetDay = targetTimeStamp.getDay().getDayNumber();

        // If same year, same month
        if ((currYear == targetYear) && (currMonth.getValue() == targetMonth.getValue()))
            daysBefore = targetDay - currDay;

        // If 1 month difference and either same year or next year
        if (targetMonth.getValue() - currMonth.getValue() == 1)
        {
            if ((targetYear == currYear) || (targetYear - currYear == 1))
            {
                daysBefore = currMonth.length((currYear%4==0) || (currYear%400==0)) - currDay + targetDay;
            }
        }

        return daysBefore;
    }
}
