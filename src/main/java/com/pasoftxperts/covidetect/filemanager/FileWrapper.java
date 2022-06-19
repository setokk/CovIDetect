/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used to save room files and professor names from the simulation class to .ser files
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

    //
    // Takes a university name, department name and a room list. Saves each room to a .ser file.
    //
    public static void saveFilesByRoom(String universityName, String departmentName, List<Room> roomList)
    {
        // Make directories for room files
        String finalPath = PATH + "/" + universityName.toLowerCase() + "/" + departmentName.toLowerCase();

        new File(finalPath).mkdirs();

        // We now save every room in a different .ser file
        for (int i = 0; i < roomList.size(); i++)
        {
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
    // Takes a university name, department name and a professor names list. Saves professor names list to a .ser file.
    //
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
