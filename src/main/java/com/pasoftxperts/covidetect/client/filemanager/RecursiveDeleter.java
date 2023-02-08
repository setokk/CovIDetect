/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used to delete a directory recursively
 |
 |
*/

package com.pasoftxperts.covidetect.client.filemanager;

import java.io.File;

public class RecursiveDeleter
{
    public static void deleteDirectoryRecursively(String path)
    {
        // Delete previous history files, if any
        new File(path).mkdirs();

        // We go through the user folders
        File folder = new File(path);
        File[] listOfFolders = folder.listFiles();

        if (listOfFolders == null)
            return;

        // Delete them
        for (int i = 0; i < listOfFolders.length; i++)
        {
            File[] listOfFiles = listOfFolders[i].listFiles();

            if (listOfFiles != null)
            {
                // We go through the files
                for (int j = 0; j < listOfFiles.length; j++)
                    listOfFiles[j].delete();

            }

            listOfFolders[i].delete();
        }
    }
}
