/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 |
 | We create an arraylist of objects and add GUI field values to them in a specific order (from top left to bottom and after -> top right to bottom)
 | Thus, we can save the values to a file and load them back when a user selects the history option
 | Example,
 | In the statistics GUI, we will add them in this order:
 | - (Top Left to Bottom) Room value, Start Date Value, End Date Value, Show By Option Value, Data Category Value, Statistical Method Value.
 | - (Top Right to Bottom) Min Value, Max Value, Average Value, Statistical Method Result (Standard Deviation) value, yAxis values, Show by Elements (xAxis) values
*/

package com.pasoftxperts.covidetect.client.history;

import java.io.Serializable;
import java.util.ArrayList;

public class StatisticsValues implements Serializable
{
    private ArrayList<Object> fieldValues = new ArrayList<>();

    public void addValue(Object value) {
        fieldValues.add(value);
    }

    public ArrayList<Object> getFieldValues() {
        return fieldValues;
    }
}
