package com.pasoftxperts.covidetect.guicontrollers.fileschecker;

import com.pasoftxperts.covidetect.guicontrollers.MainApplicationController;

import java.io.File;

public class FilesChecker
{
    // Check if the simulation files exist. Returns true if they do. Otherwise, returns false
    // Checks if the number of files in the main simulation path (in this case /university of macedonia/applied informatics/)
    // is less or equal to 1 (exclude empty history directory)
    public static boolean checkSimulationFiles()
    {
        new File(MainApplicationController.PATH).mkdirs();
        File folder = new File(MainApplicationController.PATH);

        return (folder.listFiles().length > 1); // We exclude the history directory which is empty at first and counts as one file
    }
}

