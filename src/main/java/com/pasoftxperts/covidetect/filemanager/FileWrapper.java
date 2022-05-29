package com.pasoftxperts.covidetect.filemanager;

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
        // Make directories for room files
        String finalPath = path + "/" + universityName.toLowerCase() + "/" + departmentName.toLowerCase();

        //  Room name list (needed for list catalogs)
        ArrayList<String> roomNames = new ArrayList<>();

        new File(finalPath).mkdirs();

        // We now save every room in a different .ser file
        for (int i = 0; i < roomList.size(); i++)
        {
            Room room = roomList.get(i);

            roomNames.add(room.getRoomId());

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

        // Save room name list to a .ser file
        try
        {
            FileOutputStream fileOut = new FileOutputStream(finalPath + "/roomNames.ser");
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

            // Write object ArrayList
            objOut.writeObject(roomNames);

            objOut.close();
            fileOut.close();
        }
        catch (IOException e) { return; }

    }
}
