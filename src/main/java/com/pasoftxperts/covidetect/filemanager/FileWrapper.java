package com.pasoftxperts.covidetect.filemanager;

import com.pasoftxperts.covidetect.university.Room;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.util.ArrayList;

public class FileWrapper
{
    public static final String path = System.getProperty("user.dir");
    private static ArrayList<String> roomNames;

    // Saves input files that it takes from server or simulation
    public static void saveFilesByRoom(String universityName, String departmentName, ArrayList<Room> roomList)
    {
        // Make directories for room files
        String finalPath = path + "/" + universityName.toLowerCase() + "/" + departmentName.toLowerCase();

        //  Room name list (needed for list catalogs)
        roomNames = new ArrayList<>();

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

    }

    // Saves Room Names so that they can be used in a combo box etc.
    public static void saveRoomNames(String universityName, String departmentName)
    {
        String finalPath = path + "/" + universityName.toLowerCase() + "/" + departmentName.toLowerCase();

        if (roomNames == null)
            return;

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

    public static void saveProfessorNames(String universityName, String departmentName, ArrayList<String> professorNames)
    {
        ArrayList<String> names = SerializationUtils.clone(professorNames);

        for (int i = 0; i < names.size(); i++)
            names.set(i, "Professor " + names.get(i));

        String finalPath = path + "/" + universityName + "/" + departmentName + "/" + "professors";

        new File(finalPath).mkdirs();

        try
        {
            FileOutputStream fileOut = new FileOutputStream(finalPath + "/professorNames.ser");
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

            // Write object ArrayList
            objOut.writeObject(names);

            objOut.close();
            fileOut.close();
        }
        catch (IOException e) { return; }
    }
}
