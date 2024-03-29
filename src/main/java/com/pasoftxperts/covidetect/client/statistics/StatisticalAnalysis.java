/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used to extract statistical data.
 | Implements AttendanceStats and CovidCaseStats
 |
 |
 | Method Documentation:
 |     [*] public ArrayList<Double> calculateAttendanceRates(String startDate,
 |                                                    String endDate,
 |                                                    String showByOption,
 |                                                    ArrayList<String> showByElements,
 |                                                    ArrayList<Room> roomList)
 |         Takes a range of dates (start date to end date), a show by option (daily, monthly etc.)
 |         show by elements list (x axis) and a room list.
 |         Returns the attendance rates.
 |
 |     [*] public ArrayList<Double> calculateCovidCases(String startDate,
 |                                                    String endDate,
 |                                                    String showByOption,
 |                                                    ArrayList<String> showByElements,
 |                                                    ArrayList<Room> roomList)
 |         Takes a range of dates (start date to end date), a show by option (daily, monthly etc.)
 |         show by elements list (x axis) and a room list.
 |         Returns the covid cases.
 |
 |     [*] public double calculateStandardDeviation(ArrayList<Double> yAxis, double average, int percentageFactor)
 |         Takes a yAxis list, the average of yAxis and a percentage factor (100 if calculating for rates, 1 if calculating for number of covid cases) as input.
 |         Returns the standard deviation.
 |
 |     [*] public double calculateAverage(ArrayList<Double> yAxis)
 |         Takes a yAxis list as input.
 |         Returns the average.
 |
 |     [*] public double calculateMax(ArrayList<Double> yAxis)
 |         Takes a yAxis list as input.
 |         Returns the max.
 |
 |     [*] public double calculateMin(ArrayList<Double> yAxis)
 |         Takes a yAxis list as input.
 |         Returns the min.
 |
 |
*/

package com.pasoftxperts.covidetect.client.statistics;

import com.pasoftxperts.covidetect.client.counters.CovidCasesCounter;
import com.pasoftxperts.covidetect.client.counters.StudentCounter;
import com.pasoftxperts.covidetect.client.sorting.MultipleSorting;
import com.pasoftxperts.covidetect.client.time.TimeStamp;
import com.pasoftxperts.covidetect.client.filemanager.TaskObjectReader;
import com.pasoftxperts.covidetect.simulation.Simulation;
import com.pasoftxperts.covidetect.client.university.Room;

import java.util.*;

public class StatisticalAnalysis implements AttendanceStats, CovidCaseStats
{
    @Override
    public ArrayList<Double> calculateAttendanceRates(String startDate,
                                                      String endDate,
                                                      String showByOption,
                                                      ArrayList<String> showByElements,
                                                      ArrayList<Room> roomList)
    {
        //
        // showByOption determines the X Axis of the chart (showByElements)
        //
        switch (showByOption)
        {
            case "Year":
                for (int i = Simulation.START_YEAR; i <= Simulation.END_YEAR + 1; i++)
                    showByElements.add(String.valueOf(i));
                break;

            case "Month":
                showByElements.add("January");
                showByElements.add("February");
                showByElements.add("March");
                showByElements.add("April");
                showByElements.add("May");
                showByElements.add("June");
                showByElements.add("July");
                showByElements.add("August");
                showByElements.add("September");
                showByElements.add("October");
                showByElements.add("November");
                showByElements.add("December");
                break;

            case "Day":
                showByElements.add("Monday");
                showByElements.add("Tuesday");
                showByElements.add("Wednesday");
                showByElements.add("Thursday");
                showByElements.add("Friday");
                break;

            case "Professor":

                // Read professor names
                TaskObjectReader taskObjectReader = new TaskObjectReader(System.getProperty("user.dir") + "/university of macedonia/applied informatics/professors/professorNames.ser");
                taskObjectReader.readObjectFile();

                ArrayList<String> nameList = (ArrayList<String>) taskObjectReader.getResult().orElse(new ArrayList<String>(0));

                for (int i = 0; i < nameList.size(); i++)
                    showByElements.add(nameList.get(i));

                nameList.clear();

                break;

            default:
                break;
        }


        // Return statistical results array (y Axis)
        ArrayList<Double> attendanceRates = new ArrayList<>(showByElements.size());

        // In a month for example, we don't have courses every day.
        // So we can't accurately divide by 31 for example if we want the monthly rate.
        ArrayList<Integer> showByCounter = new ArrayList<>(showByElements.size());

        // Initialize Attendance Rates
        for (int i = 0; i < showByElements.size(); i++)
            attendanceRates.add(0.0);

        // Initialize Show by Counter
        for (int i = 0; i < showByElements.size(); i++)
            showByCounter.add(0);

        // Start TimeStamp that indicates where to start searching for statistical results
        TimeStamp startTimeStamp = new TimeStamp(startDate);

        // End TimeStamp that indicates where to stop searching for statistical results
        TimeStamp endTimeStamp = new TimeStamp(endDate);


        // Calculate Attendance based on case
        if (!showByOption.equals("Hour"))
        {
            for (Room room : roomList)
            {
                for (TimeStamp timeStamp : room.getTimeStampList())
                {
                    // Check if we exceeded the end date
                    if (timeStamp.isAfter(endTimeStamp) || timeStamp.equals(endTimeStamp))
                        break;

                    if (timeStamp.isAfter(startTimeStamp) || timeStamp.equals(startTimeStamp))
                    {
                        double numberOfStudents = StudentCounter.countStudents(timeStamp.getSeatGraph());
                        double attendance = numberOfStudents / room.getCapacity(); // Attendance Rate of a day

                        int index;

                        // We get the corresponding index of the X Axis
                        if (showByOption.equals("Year")) // Yearly
                        {
                            index = showByElements.indexOf(String.valueOf(timeStamp.getYear()));
                        }
                        else if (showByOption.equals("Month")) // Monthly
                        {
                            index = showByElements.indexOf(String.valueOf(timeStamp.getMonthName()));
                        }
                        else if (showByOption.equals("Day")) // By Day
                        {
                            index = showByElements.indexOf(String.valueOf(timeStamp.getDayName()));
                        }
                        else // By Professor
                        {
                            index = showByElements.indexOf(timeStamp.getDay().getHourSpan()
                                                                             .getCourse()
                                                                             .getProfessor()
                                                                             .professorName());
                        }

                        attendanceRates.set(index, attendanceRates.get(index) + attendance);

                        // Increment
                        showByCounter.set(index, showByCounter.get(index) + 1);

                    }
                }
            }
        }
        else
        {
            for (Room room : roomList)
            {
                for (TimeStamp timeStamp : room.getTimeStampList())
                {
                    // Check if we exceeded the end date
                    if (timeStamp.isAfter(endTimeStamp) || timeStamp.equals(endTimeStamp))
                        break;

                    if (timeStamp.isAfter(startTimeStamp) || timeStamp.equals(startTimeStamp))
                    {
                        double numberOfStudents = StudentCounter.countStudents(timeStamp.getSeatGraph());
                        double attendance = numberOfStudents / room.getCapacity(); // Attendance Rate of a day

                        String startHour = String.valueOf(timeStamp.getDay().getHourSpan().getStartHour());

                        if (!showByElements.contains(startHour))
                        {
                            showByElements.add(startHour);
                            attendanceRates.add(0.0);
                            showByCounter.add(0);
                        }

                        int index;

                        index = showByElements.indexOf(startHour);

                        attendanceRates.set(index, attendanceRates.get(index) + attendance);

                        // Increment
                        showByCounter.set(index, showByCounter.get(index) + 1);

                    }
                }
            }

            //
            // Sorting the hours accordingly
            //

            // Group both elements for sorting
            ArrayList<MultipleSorting> sorted = new ArrayList<>();

            for (int i = 0; i < showByElements.size(); i++)
            {
                sorted.add(new MultipleSorting(Integer.parseInt(showByElements.get(i)), attendanceRates.get(i), showByCounter.get(i)));
            }

            Collections.sort(sorted);

            // Format the hours correctly and set the corresponding Attendance Rates
            for (int i = 0; i < showByElements.size(); i++)
            {
                showByElements.set(i, sorted.get(i).xaxis + ":00");
                attendanceRates.set(i, sorted.get(i).yaxis);
                showByCounter.set(i, sorted.get(i).counter);
            }

        }


        // Divide the attendanceRates so far by their show by counter
        for (int i = 0; i < attendanceRates.size(); i++)
        {
            if (showByCounter.get(i) == 0)
                attendanceRates.set(i, 0.0);
            else
                attendanceRates.set(i, attendanceRates.get(i) / showByCounter.get(i));
        }

        showByCounter.removeAll(showByCounter);

        return attendanceRates;
    }

    @Override
    public ArrayList<Double> calculateCovidCases(String startDate,
                                                  String endDate,
                                                  String showByOption,
                                                  ArrayList<String> showByElements,
                                                  ArrayList<Room> roomList)
    {
        // showByOption determines the X Axis of the chart (showByElements)
        switch (showByOption)
        {
            case "Year":
                for (int i = Simulation.START_YEAR; i <= Simulation.END_YEAR + 1; i++)
                    showByElements.add(String.valueOf(i));
                break;

            case "Month":
                showByElements.add("January");
                showByElements.add("February");
                showByElements.add("March");
                showByElements.add("April");
                showByElements.add("May");
                showByElements.add("June");
                showByElements.add("July");
                showByElements.add("August");
                showByElements.add("September");
                showByElements.add("October");
                showByElements.add("November");
                showByElements.add("December");
                break;

            case "Day":
                showByElements.add("Monday");
                showByElements.add("Tuesday");
                showByElements.add("Wednesday");
                showByElements.add("Thursday");
                showByElements.add("Friday");
                break;

            case "Professor":
                String path = System.getProperty("user.dir") + "/university of macedonia/applied informatics/professors/professorNames.ser";

                // Read professor names
                TaskObjectReader taskObjectReader = new TaskObjectReader(path);
                taskObjectReader.readObjectFile();

                ArrayList<String> nameList = (ArrayList<String>) taskObjectReader.getResult().orElse(new ArrayList<String>(0));

                for (int i = 0; i < nameList.size(); i++)
                    showByElements.add(nameList.get(i));

                nameList.clear();

                break;

            default:
                break;
        }


        // Return statistical results array
        ArrayList<Double> covidCases = new ArrayList<>(showByElements.size());

        // Initialize Attendance Rates
        for (int i = 0; i < showByElements.size(); i++)
            covidCases.add(0.0);

        // Start TimeStamp that indicates where to start searching for statistical results
        TimeStamp startTimeStamp = new TimeStamp(startDate);

        // End TimeStamp that indicates where to stop searching for statistical results
        TimeStamp endTimeStamp = new TimeStamp(endDate);


        //
        // Calculate Attendance based on case
        //
        if (!showByOption.equals("Hour"))
        {
            for (Room room : roomList)
            {
                for (TimeStamp timeStamp : room.getTimeStampList())
                {
                    // Check if we exceeded the end date
                    if (timeStamp.isAfter(endTimeStamp) || timeStamp.equals(endTimeStamp))
                        break;

                    if (timeStamp.isAfter(startTimeStamp) || timeStamp.equals(startTimeStamp))
                    {
                        double totalCases = CovidCasesCounter.countCovidCases(timeStamp.getSeatGraph());

                        int index;

                        // We get the corresponding index of the X Axis
                        if (showByOption.equals("Year")) // Yearly
                        {
                            index = showByElements.indexOf(String.valueOf(timeStamp.getYear()));
                        }
                        else if (showByOption.equals("Month")) // Monthly
                        {
                            index = showByElements.indexOf(String.valueOf(timeStamp.getMonthName()));
                        }
                        else if (showByOption.equals("Day")) // By Day
                        {
                            index = showByElements.indexOf(String.valueOf(timeStamp.getDayName()));
                        }
                        else // By Professor
                        {
                            index = showByElements.indexOf(timeStamp.getDay().getHourSpan()
                                    .getCourse()
                                    .getProfessor()
                                    .professorName());
                        }

                        covidCases.set(index, covidCases.get(index) + totalCases);

                    }
                }
            }
        }
        else
        {
            for (Room room : roomList)
            {
                for (TimeStamp timeStamp : room.getTimeStampList())
                {
                    // Check if we exceeded the end date
                    if (timeStamp.isAfter(endTimeStamp) || timeStamp.equals(endTimeStamp))
                        break;

                    if (timeStamp.isAfter(startTimeStamp) || timeStamp.equals(startTimeStamp))
                    {
                        double totalCases = CovidCasesCounter.countCovidCases(timeStamp.getSeatGraph());

                        String startHour = String.valueOf(timeStamp.getDay().getHourSpan().getStartHour());

                        if (!showByElements.contains(startHour))
                        {
                            showByElements.add(startHour);
                            covidCases.add(0.0);
                        }

                        int index;

                        index = showByElements.indexOf(startHour);

                        covidCases.set(index, covidCases.get(index) + totalCases);

                    }
                }
            }

            // Sorting the hours accordingly

            // Group both elements for sorting
            ArrayList<MultipleSorting> sorted = new ArrayList<>();

            for (int i = 0; i < showByElements.size(); i++)
            {
                sorted.add(new MultipleSorting(Integer.parseInt(showByElements.get(i)), covidCases.get(i)));
            }

            Collections.sort(sorted);

            // Format the hours correctly and set the corresponding Attendance Rates
            for (int i = 0; i < showByElements.size(); i++)
            {
                showByElements.set(i, sorted.get(i).xaxis + ":00");
                covidCases.set(i, sorted.get(i).yaxis);
            }

            sorted.clear();

        }

        return covidCases;
    }


    //
    // Takes a list of data, the average of that and a percentage factor (* 100, * 1, etc.) depending on
    // What the data represents (in the case of rates, percentageFactor is 100)
    //
    public double calculateStandardDeviation(ArrayList<Double> yAxis, double average, int percentageFactor)
    {
        double result = 0;

        int n = yAxis.size(); // Sample size

        double numerator = 0; // Numerator

        for (int i = 0; i < n; i++)
        {
            if (yAxis.get(i) != 0)
                numerator += Math.pow((yAxis.get(i) - average) * percentageFactor, 2);
            else
                n--;
        }

        if (n - 1 == 0)
            return 0;

        result = Math.sqrt(numerator / (n - 1)); // Standard Deviation of sample (n - 1)

        return result;
    }


    public double calculateAverage(ArrayList<Double> yAxis)
    {
        return yAxis.stream()
                    .mapToDouble(e -> e)
                    .average()
                    .orElse(0);
    }

    public double calculateMax(ArrayList<Double> yAxis)
    {
        return yAxis.stream()
                    .mapToDouble(e -> e)
                    .max()
                    .orElse(0);
    }

    public double calculateMin(ArrayList<Double> yAxis)
    {
        return yAxis.stream()
                    .mapToDouble(e -> e)
                    .min()
                    .orElse(0);
    }
}
