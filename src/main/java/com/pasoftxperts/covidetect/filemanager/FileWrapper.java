/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used to save room files and room/professor names to .ser files
 |
 |
*/

package com.pasoftxperts.covidetect.filemanager;

import com.pasoftxperts.covidetect.university.Room;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileWrapper
{
    public static final String PATH = System.getProperty("user.dir");

    private static ArrayList<String> roomNames;

    //
    // Gets input from the simulation and saves it into files
    //
    public static void saveFilesByRoom(String universityName, String departmentName, List<Room> roomList)
    {
        // Make directories for room files
        String finalPath = PATH + "/" + universityName.toLowerCase() + "/" + departmentName.toLowerCase();

        //  Room name list (needed for list catalogs)
        roomNames = new ArrayList<>();

        new File(finalPath).mkdirs();

        // We now save every room in a different .ser file
        for (int i = 0; i < roomList.size(); i++)
        {
            roomNames.add(roomList.get(i).getRoomId());

            try
            {
                FileOutputStream fileOut = new FileOutputStream(finalPath + "/" + roomList.get(i).getRoomId() + ".ser");
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOut);
                ObjectOutputStream objOut = new ObjectOutputStream(bufferedOutputStream);

                // Write object ArrayList
                objOut.writeObject(roomList.get(i));

                objOut.close();
                bufferedOutputStream.close();
                fileOut.close();
            }
            catch (IOException e) { return; }
        }

    }


    //
    // Saves Room Names so that they can be used in a combo box etc.
    //
    public static void saveRoomNames(String universityName, String departmentName)
    {
        String finalPath = PATH + "/" + universityName.toLowerCase() + "/" + departmentName.toLowerCase();

        if (roomNames == null)
            return;

        // Save room name list to a .ser file
        try
        {
            FileOutputStream fileOut = new FileOutputStream(finalPath + "/roomNames.ser");
            ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(fileOut));

            // Write object ArrayList
            objOut.writeObject(roomNames);

            objOut.close();
            fileOut.close();
        }
        catch (IOException e) { return; }
    }


    public static void saveProfessorNames(String universityName, String departmentName, ArrayList<String> professorNames)
    {
        ArrayList<String> names = SerializationUtils.clone(professorNames);

        for (int i = 0; i < names.size(); i++)
            names.set(i, "Professor " + names.get(i));

        String finalPath = PATH + "/" + universityName + "/" + departmentName + "/" + "professors";

        new File(finalPath).mkdirs();

        try
        {
            FileOutputStream fileOut = new FileOutputStream(finalPath + "/professorNames.ser");
            ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(fileOut));

            // Write object ArrayList
            objOut.writeObject(names);

            objOut.close();
            fileOut.close();
        }
        catch (IOException e) { return; }
    }
}
