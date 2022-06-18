/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used for creating a simulation (university, department, students, covid cases, possible cases) and saving
 | Its usage is only for demonstration purposes. It only exists to serve as input data for our system.
 |
 |
 | Design:
 |
 | - The TOTAL_COURSES for this simulation are 48
 | - The START_YEAR is 2020
 | - The END_YEAR is 2022 (but it goes till January 24th 2023, end of semester)
 | - The NUMBER_OF_ROOMS is 9
 | - The LOWER_BOUND of total students in this department and university is 700
 | - The UPPER_BOUND of total students in this department and university is 1000
 |
 |   In order to populate the graphs, we firstly set all the students' health indicators to 0 (healthy)
 |   After that, every 5 days, we reset the health indicators randomly for all students with a covid case probability of 4% (see line 288)
 |
 |   We populate the seats randomly, at random seats each time and with random students
 |   (We don't check if a certain student is attending all semester's course for the sake of this class being just a simulation)
 |
 |
 |
 | Method Documentation:
 |
 |     [*] public static void runSimulation()
 |         Creates a random simulation (populated room objects) and saves the room objects, room names and professor names to .ser files
 |
 |     [*] public static ArrayList<ArrayList<Seat>> populateWithStudents(ArrayList<Student> studentList, int rows, int cols)
 |         Takes an ArrayList of Students and the rows/cols of the seats of a room as input. Returns a 2D ArrayList that's populated with students
 |
 |     [*] public static Room findRoomById(String roomId, ArrayList<Room> roomList)
 |         Takes a room and an ArrayList of rooms as input. Returns a room with that room ID, if found.
 |         If not found, returns a new room object with a room ID of "Room not found"
 |
*/

package com.pasoftxperts.covidetect.simulation;

import com.pasoftxperts.covidetect.filemanager.FileWrapper;
import com.pasoftxperts.covidetect.graphanalysis.GraphNeighboursGenerator;
import com.pasoftxperts.covidetect.student.Student;
import com.pasoftxperts.covidetect.time.Day;
import com.pasoftxperts.covidetect.time.HourSpan;
import com.pasoftxperts.covidetect.time.TimeStamp;
import com.pasoftxperts.covidetect.university.Department;
import com.pasoftxperts.covidetect.university.Room;
import com.pasoftxperts.covidetect.university.Seat;
import com.pasoftxperts.covidetect.university.University;
import org.apache.commons.lang3.SerializationUtils;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Simulation
{
    public static final int TOTAL_COURSES = 48;
    public static final int LOWER_BOUND = 700; // Lower bound for number of students
    public static final int UPPER_BOUND = 1000; // Upper bound for number of students
    public static final int NUMBER_OF_ROOMS = 9;
    public static final int START_YEAR = 2020; // Year the simulation starts
    public static final int END_YEAR = 2022; // Year the simulation ends

    public static void runSimulation()
    {
        // We use an arraylist for students because we are going to search for students really often.
        // If we were frequently adding/removing students, we would use a linked list.
        ArrayList<Student> studentList = new ArrayList<>();

        // List of attending students
        ArrayList<Student> attendingStudents = new ArrayList<>();

        // Seats of the current room
        ArrayList<ArrayList<Seat>> seats;

        // Seat graph
        DefaultUndirectedGraph<Seat, Integer> graph;

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

            studentList.add(new Student("AI" + (idNumberCounter + i*8), healthIndicator));
        }

        // We now create a reference for which rooms have which HourSpans and for which days

        // Fall Semester Curriculum
        ArrayList<ArrayList<HourSpan>> fallSemesterCurriculum = new ArrayList<>();
        ArrayList<String> fallSemesterRoomIdList = new ArrayList<>();

        // Spring Semester Curriculum
        ArrayList<ArrayList<HourSpan>> springSemesterCurriculum = new ArrayList<>();
        ArrayList<String> springSemesterRoomIdList = new ArrayList<>();

        // Initialize Curriculum
        ArrayList<String> professorNameList = Curriculum.initializeCurriculum(fallSemesterCurriculum,
                                                                              springSemesterCurriculum,
                                                                              fallSemesterRoomIdList,
                                                                              springSemesterRoomIdList);

        // Create Rooms
        ArrayList<Room> roomList = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_ROOMS; i++)
        {
            roomList.add(new Room("Room " + (12 + i), 81, 9));
        }

        // Create a list with the months
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
            // We need to keep track of which semester we are currently
            String semester = "";
            String prevSemester = ""; // Will help us know if we transitioned to a new semester

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
                        prevSemester = "Fall";
                        startDay = 24;
                    }
                    case OCTOBER -> {
                        semester = "Fall";
                        prevSemester = "Spring";
                    }
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
                    if (!TimeStamp.convertDayToText(year, months.get(i), day).equals("Saturday") &&
                            !TimeStamp.convertDayToText(year, months.get(i), day).equals("Sunday"))
                    {
                        // We get the index for the Semester Curriculum table
                        index = TimeStamp.getDayOfWeekIndex(year, months.get(i), day);

                        if (semester.equals("Fall"))
                        {
                            // -1 means that a new semester has come
                            if (prevSemester.equals("Spring"))
                            {
                                roomIdCounter = -1;
                                prevSemester = "Fall"; // Reset it to "Fall" so that we won't assign -1 to the room counter again in the same semester
                            }

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

        // We need to randomly pick students to turn to covid cases
        Random random = new Random(System.currentTimeMillis());

        // We've populated the rooms with the appropriate TimeStamps based on the Curriculum
        // What's left is creating the graphs for each room and passing students through
        Room room;
        ArrayList<TimeStamp> timeStamps;

        for (int i = 0; i < roomList.size(); i++)
        {
            room = roomList.get(i);

            timeStamps = room.getTimeStampList();

            // Counter to keep track of how many days pass so that the health indicator status changes
            // Typically, it will be 5 days
            int dayCounter = 5;

            // Previous day number. Lets us know when to increment the dayCounter.
            int prevDay = 0;

            for (int j = 0; j < timeStamps.size(); j++)
            {
                if (dayCounter >= 5)
                {
                    dayCounter = 0;

                    // Reset health indicators
                    for (int k = 0; k < studentList.size(); k++)
                        studentList.get(k).setHealthIndicator(0);

                    // With a certain low probability, pick a number of students that are going to be a covid case
                    // from studentList
                    double probability = 0.03;

                    for (int k = 0; k < studentList.size(); k++)
                    {
                        if (random.nextFloat() < probability)
                            studentList.get(k).setHealthIndicator(1);
                    }
                }

                seats = populateWithStudents(studentList, attendingStudents, room.getSeatRows(), room.getSeatColumns());

                // Calculate Neighbours
                graph = GraphNeighboursGenerator.calculateNeighboursGraph(seats,
                                                                          room.getSeatRows(),
                                                                          room.getSeatColumns());

                // Add graph to timestamp
                timeStamps.get(j).addSeatGraph(SerializationUtils.clone(graph));

                int thisDay = timeStamps.get(j).getDay().getDayNumber();

                if (prevDay == 0) // First time in loop
                {
                    prevDay = thisDay;
                }
                else
                {
                    if (prevDay < thisDay)
                    {
                        dayCounter += thisDay - prevDay;
                        prevDay = thisDay;
                    }
                    else if (prevDay > thisDay)
                    {
                        Month thisMonth = timeStamps.get(j).getMonth();
                        int thisYear = timeStamps.get(j).getYear();

                        dayCounter += thisMonth.length((thisYear%4==0) || (thisYear%400==0)) - prevDay + thisDay;
                        prevDay = thisDay;
                    }
                }
            }
        }

        FileWrapper.saveFilesByRoom(university.getUniversityName(), appliedInformatics.getDepartmentName(), roomList);

        FileWrapper.saveProfessorNames(university.getUniversityName(), appliedInformatics.getDepartmentName(), professorNameList);

        System.gc();
    }

    public static Room findRoomById(String roomId, ArrayList<Room> roomList)
    {
        return roomList.stream()
                .filter(e -> e.getRoomId().equals(roomId))
                .findFirst()
                .orElse(new Room("Room not found", 15, 3));
    }

    public static ArrayList<ArrayList<Seat>> populateWithStudents(ArrayList<Student> studentList,
                                                                  ArrayList<Student> attendingStudents,
                                                                  int rows,
                                                                  int cols)
    {
        Random random = new Random(System.currentTimeMillis());

        int capacity = rows * cols;

        int lowBound = 10; // 10 students at the lowest
        int upperBound = capacity; //  Max number of students at the highest

        // Randomly determine how many students will attend a course in a TimeStamp (date)
        int attendanceNumber = random.nextInt(upperBound - lowBound) + lowBound;

        // Randomly pick attendanceNumber of students from students list

        // Create a list and shuffle it to get unique random indexes for students
        ArrayList<Integer> studentIndexes = new ArrayList<>();
        for (int i = 0; i < studentList.size(); i++)
            studentIndexes.add(i);

        Collections.shuffle(studentIndexes);

        for (int i = 0; i < attendanceNumber; i++)
        {
            int index = studentIndexes.get(i);

            attendingStudents.add(SerializationUtils.clone(studentList.get(index)));
        }

        // Create and add seats to list
        ArrayList<ArrayList<Seat>> roomSeats = new ArrayList<>();

        for (int i = 0; i < rows; i++)
        {
            ArrayList<Seat> rowSeats = new ArrayList<>();

            for (int j = 0; j < cols; j++)
            {
                rowSeats.add(new Seat(GraphNeighboursGenerator.numberOfSeat(i, j, cols)));
            }

            roomSeats.add(SerializationUtils.clone(rowSeats));
        }

        // Create a list and shuffle it to get unique random indexes for where students will be sitting
        ArrayList<Integer> seatNumberList = new ArrayList<>();
        for (int i = 0; i < capacity; i++)
            seatNumberList.add(i + 1);

        Collections.shuffle(seatNumberList);

        // We now add the attending students to random seats
        for (int i = 0; i < attendanceNumber; i++)
        {
            int seatNumber = seatNumberList.get(i);

            int row = ((seatNumber - 1) / cols);
            int col = ((seatNumber - 1) % cols);

            // Add Student
            roomSeats.get(row).get(col).occupySeat(SerializationUtils.clone(attendingStudents.get(i)));
        }

        return roomSeats;
    }
}
