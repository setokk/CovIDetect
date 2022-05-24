package com.pasoftxperts.covidetect.university;

import com.pasoftxperts.covidetect.student.Student;

import java.io.Serializable;

public class Seat implements Serializable
{
    private int seatNumber;
    private Student student;
    private boolean occupied;

    public Seat(int seatNumber)
    {
        if (seatNumber <= 0)
            throw new IllegalArgumentException("seat number has to be a positive number");

        this.seatNumber = seatNumber;
    }

    public void occupySeat(Student student)
    {
        if (occupied)
            return;

        this.student = student;
        occupied = true;
    }

    public void freeSeat()
    {
        student = null;
        occupied = false;
    }

    public int getSeatNumber() { return seatNumber; }

    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }

    public Student getStudent() { return student; }

    public boolean isOccupied() { return occupied; }
}
