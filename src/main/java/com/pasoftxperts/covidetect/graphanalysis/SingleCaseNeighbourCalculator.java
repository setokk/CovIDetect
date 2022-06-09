package com.pasoftxperts.covidetect.graphanalysis;

import com.pasoftxperts.covidetect.university.Seat;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.ArrayList;

import static com.pasoftxperts.covidetect.graphanalysis.GraphNeighboursGenerator.*;

/* Calculates possible cases (neighbours) of a single case */
public class SingleCaseNeighbourCalculator
{
    public static DefaultUndirectedGraph<Seat, Integer> calculateSingleCaseNeighbours(Seat covidCase,
                                                                                      ArrayList<Seat> seats,
                                                                                      int rows,
                                                                                      int cols)
    {
        DefaultUndirectedGraph<Seat, Integer> graph = new DefaultUndirectedGraph<>(Integer.class);

        ArrayList<Seat> covidCases = new ArrayList<>();

        // Populate graph
        for (int i = 0; i < seats.size(); i++)
        {
            graph.addVertex(seats.get(i));

            if (seats.get(i).isOccupied() && isCovidCase(seats.get(i).getStudent()))
                covidCases.add(seats.get(i));
        }


        // Get Covid Case indexes
        int i = (covidCase.getSeatNumber() - 1) / cols;
        int j = (covidCase.getSeatNumber() - 1) % cols;

        // Can go up
        if (i - 1 >= 0)
        {
            // Left
            if (j - 1 >= 0)
            {
                lookLeft("up", seats, graph, i, j, cols);
            }
            // Right
            if (j + 1 < cols)
            {
                lookRight("up", seats, graph, i, j, cols);
            }
        }

        // Can go down
        if (i + 1 < rows)
        {
            // Left
            if (j - 1 >= 0)
            {
                lookLeft("down", seats, graph, i, j, cols);
            }
            // Right
            if (j + 1 < cols)
            {
                lookRight("down", seats, graph, i, j, cols);
            }
        }

        // Turn falsely possible cases to covid cases again
        ArrayList<Seat> seatsInGraph = new ArrayList<>(graph.vertexSet());

        for (int k = 0; k < covidCases.size(); k++)
        {
            Seat currentCase = covidCases.get(k);

            seatsInGraph.get(currentCase.getSeatNumber() - 1).getStudent().setHealthIndicator(1);
        }

        return graph;
    }

    // Looks either up or down and in each case, to the right too (based on a mathematical formula).
    public static void lookRight(String direction,
                                 ArrayList<Seat> seats,
                                 DefaultUndirectedGraph<Seat, Integer> graph,
                                 int i,
                                 int j,
                                 int cols)
    {
        if (direction.equalsIgnoreCase("up"))
        {
            // Update the health indicators
            setPossibleCase(seats.get(numberOfSeat(i, j+1, cols) - 1));

            setPossibleCase(seats.get(numberOfSeat(i-1, j, cols) - 1));

            setPossibleCase(seats.get(numberOfSeat(i-1, j+1, cols) - 1));

            // Now, add all three edges to covidSeat
            Seat covidSeat = seats.get(numberOfSeat(i, j, cols) - 1);

            addAllEdges(covidSeat,
                    seats.get(numberOfSeat(i, j+1, cols) - 1),
                    seats.get(numberOfSeat(i-1, j, cols) - 1),
                    seats.get(numberOfSeat(i-1, j+1, cols) - 1),
                    graph);

        }
        else if (direction.equalsIgnoreCase("down"))
        {
            // Update the health indicators
            setPossibleCase(seats.get(numberOfSeat(i, j+1, cols) - 1));

            setPossibleCase(seats.get(numberOfSeat(i+1, j, cols) - 1));

            setPossibleCase(seats.get(numberOfSeat(i+1, j+1, cols) - 1));

            // Now, add the edges
            Seat covidSeat = seats.get(numberOfSeat(i, j, cols) - 1);

            addAllEdges(covidSeat,
                    seats.get(numberOfSeat(i, j+1, cols) - 1),
                    seats.get(numberOfSeat(i+1, j, cols) - 1),
                    seats.get(numberOfSeat(i+1, j+1, cols) - 1),
                    graph);
        }
        else
        {
            throw new IllegalArgumentException("Direction must be either 'up' or 'down'");
        }
    }

    // Looks either up or down and in each case, to the left too (based on a mathematical formula).
    public static void lookLeft(String direction,
                                ArrayList<Seat> seats,
                                DefaultUndirectedGraph<Seat, Integer> graph,
                                int i,
                                int j,
                                int cols)
    {
        if (direction.equalsIgnoreCase("up"))
        {
            // Update the health indicators
            setPossibleCase(seats.get(numberOfSeat(i, j-1, cols) - 1));

            setPossibleCase(seats.get(numberOfSeat(i-1, j, cols) - 1));

            setPossibleCase(seats.get(numberOfSeat(i-1, j-1, cols) - 1));

            // Now, add the edges
            Seat covidSeat = seats.get(numberOfSeat(i, j, cols) - 1);

            addAllEdges(covidSeat,
                    seats.get(numberOfSeat(i, j-1, cols) - 1),
                    seats.get(numberOfSeat(i-1, j, cols) - 1),
                    seats.get(numberOfSeat(i-1, j-1, cols) - 1),
                    graph);
        }
        else if (direction.equalsIgnoreCase("down"))
        {
            // Update the health indicators
            setPossibleCase(seats.get(numberOfSeat(i, j-1, cols) - 1));

            setPossibleCase(seats.get(numberOfSeat(i+1, j, cols) - 1));

            setPossibleCase(seats.get(numberOfSeat(i+1, j-1, cols) - 1));

            // Now, add the edges
            Seat covidSeat = seats.get(numberOfSeat(i, j, cols) - 1);

            addAllEdges(covidSeat,
                    seats.get(numberOfSeat(i, j-1, cols) - 1),
                    seats.get(numberOfSeat(i+1, j, cols) - 1),
                    seats.get(numberOfSeat(i+1, j-1, cols) - 1),
                    graph);
        }
        else
        {
            throw new IllegalArgumentException("Direction must be either 'up' or 'down'");
        }
    }
}
