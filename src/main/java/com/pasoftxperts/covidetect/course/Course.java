package com.pasoftxperts.covidetect.course;

import com.pasoftxperts.covidetect.professor.Professor;

import java.util.ArrayList;

public class Course
{
    private String courseId;
    private String courseName;
    private int semesterNum;
    private ArrayList<Professor> professorList;


    public Course(String courseId,
                  String courseName,
                  int semesterNum)
    {
        this.courseId = courseId;
        this.courseName = courseName;
        this.semesterNum = semesterNum;
    }

    public String getCourseId() { return courseId;}

    public String getCourseName() { return courseName; }

    public int getSemesterNum() { return semesterNum; }

    public void addProfessor(Professor p)
    {
        professorList.add(p);
    }
}