package com.pasoftxperts.covidetect.simulation;

import com.pasoftxperts.covidetect.course.Course;
import com.pasoftxperts.covidetect.time.HourSpan;

import java.util.ArrayList;

public class Curriculum
{
    // We create two lists that keep track of the curriculum for fall and spring semesters.
    // Each table will have 7 rows representing [Monday - Sunday] with values 0-6.
    private static ArrayList<ArrayList<HourSpan>> fallSemesterCurriculum = new ArrayList<>();
    private static ArrayList<ArrayList<HourSpan>> springSemesterCurriculum = new ArrayList<>();

    // List of courses.
    private static ArrayList<Course> courseList = new ArrayList<>();

    // A table in which we will keep track of which HourSpan belongs to which room.
    private static ArrayList<String> fallSemesterRoomIdList = new ArrayList<>();
    private static ArrayList<String> springSemesterRoomIdList = new ArrayList<>();

    public static void initializeCurriculum()
    {
        // Create Courses
        createHardCodedCourses();

        // We start with odd semesters [1, 3, 5, 7]

        // For each day, we create a temp HourSpan list which will represent the day's lectures and hours.

        // We distribute the courses for each semester [fall-spring] as:
        // [4 (MONDAY) + 4 (TUESDAY) + 5 (WEDNESDAY) + 5 (THURSDAY) + 6 (FRIDAY)]


                                            /*  !! FALL SEMESTER !! */

                                         /* MONDAY - FALL SEMESTER */
        ArrayList<HourSpan> currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(8, 11, findCourseById("AIC21001")));
        fallSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(13, 14, findCourseById("AIC21021")));
        fallSemesterRoomIdList.add("Room 11");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21041")));
        fallSemesterRoomIdList.add("Room 15");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21061")));
        fallSemesterRoomIdList.add("Room 13");

        // We add the day.
        fallSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* TUESDAY - FALL SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(10, 13, findCourseById("AIC21002")));
        fallSemesterRoomIdList.add("Room 17");

        currentHourSpanList.add(new HourSpan(12, 13, findCourseById("AIC21022")));
        fallSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(20, 22, findCourseById("AIC21042")));
        fallSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(15, 16, findCourseById("AIC21062")));
        fallSemesterRoomIdList.add("Room 15");

        // We add the day.
        fallSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* WEDNESDAY - FALL SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(9, 12, findCourseById("AIC21003")));
        fallSemesterRoomIdList.add("Room 16");

        currentHourSpanList.add(new HourSpan(9, 12, findCourseById("AIC21004")));
        fallSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21023")));
        fallSemesterRoomIdList.add("Room 14");

        currentHourSpanList.add(new HourSpan(20, 22, findCourseById("AIC21043")));
        fallSemesterRoomIdList.add("Room 20");

        currentHourSpanList.add(new HourSpan(13, 14, findCourseById("AIC21063")));
        fallSemesterRoomIdList.add("Room 13");

        // We add the day.
        fallSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* THURSDAY - FALL SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(9, 12, findCourseById("AIC21005")));
        fallSemesterRoomIdList.add("Room 13");

        currentHourSpanList.add(new HourSpan(19, 20, findCourseById("AIC21006")));
        fallSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21024")));
        fallSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(13, 15, findCourseById("AIC21044")));
        fallSemesterRoomIdList.add("Room 15");

        currentHourSpanList.add(new HourSpan(10, 11, findCourseById("AIC21064")));
        fallSemesterRoomIdList.add("Room 16");

        // We add the day.
        fallSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* FRIDAY - FALL SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21025")));
        fallSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(13, 15, findCourseById("AIC21045")));
        fallSemesterRoomIdList.add("Room 15");

        currentHourSpanList.add(new HourSpan(10, 11, findCourseById("AIC21065")));
        fallSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21026")));
        fallSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(13, 15, findCourseById("AIC21046")));
        fallSemesterRoomIdList.add("Room 17");

        currentHourSpanList.add(new HourSpan(10, 11, findCourseById("AIC21066")));
        fallSemesterRoomIdList.add("Room 16");

        // We add the day.
        fallSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* SATURDAY - FALL SEMESTER */
        fallSemesterCurriculum.add(null); // No courses in the weekend.


                                        /* SUNDAY - FALL SEMESTER */
        fallSemesterCurriculum.add(null); // No courses in the weekend.


                                            /*  !! SPRING SEMESTER !! */

                                        /* MONDAY - SPRING SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(8, 11, findCourseById("AIC21001")));
        springSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(13, 14, findCourseById("AIC21021")));
        springSemesterRoomIdList.add("Room 11");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21041")));
        springSemesterRoomIdList.add("Room 15");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21061")));
        springSemesterRoomIdList.add("Room 13");

        // We add the day.
        springSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* TUESDAY - SPRING SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(10, 13, findCourseById("AIC21002")));
        springSemesterRoomIdList.add("Room 17");

        currentHourSpanList.add(new HourSpan(12, 13, findCourseById("AIC21022")));
        springSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(20, 22, findCourseById("AIC21042")));
        springSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(15, 16, findCourseById("AIC21062")));
        springSemesterRoomIdList.add("Room 15");

        // We add the day.
        springSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* WEDNESDAY - SPRING SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(9, 12, findCourseById("AIC21003")));
        springSemesterRoomIdList.add("Room 16");

        currentHourSpanList.add(new HourSpan(9, 12, findCourseById("AIC21004")));
        springSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21023")));
        springSemesterRoomIdList.add("Room 14");

        currentHourSpanList.add(new HourSpan(20, 22, findCourseById("AIC21043")));
        springSemesterRoomIdList.add("Room 20");

        currentHourSpanList.add(new HourSpan(13, 14, findCourseById("AIC21063")));
        springSemesterRoomIdList.add("Room 13");

        // We add the day.
        springSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* THURSDAY - SPRING SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(9, 12, findCourseById("AIC21005")));
        springSemesterRoomIdList.add("Room 13");

        currentHourSpanList.add(new HourSpan(19, 20, findCourseById("AIC21006")));
        springSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21024")));
        springSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(13, 15, findCourseById("AIC21044")));
        springSemesterRoomIdList.add("Room 15");

        currentHourSpanList.add(new HourSpan(10, 11, findCourseById("AIC21064")));
        springSemesterRoomIdList.add("Room 16");

        // We add the day.
        springSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                            /* FRIDAY - SPRING SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21025")));
        springSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(13, 15, findCourseById("AIC21045")));
        springSemesterRoomIdList.add("Room 15");

        currentHourSpanList.add(new HourSpan(10, 11, findCourseById("AIC21065")));
        springSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21026")));
        springSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(13, 15, findCourseById("AIC21046")));
        springSemesterRoomIdList.add("Room 17");

        currentHourSpanList.add(new HourSpan(10, 11, findCourseById("AIC21066")));
        springSemesterRoomIdList.add("Room 16");

        // We add the day.
        springSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                            /* SATURDAY - SPRING SEMESTER */
        springSemesterCurriculum.add(null); // No courses in the weekend.


                                            /* SUNDAY - SPRING SEMESTER */
        springSemesterCurriculum.add(null); // No courses in the weekend.
    }

    public static void createHardCodedCourses()
    {
        courseList.add(new Course("AIC21001", "Imperative Programming", 1));
        courseList.add(new Course("AIC21002", "Math Analysis", 1));
        courseList.add(new Course("AIC21003", "Linear Algebra", 1));
        courseList.add(new Course("AIC21004", "Algorithms", 1));
        courseList.add(new Course("AIC21005", "Computer Science and Technology", 1));
        courseList.add(new Course("AIC21006", "Computer Systems", 1));
        courseList.add(new Course("AIC21011", "Computer Architecture", 2));
        courseList.add(new Course("AIC21012", "Databases", 2));
        courseList.add(new Course("AIC21013", "Discrete Mathematics", 2));
        courseList.add(new Course("AIC21014", "Administration and Technology", 2));
        courseList.add(new Course("AIC21015", "Data Structures", 2));
        courseList.add(new Course("AIC21016", "Probability Theory", 2));
        courseList.add(new Course("AIC21021", "Object Oriented Programming", 3));
        courseList.add(new Course("AIC21022", "Computer Networking", 3));
        courseList.add(new Course("AIC21023", "Operating Systems", 3));
        courseList.add(new Course("AIC21024", "Information Systems", 3));
        courseList.add(new Course("AIC21025", "Financial Accounting", 3));
        courseList.add(new Course("AIC21026", "Statistics", 3));
        courseList.add(new Course("AIC21031", "Human-Computer Interaction", 4));
        courseList.add(new Course("AIC21032", "Information Systems Security", 4));
        courseList.add(new Course("AIC21033", "Web Programming", 4));
        courseList.add(new Course("AIC21034", "Digital Economics", 4));
        courseList.add(new Course("AIC21035", "Algorithms Analysis", 4));
        courseList.add(new Course("AIC21036", "Software Technology", 4));
        courseList.add(new Course("AIC21041", "Communication Systems", 5));
        courseList.add(new Course("AIC21042", "Web Services and Transactions", 5));
        courseList.add(new Course("AIC21043", "Multimedia Communication and Technology", 5));
        courseList.add(new Course("AIC21044", "E-Commerce Technology", 5));
        courseList.add(new Course("AIC21045", "Digital Marketing", 5));
        courseList.add(new Course("AIC21046", "Accounting Computerization", 5));
        courseList.add(new Course("AIC21051", "Quality Assurance and Control Techniques", 6));
        courseList.add(new Course("AIC21052", "Mobile App Development", 6));
        courseList.add(new Course("AIC21053", "Mobile and Diffuse Computing", 6));
        courseList.add(new Course("AIC21054", "Programming Languages and Compilers", 6));
        courseList.add(new Course("AIC21055", "Calculation Theory", 6));
        courseList.add(new Course("AIC21056", "Cloud Computing", 6));
        courseList.add(new Course("AIC21061", "Cryptography", 7));
        courseList.add(new Course("AIC21062", "Theory of Calculation and Automation", 7));
        courseList.add(new Course("AIC21063", "Ethics and Governance of Artificial Intelligence", 7));
        courseList.add(new Course("AIC21064", "eGovernment", 7));
        courseList.add(new Course("AIC21065", "Business Innovation and Productivity", 7));
        courseList.add(new Course("AIC21066", "Internet Law", 7));
        courseList.add(new Course("AIC21071", "Quality Assurance and Control Techniques", 8));
        courseList.add(new Course("AIC21072", "Mobile App Development", 8));
        courseList.add(new Course("AIC21073", "Mobile and Diffuse Computing", 8));
        courseList.add(new Course("AIC21074", "Programming Languages and Compilers", 8));
        courseList.add(new Course("AIC21075", "Calculation Theory", 8));
        courseList.add(new Course("AIC21076", "Cloud Computing", 8));
    }

    // Find and returns a course by ID. If not found, returns a Course with ID = "Not Found".
    public static Course findCourseById(String courseId)
    {
        return courseList.stream()
                .filter(e -> e.getCourseId().equals(courseId))
                .findFirst()
                .orElse(new Course("Not Found","",0));
    }

    public static ArrayList<ArrayList<HourSpan>> getFallSemesterCurriculum() { return fallSemesterCurriculum; }

    public static ArrayList<ArrayList<HourSpan>> getSpringSemesterCurriculum() { return springSemesterCurriculum; }

    public static ArrayList<Course> getCourseList() { return courseList; }

    public static ArrayList<String> getFallSemesterRoomIdList() { return fallSemesterRoomIdList; }

    public static ArrayList<String> getSpringSemesterRoomIdList() { return springSemesterRoomIdList; }
}
