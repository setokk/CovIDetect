package com.pasoftxperts.covidetect.university;

import com.pasoftxperts.covidetect.student.Student;

public class Seat
{
    private int seatNumber;
    private Student student;
    private Boolean occupied;

    public Seat(int seatNumber)
    {
        this.seatNumber = seatNumber;
    }

    public void occupySeat(Student s)
    {
        if (occupied)
            return;

        student = s;
        occupied = true;
    }

    public void freeSeat()
    {
        student = null;
        occupied = false;
    }
}
