package com.pasoftxperts.covidetect.guicontrollers.roomnames;

import com.pasoftxperts.covidetect.guicontrollers.MainApplicationController;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public final class RoomNames
{
    public static ArrayList<String> getAllRoomNamesSorted()
    {
        ArrayList<String> roomNames = new ArrayList<>();

        // Get list of files in main folder (simulation files)
        new File(MainApplicationController.PATH).mkdirs();
        File mainFolder = new File(MainApplicationController.PATH);

        File[] listOfFiles = mainFolder.listFiles();

        for (File file : listOfFiles)
        {
            if (file.getName().contains("Room") && file.getName().contains(".ser"))
                roomNames.add(file.getName().substring(0, file.getName().lastIndexOf('.'))); // Remove the .ser extension
        }

        Collections.sort(roomNames);

        return roomNames;
    }
}
