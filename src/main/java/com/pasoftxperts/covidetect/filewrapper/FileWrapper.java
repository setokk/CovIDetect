package com.pasoftxperts.covidetect.filewrapper;

import com.pasoftxperts.covidetect.university.Room;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileWrapper
{
    public static final String path = System.getProperty("user.dir");

    // Saves input files that it takes from server or simulation
    public static void saveFilesByRoom(String universityName, String departmentName, ArrayList<Room> roomList)
    {
        // Make directories
        String finalPath = path + "/" + universityName.toLowerCase() + "/" + departmentName.toLowerCase();

        new File(finalPath).mkdirs();

        // We now save every room in a different .ser file
        for (int i = 0; i < roomList.size(); i++)
        {
            Room room = roomList.get(i);

            try
            {
                FileOutputStream fileOut = new FileOutputStream(finalPath + "/" + room.getRoomId() + ".ser");
                ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

                // Write object ArrayList
                objOut.writeObject(room);

                objOut.close();
                fileOut.close();
            }
            catch (IOException e) { return; }
        }
    }
}
