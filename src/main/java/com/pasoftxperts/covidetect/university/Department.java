package com.pasoftxperts.covidetect.university;

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