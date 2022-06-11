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
}