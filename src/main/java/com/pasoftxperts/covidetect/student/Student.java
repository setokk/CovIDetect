/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class represents a student. A student has a studentId and a health indicator
 | (0 is healthy, 1 is a covid case, 2 is a possible case). No other values allowed.
 |
 |
*/

package com.pasoftxperts.covidetect.student;

import java.io.Serializable;

public class Student implements Serializable
{
    private String studentId;
    private int healthIndicator; // 0 == healthy. 1 == covid case. 2 == possible covid case.


    public Student(String studentId, int healthIndicator)
    {
        // Value between [0,2]
        if ((healthIndicator < 0) || (healthIndicator > 2))
            throw new IllegalArgumentException("health indicator value must be between [0,2]");

        this.healthIndicator = healthIndicator;
        this.studentId = studentId;
    }


    public String getStudentId() { return studentId; }

    public int getHealthIndicator() { return healthIndicator; }

    public void setStudentId(String studentId) { this.studentId = studentId; }

    public void setHealthIndicator(int healthIndicator)
    {
        // Value between [0,2]
        if ((healthIndicator < 0) || (healthIndicator > 2))
            throw new IllegalArgumentException("health indicator value must be between [0,2]");

        this.healthIndicator = healthIndicator;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Student))
            return false;

        Student otherStudent = (Student) o;

        return (this.studentId.equals(otherStudent.getStudentId()));
    }
}