package com.pasoftxperts.covidetect.university;

import com.pasoftxperts.covidetect.time.TimeStamp;

import edu.uci.ics.jung.graph.UndirectedGraph;

import java.util.ArrayList;

public class Room
{
    private final int capacity;
    private final int seatRows;
    private String roomId;
    private ArrayList<TimeStamp> timeStampList;

    public Room(int capacity, int seatRows, String roomId)
    {
        if ( (capacity <= 0) || (seatRows <=0) )
            throw new IllegalArgumentException("capacity and seat rows must be > 0");

        if (seatRows < 2)
            throw new IllegalArgumentException("seat rows must not be less than two (1 row or 0 rows are prohibited)");

        this.capacity = capacity;
        this.seatRows = seatRows;
        this.roomId = roomId;
        timeStampList = new ArrayList<>();
    }

    public int getCapacity() { return capacity; }

    public String getRoomId() { return roomId; }

    public int getSeatRows() { return seatRows; }

    public ArrayList<TimeStamp> getTimeStampList() { return timeStampList; }

    public void addTimeStamp(TimeStamp ts)
    {
        timeStampList.add(ts);
    }
}
