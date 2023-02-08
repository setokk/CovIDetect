/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class represents a university.
 | Each university has a university id, a university name and a list of departments.
 |
 |
*/

package com.pasoftxperts.covidetect.client.university;

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

    public String getUniversityId() { return universityId; }

    public String getUniversityName() { return universityName; }

    public ArrayList<Department> getDepartmentList() { return departmentList; }
}
