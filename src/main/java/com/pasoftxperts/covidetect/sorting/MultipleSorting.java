package com.pasoftxperts.covidetect.sorting;

public class MultipleSorting implements Comparable<MultipleSorting>
{
    public int xaxis;
    public double yaxis;
    public int counter;

    public MultipleSorting(int xaxis, double yaxis, int counter)
    {
        this.xaxis = xaxis;
        this.yaxis = yaxis;
        this.counter = counter;
    }

    public MultipleSorting(int xaxis, double yaxis)
    {
        this.xaxis = xaxis;
        this.yaxis = yaxis;
    }

    @Override
    public int compareTo(MultipleSorting o)
    {
        return this.xaxis - o.xaxis;
    }
}
