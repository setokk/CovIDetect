package com.pasoftxperts.covidetect.university;

import java.util.ArrayList;

public class University
{
    private String universityId;
    private String universityName;
    private ArrayList<Department> departmentList;


    public University(String universityId, String universityName)
    {
        this.universityId = universityId;
        this.universityName = universityName;
        departmentList = new ArrayList<>();
    }

    public void addDepartment(Department d)
    {
        departmentList.add(d);
    }
}
