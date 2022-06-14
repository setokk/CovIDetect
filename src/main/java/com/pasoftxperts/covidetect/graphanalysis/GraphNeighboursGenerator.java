/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used to calculate the possible cases for each covid case, given a 2D ArrayList.
 |
 |
 | Design:
 | Given a 2D ArrayList of Seats:
 |
 | [C]  []   []   []   [C]  [C]  []   []
 | [H]  []   []   []   []   [H]  []   []
 | []   []   []   []   []   []   []   []
 | []   []   [H]  []   []   [H]  [H]  []
 | []   []   []   []   []   []   [C]  [H]
 | []   []   []   []   []   []   [H]  []
 |
 | where C = Covid Case, H = Healthy and P = Possible Case,
 | we want to calculate all the possible cases, as in:
 |
 | [C]  []   []   []   [C]  [C]  []   []
 | [P]  []   []   []   []   [P]  []   []
 | []   []   []   []   []   []   []   []
 | []   []   [H]  []   []   [P]  [P]  []
 | []   []   []   []   []   []   [C]  [P]
 | []   []   []   []   []   []   [P]  []
 |
 | and returns an undirected graph with all the seats and their edges.
 |
 | In this version of the system, we only create edges with the covid cases and those surrounding them (possible cases).
 | We do that by first saving the covid cases to another arraylist. We then loop through the covid cases list and each time set the current case's health indicator to 1
 | so that if a previous covid case was next to the current one (made it a possible case), we set the current one as a covid case again.
 |
 | After that, through a mathematical formula (lookLeft, lookRight methods), we appropriately set the possible cases.
 | (visual explanation of the formula):
 |
 |   [H]  [H]  []      If we can go up and left from where C is, it means that we can set a possible case diagonally too (we get rid of 1 check each time)
 |   []   [C]  [P]
 |   [P]  []
 |
 |  [P]  [P]  []       So we can now have this
 |  []   [C]  [P]
 |  [P]  []
 |
 |
 | In a later version of the system, authorized users will be able to drag and drop seats in a room and a template graph for that room
 | will automatically generate using this class's methods.
 |
 | (the gaps between seats will be represented with a student health indicator value of -1 or just an unoccupied seat)
 |
 | The graph will also be undirected and all its vertices, covid case or not, will have edges with their surrounding seats
 | (easier calculation of possible cases as all we have to do is look at its edges and if the seat it has an edge with is a covid case)
 |
 |
 | Method Documentation:
 |     [*] public static DefaultUndirectedGraph<Seat, Integer> calculateNeighboursGraph(ArrayList<ArrayList<Seat>> seats,
 |                                                                               int rows,
 |                                                                               int cols)
 |         Takes a 2D ArrayList of Seats and the rows/cols of the list. Returns an undirected graph with all the seats adn the appropriate edges.
 |         (see explanation above)
 |
 |     [*] public static boolean isCovidCase(Student student)
 |         Returns whether a student is a covid case
 |
 |     [*] public static int numberOfSeat(int i, int j, int cols)
 |         Takes the row and col index and the number of columns. Returns the number of seat.
 |
 |     [*] public static void saveCovidCases(ArrayList<Seat> covidCases,
 |                                    ArrayList<ArrayList<Seat>> seats,
 |                                    int rows,
 |                                    int cols)
 |         Takes an empty covidCases list, seat list and rows/cols of the seat list.
 |         Saves the covid cases of seat list to covidCase list
 |
 |     [*] lookLeft and lookRight methods are explained above. They take a String direction to determine if they can look up or down.
 |
 |     [*] public static void addAllEdges(Seat sourceSeat,
 |                                 Seat seatOne,
 |                                 Seat seatTwo,
 |                                 Seat seatThree,
 |                                 DefaultUndirectedGraph<Seat, Integer> graph)
 |         Adds edges from sourceSeat to the three other Seats
 |
 |
*/

package com.pasoftxperts.covidetect.graphanalysis;

import com.pasoftxperts.covidetect.student.Student;
import com.pasoftxperts.covidetect.university.Seat;

import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.ArrayList;

public class GraphNeighboursGenerator
{
    //
    // Calculates the neighbours and returns a graph.
    //
    public static DefaultUndirectedGraph<Seat, Integer> calculateNeighboursGraph(ArrayList<ArrayList<Seat>> seats,
                                                                                 int rows,
                                                                                 int cols)
    {
        // First, we create an Undirected Graph to populate
        DefaultUndirectedGraph<Seat, Integer> graph = new DefaultUndirectedGraph<>(Integer.class);
        populateGraph(graph, seats, rows, cols);

        // Create Covid Cases Seats List
        ArrayList<Seat> covidCasesSeats = new ArrayList<>();

        // We need them so that we don't lose information on who is a covid case later
        saveCovidCases(covidCasesSeats, seats, rows, cols);


        Seat covidCase;

        // Loop through the covid cases
        for (int k = 0; k < covidCasesSeats.size(); k++)
        {
            covidCase = covidCasesSeats.get(k);

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
        Seat seat;

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                // We add the number of seat (1, 2, 3 ,4, 5 ,6 ,7, ..., 20 in this case)
                seat = seats.get(i).get(j);

                if (seat.isOccupied() && isCovidCase(seat.getStudent()))
                {
                    covidCases.add(seat);
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
        ArrayList<Seat> currSeats;

        for (int i = 0; i < rows; i++)
        {
            currSeats = seats.get(i);

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
