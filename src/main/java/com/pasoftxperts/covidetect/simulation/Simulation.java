package com.pasoftxperts.covidetect.simulation;

import com.pasoftxperts.covidetect.student.Student;
import com.pasoftxperts.covidetect.time.Day;
import com.pasoftxperts.covidetect.time.HourSpan;
import com.pasoftxperts.covidetect.time.TimeStamp;
import com.pasoftxperts.covidetect.university.Department;
import com.pasoftxperts.covidetect.university.Room;
import com.pasoftxperts.covidetect.university.University;

import java.time.Month;
import java.util.ArrayList;
import java.util.Random;

public class Simulation
{
    public static final int TOTAL_COURSES = 48;
    public static final int LOWER_BOUND = 700; // Lower bound for number of students
    public static final int UPPER_BOUND = 1000; // Upper bound for number of students
    public static final int NUMBER_OF_ROOMS = 9;
    public static final int START_YEAR = 2020; // Year the simulation starts
    public static final int END_YEAR = 2022; // Year the simulation ends [current year]
    public static final Month END_MONTH = Month.MAY; // Month the simulation end for current year (see END_YEAR)

    // We use an arraylist for students because we are going to search for students really often.
    // If we were frequently adding/removing students, we would use a linked list.
    private static ArrayList<Student> studentList = new ArrayList<>();

    public static void runSimulation()
    {
        // Create a university
        University university = new University("UoM", "University of Macedonia");

        // Create Applied Informatics Department
        Department appliedInformatics = new Department("ICS", "Applied Informatics", TOTAL_COURSES);

        // Generate a random number of students [700, 1000]
        Random rand = new Random(System.currentTimeMillis());
        int numberOfStudents = rand.nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;

        // Create students and add them to a list
        int idNumberCounter = 0;

        for (int i=0; i<numberOfStudents; i++)
        {
            int healthIndicator = 0;

            studentList.add(new Student("AI" + String.valueOf(idNumberCounter + i*8), healthIndicator));
        }

        // Initialize Curriculum
        Curriculum.initializeCurriculum();

        // We now have a reference for which rooms have which HourSpans and for which days

        // Fall Semester Curriculum
        ArrayList<ArrayList<HourSpan>> fallSemesterCurriculum = Curriculum.getFallSemesterCurriculum();
        ArrayList<String> fallSemesterRoomIdList = Curriculum.getFallSemesterRoomIdList();

        // Spring Semester Curriculum
        ArrayList<ArrayList<HourSpan>> springSemesterCurriculum = Curriculum.getSpringSemesterCurriculum();
        ArrayList<String> springSemesterRoomIdList = Curriculum.getSpringSemesterRoomIdList();

        // Create Rooms
        ArrayList<Room> roomList = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_ROOMS; i++)
        {
            roomList.add(new Room("Room " + String.valueOf(12 + i), 81, 9));
        }

        // Create a list with the months ()
        ArrayList<Month> months = new ArrayList<>();

        months.add(Month.FEBRUARY);
        months.add(Month.MARCH);
        months.add(Month.APRIL);
        months.add(Month.MAY);
        months.add(Month.JUNE);

        months.add(Month.OCTOBER);
        months.add(Month.NOVEMBER);
        months.add(Month.DECEMBER);
        months.add(Month.JANUARY);

        int year = START_YEAR;

        while (year <= END_YEAR)
        {
            // We need to keep track of which semester we are talking about
            String semester = "";

            for (int i = 0; i < months.size(); i++)
            {
                int days = months.get(i).length((year%4==0) || (year%400==0));

                int startDay = 1; // Day number which lectures start happening in a month
                int endDay = days; // Day number which we will stop counting in a month

                // Spring Semester in February starts at approximately 24/02
                // Spring Semester in June stops at approximately 10/06
                // Fall Semester in January stops at approximately 24/01
                switch (months.get(i))
                {
                    case FEBRUARY -> {
                        semester = "Spring";
                        startDay = 24;
                    }
                    case OCTOBER -> semester = "Fall";
                    case JUNE -> endDay = 10;
                    case JANUARY -> {
                        endDay = 24;
                        year++;  // If we get to January, that means we changed a year
                    }
                }

                // Keeps track of the room id list index. Resets to 0 after completing 7 days (1 week has passed)
                int roomIdCounter = -1;

                // Keep track of how many days pass by
                int daysPassed = 0;

                // Index to keep track of which day a month starts
                int index;

                // Start populating Room TimeStamps with HourSpans
                for (int day = startDay; day <= endDay; day++)
                {
                    // We check if the day is not a Saturday nor a Sunday (No lectures)
                    if (!TimeStamp.dayToText(year, months.get(i), day).equals("Saturday") &&
                            !TimeStamp.dayToText(year, months.get(i), day).equals("Sunday"))
                    {
                        // We get the index for the Semester Curriculum table
                        index = TimeStamp.getDayValueOfWeek(year, months.get(i), day);

                        if (semester.equals("Fall"))
                        {
                            roomIdCounter = -1;

                            ArrayList<HourSpan> currentHourSpans = fallSemesterCurriculum.get(index);

                            // A week has passed. Reset the counter
                            if ((daysPassed == 7) || (roomIdCounter == -1))
                            {
                                roomIdCounter = 0;
                                daysPassed = 0;

                                for (int k = 0; k < index; k++)
                                {
                                    roomIdCounter += fallSemesterCurriculum.get(k).size();
                                }
                            }

                            for (int j = 0; j < currentHourSpans.size(); j++)
                            {
                                // Get the room id
                                String roomId = fallSemesterRoomIdList.get(roomIdCounter + j);

                                findRoomById(roomId, roomList).addTimeStamp(new TimeStamp(year, months.get(i), new Day(day, currentHourSpans.get(j))));
                            }

                            roomIdCounter += currentHourSpans.size();
                        }
                        else
                        {
                            ArrayList<HourSpan> currentHourSpans = springSemesterCurriculum.get(index);

                            // A week has passed. Reset the counter
                            if ((daysPassed == 7) || (roomIdCounter == -1))
                            {
                                roomIdCounter = 0;
                                daysPassed = 0;

                                for (int k = 0; k < index; k++)
                                {
                                    roomIdCounter += springSemesterCurriculum.get(k).size();
                                }
                            }

                            for (int j = 0; j < currentHourSpans.size(); j++)
                            {
                                // Get the room id
                                String roomId = springSemesterRoomIdList.get(roomIdCounter + j);

                                findRoomById(roomId, roomList).addTimeStamp(new TimeStamp(year, months.get(i), new Day(day, currentHourSpans.get(j))));
                            }

                            roomIdCounter += currentHourSpans.size();
                        }
                    }
                    else // Reset the counter
                    {
                        roomIdCounter = 0;
                    }

                    daysPassed++;
                }
            }
        }

        printRoomTimeStamps(roomList);
    }

    public static Room findRoomById(String roomId, ArrayList<Room> roomList)
    {
        return roomList.stream()
                .filter(e -> e.getRoomId().equals(roomId))
                .findFirst()
                .orElse(new Room("Room not found", 15, 3));
    }

    public static void printRoomTimeStamps(ArrayList<Room> roomList)
    {
        for (int i = 0; i < NUMBER_OF_ROOMS; i++)
        {
            System.out.println("-------------------------------------");
            System.out.println("Room " + roomList.get(i).getRoomId());

            ArrayList<TimeStamp> timeStamps = roomList.get(i).getTimeStampList();

            for (int j = 0; j < timeStamps.size(); j++)
            {
                System.out.println(timeStamps.size());
                HourSpan hourspan = timeStamps.get(j).getDay().getHourSpan();

                System.out.println("Date: " + timeStamps.get(j).getDateToString());
                System.out.println("Course: " + hourspan.getCourse().getCourseName() + ", ID = " + hourspan.getCourse().getCourseId());
                System.out.println("Semester: " + hourspan.getCourse().getSemesterNum());
                System.out.println("Start Hour: " + hourspan.getStartHour());
                System.out.println("End Hour: " + hourspan.getEndHour());
                System.out.println("-------------------------------------");
                System.out.println("-------------------------------------");
                System.out.println("");
            }
        }
    }
}
