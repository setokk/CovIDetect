package com.pasoftxperts.covidetect.statistics;

import com.pasoftxperts.covidetect.counters.CovidCasesCounter;
import com.pasoftxperts.covidetect.counters.StudentCounter;
import com.pasoftxperts.covidetect.filemanager.ObjectListReader;
import com.pasoftxperts.covidetect.simulation.Simulation;
import com.pasoftxperts.covidetect.sorting.MultipleSorting;
import com.pasoftxperts.covidetect.time.TimeStamp;
import com.pasoftxperts.covidetect.university.Room;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticalAnalysis implements AttendanceStats, CovidCaseStats
{
    @Override
    public ArrayList<Double> calculateAttendanceRates(String startDate,
                                                      String endDate,
                                                      ArrayList<Double> minMaxAverage,
                                                      String showByOption,
                                                      ArrayList<String> showByElements,
                                                      ArrayList<Room> roomList)
    {
        // showByOption determines the X Axis of the chart (showByElements)
        switch (showByOption)
        {
            case "Yearly":
                for (int i = Simulation.START_YEAR; i <= Simulation.END_YEAR + 1; i++)
                    showByElements.add(String.valueOf(i));
                break;

            case "Monthly":
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

            case "By Day":
                showByElements.add("Monday");
                showByElements.add("Tuesday");
                showByElements.add("Wednesday");
                showByElements.add("Thursday");
                showByElements.add("Friday");
                break;

            case "By Professor":
                String path = System.getProperty("user.dir") + "/university of macedonia/applied informatics/professors/professorNames.ser";

                ArrayList<Object> tempList = ObjectListReader.readObjectListFile(path);

                List<String> nameList = tempList.stream()
                                                .map(object -> Objects.toString(object, null))
                                                .collect(Collectors.toList());

                for (int i = 0; i < nameList.size(); i++)
                    showByElements.add(nameList.get(i));
                break;

            default:
                break;
        }


        // Return statistical results array
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
        if ((!showByOption.equals("(Default)")) && (!showByOption.equals("Weekly")) && (!showByOption.equals("Hourly")))
        {
            for (Room room : roomList)
            {
                ArrayList<TimeStamp> timeStamps = room.getTimeStampList();

                for (TimeStamp timeStamp : timeStamps)
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
                        if (showByOption.equals("Yearly")) // Yearly
                        {
                            index = showByElements.indexOf(String.valueOf(timeStamp.getYear()));
                        }
                        else if (showByOption.equals("Monthly")) // Monthly
                        {
                            index = showByElements.indexOf(String.valueOf(timeStamp.getMonthName()));
                        }
                        else if (showByOption.equals("By Day")) // By Day
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
        else if (showByOption.equals("Hourly"))
        {
            for (Room room : roomList)
            {
                ArrayList<TimeStamp> timeStamps = room.getTimeStampList();

                for (TimeStamp timeStamp : timeStamps)
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

            // Sorting the hours accordingly

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

        // Initialize Min and Max
        minMaxAverage.add(0,1000000.0);
        minMaxAverage.add(1, -1.0);

        // Calculate the average, min and max
        double average = 0;

        for (int i = 0; i < attendanceRates.size(); i++)
        {
            if (showByCounter.get(i) == 0)
                attendanceRates.set(i, 0.0);
            else
                attendanceRates.set(i, attendanceRates.get(i) / showByCounter.get(i));


            if (attendanceRates.get(i) < minMaxAverage.get(0))
                minMaxAverage.set(0, attendanceRates.get(i));

            if (attendanceRates.get(i) > minMaxAverage.get(1))
                minMaxAverage.set(1, attendanceRates.get(i));

            average += attendanceRates.get(i) / attendanceRates.size();
        }

        minMaxAverage.add(2, average);

        return attendanceRates;
    }

    @Override
    public ArrayList<Double> calculateCovidCases(String startDate,
                                                  String endDate,
                                                  ArrayList<Double> minMaxAverage,
                                                  String showByOption,
                                                  ArrayList<String> showByElements,
                                                  ArrayList<Room> roomList)
    {
        // showByOption determines the X Axis of the chart (showByElements)
        switch (showByOption)
        {
            case "Yearly":
                for (int i = Simulation.START_YEAR; i <= Simulation.END_YEAR + 1; i++)
                    showByElements.add(String.valueOf(i));
                break;

            case "Monthly":
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

            case "By Day":
                showByElements.add("Monday");
                showByElements.add("Tuesday");
                showByElements.add("Wednesday");
                showByElements.add("Thursday");
                showByElements.add("Friday");
                break;

            case "By Professor":
                String path = System.getProperty("user.dir") + "/university of macedonia/applied informatics/professors/professorNames.ser";

                ArrayList<Object> tempList = ObjectListReader.readObjectListFile(path);

                List<String> nameList = tempList.stream()
                        .map(object -> Objects.toString(object, null))
                        .collect(Collectors.toList());

                for (int i = 0; i < nameList.size(); i++)
                    showByElements.add(nameList.get(i));
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


        // Calculate Attendance based on case
        if ((!showByOption.equals("(Default)")) && (!showByOption.equals("Weekly")) && (!showByOption.equals("Hourly")))
        {
            for (Room room : roomList)
            {
                ArrayList<TimeStamp> timeStamps = room.getTimeStampList();

                for (TimeStamp timeStamp : timeStamps)
                {
                    // Check if we exceeded the end date
                    if (timeStamp.isAfter(endTimeStamp) || timeStamp.equals(endTimeStamp))
                        break;

                    if (timeStamp.isAfter(startTimeStamp) || timeStamp.equals(startTimeStamp))
                    {
                        double totalCases = CovidCasesCounter.countCovidCases(timeStamp.getSeatGraph());

                        int index;

                        // We get the corresponding index of the X Axis
                        if (showByOption.equals("Yearly")) // Yearly
                        {
                            index = showByElements.indexOf(String.valueOf(timeStamp.getYear()));
                        }
                        else if (showByOption.equals("Monthly")) // Monthly
                        {
                            index = showByElements.indexOf(String.valueOf(timeStamp.getMonthName()));
                        }
                        else if (showByOption.equals("By Day")) // By Day
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
        else if (showByOption.equals("Hourly"))
        {
            for (Room room : roomList)
            {
                ArrayList<TimeStamp> timeStamps = room.getTimeStampList();

                for (TimeStamp timeStamp : timeStamps)
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

        }

        // Initialize Min and Max
        minMaxAverage.add(0,1000000.0);
        minMaxAverage.add(1, -1.0);

        // Calculate the average, min and max
        double average = 0;

        for (int i = 0; i < covidCases.size(); i++)
        {
            average += covidCases.get(i) / covidCases.size();

            if (covidCases.get(i) < minMaxAverage.get(0))
                minMaxAverage.set(0, covidCases.get(i));

            if (covidCases.get(i) > minMaxAverage.get(1))
                minMaxAverage.set(1, covidCases.get(i));
        }

        minMaxAverage.add(2, average);

        return covidCases;
    }
}
