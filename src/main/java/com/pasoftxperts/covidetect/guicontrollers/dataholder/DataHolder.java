/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This singleton class is used to hold and transfer data from one GUI controller to another (Room Visualization to Student ID List)
 |
 |
*/

package com.pasoftxperts.covidetect.guicontrollers.dataholder;

public class DataHolder
{
    private static DataHolder dataHolder = new DataHolder();
    private Object objectData; // The data we want to store and access from other classes

    private DataHolder() {}

    public static DataHolder getInstance(){
        return dataHolder;
    }

    public Object getObjectData() {
        return objectData;
    }

    public void setObjectData(Object objectData) {
        this.objectData = objectData;
    }
}
