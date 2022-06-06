package com.pasoftxperts.covidetect.counters;

import com.pasoftxperts.covidetect.university.Seat;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.ArrayList;
import java.util.List;

public final class PossibleCasesCounter
{
    public static int countPossibleCases(DefaultUndirectedGraph<Seat, Integer> seatGraph)
    {
        List<Seat> seatList = new ArrayList<>(seatGraph.vertexSet());

        return (int) seatList.stream()
                             .filter(e -> e.isOccupied())
                             .filter(e -> e.getStudent().getHealthIndicator() == 2)
                             .count();
    }
}
