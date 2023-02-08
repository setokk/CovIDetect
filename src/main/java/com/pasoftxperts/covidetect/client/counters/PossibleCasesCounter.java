/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class counts the possible covid cases of a room at a specific date (TimeStamp)
 |
 |
*/

package com.pasoftxperts.covidetect.client.counters;

import com.pasoftxperts.covidetect.client.university.Seat;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.ArrayList;
import java.util.List;

public class PossibleCasesCounter
{
    public static int countPossibleCases(DefaultUndirectedGraph<Seat, Integer> seatGraph)
    {
        List<Seat> seatList = new ArrayList<>(seatGraph.vertexSet());

        int result = (int) seatList.stream()
                                   .filter(e -> e.isOccupied())
                                   .filter(e -> e.getStudent().getHealthIndicator() == 2)
                                   .count();

        seatList.clear();

        return result;
    }
}
