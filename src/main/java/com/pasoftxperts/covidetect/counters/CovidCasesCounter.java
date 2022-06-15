/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class counts the covid cases of a room
 |
 |
*/

package com.pasoftxperts.covidetect.counters;

import com.pasoftxperts.covidetect.university.Seat;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.ArrayList;
import java.util.List;

public class CovidCasesCounter
{
    public static int countCovidCases(DefaultUndirectedGraph<Seat, Integer> seatGraph)
    {
        List<Seat> seatList = new ArrayList<>(seatGraph.vertexSet());

        int result = (int) seatList.stream()
                                   .filter(e -> e.isOccupied())
                                   .filter(e -> e.getStudent().getHealthIndicator() == 1)
                                   .count();

        seatList.clear();

        return result;
    }
}
