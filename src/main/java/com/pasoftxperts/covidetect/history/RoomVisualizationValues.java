package com.pasoftxperts.covidetect.history;

import com.pasoftxperts.covidetect.university.Seat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class RoomVisualizationValues implements Serializable
{
    // ComboBox and date values
    private String selectedRoom;

    private String selectedDate;

    private String selectedHourSpan;

    // General Room Info
    private String totalStudents;

    private String covidCases;

    private String possibleCases;

    private String freeSeats;

    private String roomLabel;

    private String courseLabel;

    private LocalDate dateLabel;

    // List of the graph seats
    private List<Seat> seats;


    //
    // Getters and Setters
    //
    public String getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(String selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedHourSpan() {
        return selectedHourSpan;
    }

    public void setSelectedHourSpan(String selectedHourSpan) {
        this.selectedHourSpan = selectedHourSpan;
    }

    public String getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(String totalStudents) {
        this.totalStudents = totalStudents;
    }

    public String getCovidCases() {
        return covidCases;
    }

    public void setCovidCases(String covidCases) {
        this.covidCases = covidCases;
    }

    public String getPossibleCases() {
        return possibleCases;
    }

    public void setPossibleCases(String possibleCases) {
        this.possibleCases = possibleCases;
    }

    public String getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(String freeSeats) {
        this.freeSeats = freeSeats;
    }

    public String getRoomLabel() {
        return roomLabel;
    }

    public void setRoomLabel(String roomLabel) {
        this.roomLabel = roomLabel;
    }

    public String getCourseLabel() {
        return courseLabel;
    }

    public void setCourseLabel(String courseLabel) {
        this.courseLabel = courseLabel;
    }

    public LocalDate getDateLabel() {
        return dateLabel;
    }

    public void setDateLabel(LocalDate dateLabel) {
        this.dateLabel = dateLabel;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
