/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class represents a room. Each room has a room id, a capacity, the total seat rows of the room
 | and a list of TimeStamps (dates at which lectures have occurred).
 |
 |
*/

package com.pasoftxperts.covidetect.client.university;

import com.pasoftxperts.covidetect.client.time.TimeStamp;

import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable
{
    private String roomId;
    private final int capacity;
    private final int seatRows;
    private ArrayList<TimeStamp> timeStampList;

    public Room(String roomId, int capacity, int seatRows)
    {
        if ((capacity <= 0) || (seatRows <=0 ))
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

    public int getSeatColumns() { return capacity / seatRows; }

    public ArrayList<TimeStamp> getTimeStampList() { return timeStampList; }

    public void addTimeStamp(TimeStamp ts)
    {
        timeStampList.add(ts);
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Room))
            return false;

        Room otherRoom = (Room) o;

        return (this.roomId.equals(otherRoom.getRoomId()));
    }
}
