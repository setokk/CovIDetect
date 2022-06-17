package com.pasoftxperts.covidetect.covidcaseupdater;

import com.pasoftxperts.covidetect.graphanalysis.SingleCaseNeighbourCalculator;
import com.pasoftxperts.covidetect.time.TimeStamp;
import com.pasoftxperts.covidetect.university.Room;
import com.pasoftxperts.covidetect.university.Seat;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static com.pasoftxperts.covidetect.graphanalysis.GraphNeighboursGenerator.isCovidCase;
import static com.pasoftxperts.covidetect.guicontrollers.RoomVisualizationController.DEFAULT_ROOM_CAPACITY;
import static com.pasoftxperts.covidetect.guicontrollers.RoomVisualizationController.DEFAULT_SEAT_ROWS;

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

                            if (!isCovidCase(seats.get(k).getStudent()))
                            {
                                seats.get(k).getStudent().setHealthIndicator(1);

                                DefaultUndirectedGraph<Seat, Integer> graph = SingleCaseNeighbourCalculator.calculateSingleCaseNeighbours
                                               (seats.get(k),
                                                seats,
                                                DEFAULT_SEAT_ROWS,
                                                DEFAULT_ROOM_CAPACITY/ DEFAULT_SEAT_ROWS);

                                timeStamps.get(j).addSeatGraph(graph);

                                if (!modifiedRooms.contains(room))
                                    modifiedRooms.add(room);
                            }
                        }
                    }

                    seats.clear();
                }
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
