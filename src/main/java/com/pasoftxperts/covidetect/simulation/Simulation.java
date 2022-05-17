package com.pasoftxperts.covidetect.simulation;

import com.pasoftxperts.covidetect.student.Student;
import com.pasoftxperts.covidetect.time.HourSpan;
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
    public static final int NUMBER_OF_ROOMS = 8;
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

        // !! Something here !!

        // Create a list with the months
        ArrayList<Month> months = new ArrayList<>();
        months.add(Month.JANUARY);
        months.add(Month.FEBRUARY);
        months.add(Month.MARCH);
        months.add(Month.APRIL);
        months.add(Month.MAY);
        months.add(Month.JUNE);
        months.add(Month.JULY);
        months.add(Month.AUGUST);
        months.add(Month.SEPTEMBER);
        months.add(Month.OCTOBER);
        months.add(Month.NOVEMBER);
        months.add(Month.DECEMBER);

        for (int year = START_YEAR; year <= END_YEAR; year++)
        {
            for (int i = 0; i < months.size(); i++)
            {

            }
        }
    }
}
