/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class represents a department.
 | Each department has a department id, a department name, a list of rooms and a number of total courses.
 |
 |
*/

package com.pasoftxperts.covidetect.client.university;

import java.util.ArrayList;

public class Department
{
    private String departmentId;
    private String departmentName;
    private ArrayList<Room> roomList;
    private int totalCourses;

    public Department(String departmentId, String departmentName, int totalCourses)
    {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.totalCourses = totalCourses;
        roomList = new ArrayList<>();
    }

    public void addRoom(Room r)
    {
        roomList.add(r);
    }

    public String getDepartmentId() { return departmentId; }

    public String getDepartmentName() { return departmentName; }

    public ArrayList<Room> getRoomList() { return roomList; }

    public int getTotalCourses() { return totalCourses; }
}