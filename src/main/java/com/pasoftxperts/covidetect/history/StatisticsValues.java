package com.pasoftxperts.covidetect.history;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class StatisticsValues implements Serializable
{
    private String minField;

    private String maxField;

    private String averageField;

    private String statMethodLabel;

    private String selectedRoom;

    private LocalDate startDate;

    private LocalDate endDate;

    private String showByOption;

    private String selectedDataCategory;

    private String selectedStatisticalMethod;

    private ArrayList<Double> yAxis;

    private ArrayList<String> showByElements;


    // Getters and Setters
    public String getMinField() {
        return minField;
    }

    public void setMinField(String minField) {
        this.minField = minField;
    }

    public String getMaxField() {
        return maxField;
    }

    public void setMaxField(String maxField) {
        this.maxField = maxField;
    }

    public String getAverageField() {
        return averageField;
    }

    public void setAverageField(String averageField) {
        this.averageField = averageField;
    }

    public String getStatMethodLabel() {
        return statMethodLabel;
    }

    public void setStatMethodLabel(String statMethodLabel) {
        this.statMethodLabel = statMethodLabel;
    }

    public String getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(String selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getShowByOption() {
        return showByOption;
    }

    public void setShowByOption(String showByOption) {
        this.showByOption = showByOption;
    }

    public String getSelectedDataCategory() {
        return selectedDataCategory;
    }

    public void setSelectedDataCategory(String selectedDataCategory) {
        this.selectedDataCategory = selectedDataCategory;
    }

    public String getSelectedStatisticalMethod() {
        return selectedStatisticalMethod;
    }

    public void setSelectedStatisticalMethod(String selectedStatisticalMethod) {
        this.selectedStatisticalMethod = selectedStatisticalMethod;
    }

    public ArrayList<Double> getyAxis() {
        return yAxis;
    }

    public void setyAxis(ArrayList<Double> yAxis) {
        this.yAxis = yAxis;
    }

    public ArrayList<String> getShowByElements() {
        return showByElements;
    }

    public void setShowByElements(ArrayList<String> showByElements) {
        this.showByElements = showByElements;
    }
}
