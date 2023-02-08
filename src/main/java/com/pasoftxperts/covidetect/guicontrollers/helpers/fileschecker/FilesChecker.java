/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used to check for files.
 |
 |
 | Method Documentation:
 |     [*] public static boolean checkSimulationFiles()
 |         Returns true if the number of files are more than one in the path of the simulation (in this case /university of macedonia/applied informatics/)
 |         Returns false otherwise.
 |         In cases which the user might add one file by themselves, the system still handles the null pointer exceptions by using Optionals
 |         (see "public Optional<Object> getResult()" in ObjectReader and TaskObjectReader classes)
 |
 |
*/

package com.pasoftxperts.covidetect.guicontrollers.helpers.fileschecker;

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

