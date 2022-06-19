/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class represents a Course. Each course has one professor, a course ID, a course name and a semester number (1 - 8).
 |
 |
*/

package com.pasoftxperts.covidetect.course;

import com.pasoftxperts.covidetect.professor.Professor;

import java.io.Serializable;

public class Course implements Serializable
{
    private String courseId;
    private String courseName;
    private int semesterNum;
    private Professor professor;

    public Course(String courseId, String courseName, int semesterNum)
    {
        this.courseId = courseId;
        this.courseName = courseName;
        this.semesterNum = semesterNum;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getSemesterNum() {
        return semesterNum;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setSemesterNum(int semesterNum) {
        this.semesterNum = semesterNum;
    }

    public void addProfessor(Professor professor)
    {
        this.professor = professor;
    }

    public Professor getProfessor() { return this.professor; }
}