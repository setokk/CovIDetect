/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class counts the students (taken seats) of a room
 |
 |
*/

package com.pasoftxperts.covidetect.counters;

import com.pasoftxperts.covidetect.university.Seat;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.ArrayList;
import java.util.List;

public final class StudentCounter
{
    public static int countStudents(DefaultUndirectedGraph<Seat, Integer> seatGraph)
    {
        List<Seat> seatList = new ArrayList<>(seatGraph.vertexSet());

        return (int) seatList.stream()
                             .filter(e -> e.isOccupied())
                             .count();
    }
}
