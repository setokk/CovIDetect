package com.pasoftxperts.covidetect.history;

import java.io.*;
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
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

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

        // Check cases
        if (values instanceof RoomVisualizationValues)
        {
            RoomVisualizationValues roomVisualizationValues = (RoomVisualizationValues) values;

            try
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.US);

                FileOutputStream fileOutputStream = new FileOutputStream(HISTORY_PATH + "[Room Visualization] " + roomVisualizationValues.getSelectedRoom() + " (" +formatter.format(roomVisualizationValues.getDateLabel()) + ")");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                objectOutputStream.writeObject(roomVisualizationValues);

                objectOutputStream.close();
                fileOutputStream.close();
            }
            catch (IOException e)
            {

            }
        }
        else if (values instanceof StatisticsValues)
        {
            StatisticsValues statisticsValues = (StatisticsValues) values;

            try
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.US);

                FileOutputStream fileOutputStream = new FileOutputStream(HISTORY_PATH + "[Statistics] " + statisticsValues.getSelectedRoom() + " (" + formatter.format(statisticsValues.getStartDate()) + " - " + formatter.format(statisticsValues.getEndDate()) + ")");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                objectOutputStream.writeObject(statisticsValues);

                objectOutputStream.close();
                fileOutputStream.close();
            }
            catch (IOException e)
            {
                return;
            }
        }
        else
        {
            return;
        }
    }

    public static boolean isSelectedHistoryStatus() {
        return selectedHistoryStatus;
    }

    public static void setSelectedHistoryStatus(boolean selectedHistoryStatus) {
        HistoryManager.selectedHistoryStatus = selectedHistoryStatus;
    }
}
