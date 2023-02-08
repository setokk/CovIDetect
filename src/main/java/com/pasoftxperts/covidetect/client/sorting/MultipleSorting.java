/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used to parallel sort 3 elements based on xaxis
 |
 |
*/

package com.pasoftxperts.covidetect.client.sorting;

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
