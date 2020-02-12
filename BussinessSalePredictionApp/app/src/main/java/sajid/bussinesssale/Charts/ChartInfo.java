package sajid.bussinesssale.Charts;

import java.util.ArrayList;

import sajid.bussinesssale.Model.Sales;

/**
 * Created by aazib on 4/2/2017.
 */

// It contains the Information for comparing between comparableObjects on given year
public class ChartInfo {

    String chartTitle;
    String X_Title = "";    // Set on creation of chart
    String Y_Title = "Sales Amount";

    String[] Months;

    ArrayList comparableObjects;
    String chartTypeSelected;

    public ChartInfo(){
        comparableObjects = new ArrayList<Sales>();
    }

    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }

    public String getChartTitle() {
        return chartTitle;
    }

    public void setX_Title(String x_Title) {
        X_Title = x_Title;
    }

    public String getX_Title() {
        return X_Title;
    }

    public void setY_Title(String y_Title) {
        Y_Title = y_Title;
    }

    public String getY_Title() {
        return Y_Title;
    }

    public void setMonths(String[] months) {
        Months = months;
    }

    public String[] getMonths() {
        return Months;
    }

    public void setChartTypeSelected(String chartTypeSelected) {
        this.chartTypeSelected = chartTypeSelected;
    }

    public String getChartTypeSelected() {
        return chartTypeSelected;
    }

    public void setComparableObjects(ArrayList comparableObjects) {
        this.comparableObjects = comparableObjects;
    }

    public ArrayList getComparableObjects() {
        return comparableObjects;
    }

    public void addItemInComparableObject(Sales obj){
        comparableObjects.add(obj);
    }

    public void resetComparableObjects() {
        comparableObjects.clear();
    }
}