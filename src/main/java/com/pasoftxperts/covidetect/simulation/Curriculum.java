/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used for creating hard coded curriculums (Spring and Fall Semester).
 | Its usage is only for demonstration purposes. It only exists to serve as input data for our system.
 |
 |
 | Design:
 | Spring and Fall curriculums are separated to two ArrayLists (springSemesterCurriculum), (fallSemesterCurriculum)
 | We also create two other parallel ArrayLists (fallSemesterRoomIdList), (springSemesterRoomIdList) that indicate
 | which HourSpans belong to which rooms (visual representation example below):
 |
 |
 | -> springSemesterCurriculum AND fallSemesterCurriculum <--
 | [0]            [1]             [2]               [3]              [4]              [5]              [6]
 | [MONDAY]   -   [TUESDAY]   -   [WEDNESDAY]   -   [THURSDAY]   -   [FRIDAY]    -    [SATURDAY]   -   [SUNDAY]
 | [HOURSPAN1]    [HOURSPAN3]     [HOURSPAN4]       [HOURSPAN7]      [ ]              [ ]              [ ]
 | [HOURSPAN2]                    [HOURSPAN5]
 |                                [HOURSPAN6]
 |
 | -> fallSemesterRoomIdList AND springSemesterRoomIdList <--
 | [0]            [1]             [2]               [3]              [4]              [5]              [6]
 | [MONDAY]  -   [TUESDAY]    -   [WEDNESDAY]   -   [THURSDAY]   -   [FRIDAY]    -    [SATURDAY]   -   [SUNDAY]
 | [ROOM1]       [ROOM3]          [ROOM2]           [ROOM5]          [ ]              [ ]              [ ]
 | [ROOM2]                        [ROOM4]
 |                                [ROOM1]
 |
 |
 | Method Documentation:
 |     [*] public static ArrayList<String> initializeCurriculum(ArrayList<ArrayList<HourSpan>> fallSemesterCurriculum,
 |                                                                        ArrayList<ArrayList<HourSpan>> springSemesterCurriculum,
 |                                                                        ArrayList<String> fallSemesterRoomIdList,
 |                                                                        ArrayList<String> springSemesterRoomIdList)
 |         Initializes the curriculum and returns the professor names.
 |         (Populates [springSemesterCurriculum], [fallSemesterCurriculum], [fallSemesterRoomIdList], [springSemesterRoomIdList] lists)
 |
 |     [*] public static void createHardCodedCourses()
 |         Creates hard coded courses and professor names
 |
 |     [*] public static Course findCourseById(String courseId)
 |         Takes a course ID as input. Returns the course, if found. If not found, returns a new course with course ID of "Not Found"
 |
*/

package com.pasoftxperts.covidetect.simulation;

import com.pasoftxperts.covidetect.course.Course;
import com.pasoftxperts.covidetect.professor.Professor;
import com.pasoftxperts.covidetect.time.HourSpan;

import java.util.ArrayList;

public class Curriculum
{


    // We create two lists that keep track of the curriculum for fall and spring semesters.
    // Each table will have 7 rows representing [Monday - Sunday] with values 0-6.
    public static ArrayList<String> initializeCurriculum(ArrayList<ArrayList<HourSpan>> fallSemesterCurriculum,
                                                                   ArrayList<ArrayList<HourSpan>> springSemesterCurriculum,
                                                                   ArrayList<String> fallSemesterRoomIdList,
                                                                   ArrayList<String> springSemesterRoomIdList)
    {
        // List of courses.
        ArrayList<Course> courseList = new ArrayList<>();

        // List of professors.
        ArrayList<Professor> professorList = new ArrayList<>();

        // List of names
        ArrayList<String> professorNameList = new ArrayList<>();


        // Create Courses
        createHardCodedCourses(courseList, professorNameList, professorList);

        /*
        |  We start with odd semesters [1, 3, 5, 7]
        |
        |  For each day, we create a temp HourSpan list which will represent the day's lectures and hours.
        |  We distribute the courses for each semester [fall-spring] as:
        |  [4 (MONDAY) + 4 (TUESDAY) + 5 (WEDNESDAY) + 5 (THURSDAY) + 6 (FRIDAY)]
        */


                                            /*  !! FALL SEMESTER !! */

                                         /* MONDAY - FALL SEMESTER */
        ArrayList<HourSpan> currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(8, 11, findCourseById("AIC21001", courseList)));
        fallSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(13, 14, findCourseById("AIC21021", courseList)));
        fallSemesterRoomIdList.add("Room 11");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21041", courseList)));
        fallSemesterRoomIdList.add("Room 15");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21061", courseList)));
        fallSemesterRoomIdList.add("Room 13");

        // We add the day.
        fallSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* TUESDAY - FALL SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(10, 13, findCourseById("AIC21002", courseList)));
        fallSemesterRoomIdList.add("Room 17");

        currentHourSpanList.add(new HourSpan(12, 13, findCourseById("AIC21022", courseList)));
        fallSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(20, 22, findCourseById("AIC21042", courseList)));
        fallSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(15, 16, findCourseById("AIC21062", courseList)));
        fallSemesterRoomIdList.add("Room 15");

        // We add the day.
        fallSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* WEDNESDAY - FALL SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(9, 12, findCourseById("AIC21003", courseList)));
        fallSemesterRoomIdList.add("Room 16");

        currentHourSpanList.add(new HourSpan(9, 12, findCourseById("AIC21004", courseList)));
        fallSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21023", courseList)));
        fallSemesterRoomIdList.add("Room 14");

        currentHourSpanList.add(new HourSpan(20, 22, findCourseById("AIC21043", courseList)));
        fallSemesterRoomIdList.add("Room 20");

        currentHourSpanList.add(new HourSpan(13, 14, findCourseById("AIC21063", courseList)));
        fallSemesterRoomIdList.add("Room 13");

        // We add the day.
        fallSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* THURSDAY - FALL SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(9, 12, findCourseById("AIC21005", courseList)));
        fallSemesterRoomIdList.add("Room 13");

        currentHourSpanList.add(new HourSpan(19, 20, findCourseById("AIC21006", courseList)));
        fallSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21024", courseList)));
        fallSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(13, 15, findCourseById("AIC21044", courseList)));
        fallSemesterRoomIdList.add("Room 15");

        currentHourSpanList.add(new HourSpan(10, 11, findCourseById("AIC21064", courseList)));
        fallSemesterRoomIdList.add("Room 16");

        // We add the day.
        fallSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* FRIDAY - FALL SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21025", courseList)));
        fallSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(13, 15, findCourseById("AIC21045", courseList)));
        fallSemesterRoomIdList.add("Room 15");

        currentHourSpanList.add(new HourSpan(10, 11, findCourseById("AIC21065", courseList)));
        fallSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(11, 13, findCourseById("AIC21026", courseList)));
        fallSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(13, 15, findCourseById("AIC21046", courseList)));
        fallSemesterRoomIdList.add("Room 17");

        currentHourSpanList.add(new HourSpan(10, 11, findCourseById("AIC21066", courseList)));
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

        currentHourSpanList.add(new HourSpan(8, 11, findCourseById("AIC21011", courseList)));
        springSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(13, 14, findCourseById("AIC21031", courseList)));
        springSemesterRoomIdList.add("Room 11");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21051", courseList)));
        springSemesterRoomIdList.add("Room 15");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21071", courseList)));
        springSemesterRoomIdList.add("Room 13");

        // We add the day.
        springSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* TUESDAY - SPRING SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(10, 13, findCourseById("AIC21012", courseList)));
        springSemesterRoomIdList.add("Room 17");

        currentHourSpanList.add(new HourSpan(12, 13, findCourseById("AIC21032", courseList)));
        springSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(20, 22, findCourseById("AIC21052", courseList)));
        springSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(15, 16, findCourseById("AIC21072", courseList)));
        springSemesterRoomIdList.add("Room 15");

        // We add the day.
        springSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* WEDNESDAY - SPRING SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(9, 12, findCourseById("AIC21013", courseList)));
        springSemesterRoomIdList.add("Room 16");

        currentHourSpanList.add(new HourSpan(9, 12, findCourseById("AIC21014", courseList)));
        springSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21033", courseList)));
        springSemesterRoomIdList.add("Room 14");

        currentHourSpanList.add(new HourSpan(20, 22, findCourseById("AIC21053", courseList)));
        springSemesterRoomIdList.add("Room 20");

        currentHourSpanList.add(new HourSpan(13, 14, findCourseById("AIC21073", courseList)));
        springSemesterRoomIdList.add("Room 13");

        // We add the day.
        springSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                        /* THURSDAY - SPRING SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(9, 12, findCourseById("AIC21015", courseList)));
        springSemesterRoomIdList.add("Room 13");

        currentHourSpanList.add(new HourSpan(19, 20, findCourseById("AIC21016", courseList)));
        springSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21034", courseList)));
        springSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(13, 15, findCourseById("AIC21054", courseList)));
        springSemesterRoomIdList.add("Room 15");

        currentHourSpanList.add(new HourSpan(10, 11, findCourseById("AIC21074", courseList)));
        springSemesterRoomIdList.add("Room 16");

        // We add the day.
        springSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                            /* FRIDAY - SPRING SEMESTER */
        currentHourSpanList = new ArrayList<>();

        currentHourSpanList.add(new HourSpan(16, 18, findCourseById("AIC21035", courseList)));
        springSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(13, 15, findCourseById("AIC21055", courseList)));
        springSemesterRoomIdList.add("Room 15");

        currentHourSpanList.add(new HourSpan(10, 11, findCourseById("AIC21075", courseList)));
        springSemesterRoomIdList.add("Room 12");

        currentHourSpanList.add(new HourSpan(11, 13, findCourseById("AIC21036", courseList)));
        springSemesterRoomIdList.add("Room 18");

        currentHourSpanList.add(new HourSpan(13, 15, findCourseById("AIC21056", courseList)));
        springSemesterRoomIdList.add("Room 17");

        currentHourSpanList.add(new HourSpan(10, 11, findCourseById("AIC21076", courseList)));
        springSemesterRoomIdList.add("Room 16");

        // We add the day.
        springSemesterCurriculum.add(new ArrayList<>(currentHourSpanList));


                                            /* SATURDAY - SPRING SEMESTER */
        springSemesterCurriculum.add(null); // No courses in the weekend.


                                            /* SUNDAY - SPRING SEMESTER */
        springSemesterCurriculum.add(null); // No courses in the weekend.

        return professorNameList;
    }


    //
    // Method that creates hard coded courses that match those of University of Macedonia (Professor names are random)
    //
    public static void createHardCodedCourses(ArrayList<Course> courseList, ArrayList<String> professorNameList, ArrayList<Professor> professorList)
    {
        // Create courses
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


        // Create Names
        professorNameList.add("James");
        professorNameList.add("Bob");
        professorNameList.add("John");
        professorNameList.add("Alex");
        professorNameList.add("Eric");
        professorNameList.add("Robert");
        professorNameList.add("Michael");
        professorNameList.add("Susan");
        professorNameList.add("Rachel");
        professorNameList.add("Chris");
        professorNameList.add("Mary");
        professorNameList.add("Stephanie");
        professorNameList.add("Sophie");
        professorNameList.add("Thomas");
        professorNameList.add("Mark");
        professorNameList.add("Donald");
        professorNameList.add("Steven");
        professorNameList.add("Paul");
        professorNameList.add("Olivia");
        professorNameList.add("Emma");
        professorNameList.add("Isabella");
        professorNameList.add("Elizabeth");
        professorNameList.add("Chloe");
        professorNameList.add("Madison");
        professorNameList.add("Bruce");
        professorNameList.add("Nathan");
        professorNameList.add("Adam");
        professorNameList.add("Walter");
        professorNameList.add("Jeremy");
        professorNameList.add("Harold");
        professorNameList.add("Violet");
        professorNameList.add("Zoe");
        professorNameList.add("James");
        professorNameList.add("James");
        professorNameList.add("Eliana");
        professorNameList.add("Alfred");
        professorNameList.add("Jordan");
        professorNameList.add("Madelyn");
        professorNameList.add("Logan");
        professorNameList.add("Gabriel");
        professorNameList.add("Wayne");
        professorNameList.add("Delilah");
        professorNameList.add("Ruby");
        professorNameList.add("Vincent");
        professorNameList.add("Eugene");
        professorNameList.add("Autumn");
        professorNameList.add("Mason");
        professorNameList.add("Philip");

        // Add professors to list
        for (int i = 0; i < courseList.size(); i++)
        {
            professorList.add(new Professor("ID" + (i + courseList.size()),
                    "Professor " + professorNameList.get(i),
                    professorNameList.get(i).toLowerCase() + "@uom.edu.gr"));
        }

        // Add professor to courses
        for (int i = 0; i < courseList.size(); i++)
            courseList.get(i).addProfessor(professorList.get(i));
    }


    //
    // Find and returns a course by ID. If not found, returns a Course with ID = "Not Found".
    //
    public static Course findCourseById(String courseId, ArrayList<Course> courseList)
    {
        return courseList.stream()
                         .filter(e -> e.getCourseId().equals(courseId))
                         .findFirst()
                         .orElse(new Course("Not Found","",0));
    }
}
