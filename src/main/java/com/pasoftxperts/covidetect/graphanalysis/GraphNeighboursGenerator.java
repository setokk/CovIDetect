package com.pasoftxperts.covidetect.graphanalysis;

import com.pasoftxperts.covidetect.student.Student;
import com.pasoftxperts.covidetect.university.Seat;

import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.ArrayList;

public class GraphNeighboursGenerator
{
    // Calculates the neighbours and returns a graph.
    public static DefaultUndirectedGraph<Seat, Integer> calculateNeighboursGraph(ArrayList<ArrayList<Seat>> seats,
                                                                                 int rows,
                                                                                 int cols)
    {
        // First, we create an Undirected Graph to populate
        DefaultUndirectedGraph<Seat, Integer> graph = new DefaultUndirectedGraph<Seat, Integer>(Integer.class);
        populateGraph(graph, seats, rows, cols);

        // Create Covid Cases Seats List
        ArrayList<Seat> covidCasesSeats = new ArrayList<>();

        // We need them so that we don't lose information on who is a covid case later
        saveCovidCases(covidCasesSeats, seats, rows, cols);

        for (int k = 0; k < covidCasesSeats.size(); k++)
        {
            Seat covidCase = covidCasesSeats.get(k);

            int i = (covidCase.getSeatNumber() - 1) / cols;
            int j = (covidCase.getSeatNumber() - 1) % cols;

            seats.get(i).get(j)
                    .getStudent()
                    .setHealthIndicator(1);

            // Can go up
            if (i - 1 >= 0)
            {
                // Left
                if (j - 1 >= 0)
                {
                    lookLeft("up", seats, graph, i, j);
                }
                // Right
                if (j + 1 < cols)
                {
                    lookRight("up", seats, graph, i, j);
                }
            }

            // Can go down
            if (i + 1 < rows)
            {
                // Left
                if (j - 1 >= 0)
                {
                    lookLeft("down", seats, graph, i, j);
                }
                // Right
                if (j + 1 < cols)
                {
                    lookRight("down", seats, graph, i, j);
                }
            }
        }

        ArrayList<Seat> seatsInGraph = new ArrayList<>(graph.vertexSet());

        // We turn the falsely probable positive cases to 1 again
        for (int i = 0; i < covidCasesSeats.size(); i++)
        {
            int seatNumber = covidCasesSeats.get(i).getSeatNumber();

            for (int j = 0; j < seatsInGraph.size(); j++)
            {
                Seat seat = seatsInGraph.get(j);

                if (seat.getSeatNumber() == seatNumber)
                    seat.getStudent().setHealthIndicator(1);
            }

            seats.get((seatNumber - 1) / cols).get((seatNumber - 1) % cols)
                                              .getStudent()
                                              .setHealthIndicator(1);
        }

        return graph;
    }

    // Checks if the seat has a student with a covid case
    public static boolean isCovidCase(Student student) { return student.getHealthIndicator() == 1; }

    // Calculates the number of a seat given two indexes and the number of columns of the array
    public static int numberOfSeat(int i, int j, int cols) { return i * cols + j + 1; }

    // Saves covid cases into a list for later
    public static void saveCovidCases(ArrayList<Seat> covidCases,
                                      ArrayList<ArrayList<Seat>> seats,
                                      int rows,
                                      int cols)
    {
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                // We add the number of seat (1, 2, 3 ,4, 5 ,6 ,7, ..., 20 in this case)
                Seat seat = seats.get(i).get(j);

                if (seat.isOccupied() && isCovidCase(seat.getStudent()))
                {
                    covidCases.add(seats.get(i).get(j));
                }
            }
        }
    }

    // Looks either up or down and in each case, to the right too (based on a mathematical formula).
    public static void lookRight(String direction,
                                 ArrayList<ArrayList<Seat>> seats,
                                 DefaultUndirectedGraph<Seat, Integer> graph,
                                 int i,
                                 int j)
    {
        if (direction.equalsIgnoreCase("up"))
        {
            // Update the health indicators
            setPossibleCase(seats.get(i).get(j+1));

            setPossibleCase(seats.get(i-1).get(j));

            setPossibleCase(seats.get(i-1).get(j+1));

            // Now, add all three edges to covidSeat
            Seat covidSeat = seats.get(i).get(j);

            addAllEdges(covidSeat,
                    seats.get(i).get(j+1),
                    seats.get(i-1).get(j),
                    seats.get(i-1).get(j+1),
                    graph);
        }
        else if (direction.equalsIgnoreCase("down"))
        {
            // Update the health indicators
            setPossibleCase(seats.get(i).get(j+1));

            setPossibleCase(seats.get(i+1).get(j));

            setPossibleCase(seats.get(i+1).get(j+1));

            // Now, add the edges
            Seat covidSeat = seats.get(i).get(j);

            addAllEdges(covidSeat,
                    seats.get(i).get(j+1),
                    seats.get(i+1).get(j),
                    seats.get(i+1).get(j+1),
                    graph);
        }
        else
        {
            throw new IllegalArgumentException("Direction must be either 'up' or 'down'");
        }
    }

    // Looks either up or down and in each case, to the left too (based on a mathematical formula).
    public static void lookLeft(String direction,
                                ArrayList<ArrayList<Seat>> seats,
                                DefaultUndirectedGraph<Seat, Integer> graph,
                                int i,
                                int j)
    {
        if (direction.equalsIgnoreCase("up"))
        {
            // Update the health indicators
            setPossibleCase(seats.get(i).get(j-1));

            setPossibleCase(seats.get(i-1).get(j));

            setPossibleCase(seats.get(i-1).get(j-1));

            // Now, add the edges
            Seat covidSeat = seats.get(i).get(j);

            addAllEdges(covidSeat,
                    seats.get(i).get(j-1),
                    seats.get(i-1).get(j),
                    seats.get(i-1).get(j-1),
                    graph);
        }
        else if (direction.equalsIgnoreCase("down"))
        {
            // Update the health indicators
            setPossibleCase(seats.get(i).get(j-1));

            setPossibleCase(seats.get(i+1).get(j));

            setPossibleCase(seats.get(i+1).get(j-1));

            // Now, add the edges
            Seat covidSeat = seats.get(i).get(j);

            addAllEdges(covidSeat,
                    seats.get(i).get(j-1),
                    seats.get(i+1).get(j),
                    seats.get(i+1).get(j-1),
                    graph);
        }
        else
        {
            throw new IllegalArgumentException("Direction must be either 'up' or 'down'");
        }
    }

    public static void populateGraph(DefaultUndirectedGraph<Seat, Integer> graph,
                                     ArrayList<ArrayList<Seat>> seats,
                                     int rows,
                                     int cols)
    {
        for (int i = 0; i < rows; i++)
        {
            ArrayList<Seat> currSeats = seats.get(i);

            for (int j = 0; j < cols; j++)
                graph.addVertex(currSeats.get(j));
        }
    }

    // Adds Edges from sourceSeat to all three other seats
    public static void addAllEdges(Seat sourceSeat,
                                   Seat seatOne,
                                   Seat seatTwo,
                                   Seat seatThree,
                                   DefaultUndirectedGraph<Seat, Integer> graph)
    {
        graph.addEdge(sourceSeat, seatOne, 1);
        graph.addEdge(sourceSeat, seatTwo, 1);
        graph.addEdge(sourceSeat, seatThree, 1);
    }

    /* IMPORTANT - isOccupied method returns if the student is not NULL (Handles NullPointerException).*/

    // Gets a possible case and checks if there even is a student sitting.
    // Updates the student's health indicator to 2 (possible case)
    public static void setPossibleCase(Seat seat)
    {
        if (seat.isOccupied())
            seat.getStudent().setHealthIndicator(2);
    }
}
