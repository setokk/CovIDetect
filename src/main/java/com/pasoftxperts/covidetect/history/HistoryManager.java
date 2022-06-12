/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 |
 | We create an arraylist of objects and add GUI field values to them in a specific order (from top left to bottom - top right to bottom)
 | Thus, we can save the values to a file and load them back when a user selects the history option
 | Example,
 | In the statistics GUI, we will add them in this order:
 | - (Top Left to Bottom) Room value, Start Date Value, End Date Value, Show By Option Value, Data Category Value, Statistical Method Value.
 | - (Top Right to Bottom) Min Value, Max Value, Average Value, Statistical Method Result (Standard Deviation) value, yAxis values, Show by Elements (xAxis) values
*/

package com.pasoftxperts.covidetect.history;

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

    public static Object readHistory(String name)
    {
        try
        {
            FileInputStream fileInputStream = new FileInputStream(HISTORY_PATH + name);
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
        new File(HISTORY_PATH).mkdirs();

        // We go through the files and see if we exceeded the MAX FILES limit (10 files)
        File folder = new File(HISTORY_PATH);
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

            // Update History
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.US);

                FileOutputStream fileOutputStream = new FileOutputStream(HISTORY_PATH + "[Statistics] " + selectedRoom + " (" + formatter.format(startDate) + " - " + formatter.format(endDate) + ")");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                objectOutputStream.writeObject(values);

                objectOutputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
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
