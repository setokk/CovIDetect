/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used to update and load the history of statistical reports (Max Files in history = 10)
 |
 | Method Documentation:
 |      [*] public static readHistory(String fileName)
 |          Reads a file with name "fileName" that holds stores previous values of the statistical analysis results
 |
 |      [*] public static updateHistory(Object values)
 |          Gets the statistical analysis results in a StatisticalValues object (values) and saves them into a file
 |
*/

package com.pasoftxperts.covidetect.history;

import com.pasoftxperts.covidetect.loginsession.LoginSession;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HistoryManager
{
    // Indicates to the initialize functions of the GUI Controllers if they need to load history files or not
    private static boolean selectedHistoryStatus = false;

    public static final int MAX_FILES = 10; // Max history files saved at a time

    public static final String HISTORY_PATH = System.getProperty("user.dir") + "/university of macedonia/applied informatics/history/";

    public static Object readHistory(String fileName)
    {
        try
        {
            FileInputStream fileInputStream = new FileInputStream(HISTORY_PATH + LoginSession.getUsername() + "/" + fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(fileInputStream));

            Object values = objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();

            return values;
        }
        catch (IOException|ClassNotFoundException e)
        {
            return null;
        }
    }

    public static void updateHistory(Object values)
    {
        // Make path directories
        new File(HISTORY_PATH + LoginSession.getUsername()).mkdirs();

        // We go through the files and see if we exceeded the MAX FILES limit (10 files)
        File folder = new File(HISTORY_PATH + LoginSession.getUsername());
        List<File> listOfFiles = Arrays.asList(folder.listFiles());

        // Sort based on the last date modified
        Collections.sort(listOfFiles, (a, b) -> a.lastModified() > b.lastModified() ? -1 : a.lastModified() == b.lastModified() ? 0 : 1);

        // Delete oldest file
        if (listOfFiles.size() == 10)
        {
            listOfFiles.get(MAX_FILES - 1).delete();
        }

        if (values instanceof StatisticsValues)
        {
            StatisticsValues statisticsValues = (StatisticsValues) values;

            // CAREFUL, get in appropriate oder (see StatisticalValues class)
            String selectedRoom = (String) statisticsValues.getFieldValues().get(0);
            LocalDate startDate = (LocalDate) statisticsValues.getFieldValues().get(1);
            LocalDate endDate = (LocalDate) statisticsValues.getFieldValues().get(2);
            String tempData = (String) statisticsValues.getFieldValues().get(4);

            // Create a string with only the first word (either attendance or covid)
            int index = 0;

            for (int i = 0; i < tempData.length(); i++)
            {
                if (tempData.charAt(i) == ' ')
                {
                    index = i;
                    break;
                }

            }

            String dataCategory = tempData.substring(0, index);


            // Update History
            try
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy", Locale.US);

                FileOutputStream fileOutputStream = new FileOutputStream(HISTORY_PATH + LoginSession.getUsername() + "/[REPORT]_" + dataCategory.toUpperCase() + "_" + selectedRoom.toUpperCase() + "_(" + formatter.format(startDate) + "_" + formatter.format(endDate) + ")");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                objectOutputStream.writeObject(values);

                objectOutputStream.close();
                fileOutputStream.close();
            }
            catch (IOException e)
            {
                return;
            }
        }
    }

    public static boolean isSelectedHistoryStatus() {
        return selectedHistoryStatus;
    }

    public static void setSelectedHistoryStatus(boolean selectedHistoryStatus) {
        HistoryManager.selectedHistoryStatus = selectedHistoryStatus;
    }
}
