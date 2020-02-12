package sajid.bussinesssale.Charts;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.RangeBarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

import sajid.bussinesssale.Controller;
import sajid.bussinesssale.R;
import sajid.bussinesssale.Helper.Utils;
import sajid.bussinesssale.Model.Sales;

// Comparing information stored in Product Data
public class ChartEngineActivity extends AppCompatActivity {

    TextView RegionText;
    View chart;
    String regionsText="", chartType = "";

    float MAX_SALE;
    ArrayList<Sales> compareObjects;
    ChartInfo chartInfo;

    Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_engine);

        controller = Controller.getInstance(this);
        chartInfo = controller.getChartInfo();

        chartType = chartInfo.getChartTypeSelected();
        compareObjects = chartInfo.getComparableObjects();

        MAX_SALE = Utils.maxElement(compareObjects.get(0).getSalesPerMonth(),
                compareObjects.get(1).getSalesPerMonth()) + 50;

        setup_chartTextLabels();

        drawChart();
    }

    void setup_chartTextLabels() {
        chartInfo.setX_Title( "Year: " + compareObjects.get(0).getYear() );    // Both objects has same Year to compare

        String companyNames = "";

        // Size is 2, incase of comparing 2 objects
        for (int i = 0; i < compareObjects.size(); i++) {
            regionsText = regionsText + Utils.getColoredSpanned(compareObjects.get(i).getCompany(),compareObjects.get(i).getColor()+"")
                        + " from Region : "
                        + Utils.getColoredSpanned(compareObjects.get(i).getRegion(),compareObjects.get(i).getColor()+"")
                        + "<br>";
            companyNames = companyNames + ((companyNames != "") ? " VS " : "") + compareObjects.get(i).getCompany();
        }


        chartInfo.setChartTitle( companyNames + " " + "Sales Comparison" );

        RegionText = (TextView) findViewById(R.id.RegionText);
        RegionText.setText(Html.fromHtml(regionsText));
    }

    void drawChart() {
        XYMultipleSeriesDataset dataset = getBarDataset();
        XYMultipleSeriesRenderer multiRenderer = getXYMultipleSeriesRenderer();

        for(int i=0;i<compareObjects.size();i++) {
            XYSeriesRenderer objectRenderer = getXYSeriesRenderer(compareObjects.get(i).getColor());
            multiRenderer.addSeriesRenderer(objectRenderer);
        }

        setTextLabel(multiRenderer);

        displayGraph(dataset,multiRenderer);
    }

    XYMultipleSeriesDataset getBarDataset() {

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        for(int i=0;i<compareObjects.size();i++) {
            XYSeries dataSeries = new XYSeries(compareObjects.get(i).getProduct());
String x="";
            float[] sales = compareObjects.get(i).getSalesPerMonth();
            for(int j=0;j<sales.length;j++) {
                dataSeries.add(j, sales[j]);
                x += sales[j] + " , ";
            }

            Log.e("DataSet","Product: "+compareObjects.get(i).getProduct() + " /--/ Sales: " + x);
            dataset.addSeries(dataSeries);
        }

        return dataset;
    }

    XYSeriesRenderer getXYSeriesRenderer(int color) {
        XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
        seriesRenderer.setColor(color); // set color of the graph
        seriesRenderer.setFillPoints(true);
        seriesRenderer.setLineWidth(2);
        seriesRenderer.setDisplayChartValues(true);
        seriesRenderer.setChartValuesTextSize(20);

        return seriesRenderer;
    }

    void setTextLabel(XYMultipleSeriesRenderer multiRenderer) {
        String months[] = chartInfo.getMonths();
        for(int i = 0; i< months.length; i++) {
            multiRenderer.addXTextLabel(i, months[i]);      // X Axis Labels
        }
    }

    XYMultipleSeriesRenderer getXYMultipleSeriesRenderer() {
        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle(chartInfo.getChartTitle());
        multiRenderer.setXTitle(chartInfo.getX_Title());
        multiRenderer.setYTitle(chartInfo.getY_Title());

        /***
         * Customizing graphs
         */

        multiRenderer.setChartTitleTextSize(20);    //setting text size of the title

        multiRenderer.setAxisTitleTextSize(15);     //setting text size of the axis title

        multiRenderer.setLabelsTextSize(15);        //setting text size of the graph lable

        multiRenderer.setZoomButtonsVisible(false); //setting zoom buttons visiblity

        multiRenderer.setPanEnabled(false, false);  //setting pan enablity which uses graph to move on both axis

        multiRenderer.setClickEnabled(false);   //setting click false on graph

        multiRenderer.setZoomEnabled(false, false); //setting zoom to false on both axis

        multiRenderer.setShowGridY(false);  //setting lines to display on y axis

        multiRenderer.setShowGridX(false);  //setting lines to display on x axis

        multiRenderer.setFitLegend(true);   //setting legend to fit the screen size

        multiRenderer.setShowGrid(false);   //setting displaying line on grid

        multiRenderer.setZoomEnabled(false);    //setting zoom to false

        multiRenderer.setExternalZoomEnabled(false);    //setting external zoom functions to false

        multiRenderer.setAntialiasing(true);    //setting displaying lines on graph to be formatted(like using graphics)

        multiRenderer.setInScroll(false);   //setting to in scroll to false

        multiRenderer.setLegendHeight(30);  //setting to set legend height of the graph

        multiRenderer.setXLabelsAlign(Paint.Align.CENTER);  //setting x axis label align

        multiRenderer.setYLabelsAlign(Paint.Align.RIGHT);    //setting y axis label to align

        multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);   //setting text style

        multiRenderer.setYLabels(10);   //setting no of values to display in y axis

        multiRenderer.setYAxisMax(MAX_SALE);    // setting y axis max value

        multiRenderer.setXAxisMin(-0.5);    //setting used to move the graph on xaxiz to .5 to the right

        multiRenderer.setXAxisMax(chartInfo.getMonths().length);  //setting max values to be display in x axis

        multiRenderer.setBarSpacing(0.5);   //setting bar size or space between two bars

        multiRenderer.setBackgroundColor(Color.TRANSPARENT);    //Setting background color of the graph to transparent

        multiRenderer.setMarginsColor(getResources().getColor(R.color.transparent_background)); //Setting margin color of the graph to transparent

        multiRenderer.setApplyBackgroundColor(true);

        //setting the margin size for the graph in the order top, left, bottom, right
        multiRenderer.setMargins(new int[]{90, 70, 60, 30});

        return multiRenderer;
    }

    void displayGraph(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer multiRenderer ) {
        //this part is used to display graph on the xml
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);

        //Remove any views before u paint the chart
        chartContainer.removeAllViews();

        switch (chartType) {

            case "BAR_CHART":
                chart = ChartFactory.getBarChartView(this, dataset, multiRenderer, BarChart.Type.DEFAULT);
                break;
            case "LINE_CHART":
                chart = ChartFactory.getLineChartView(this, dataset, multiRenderer);
                break;
            case "SCATTER_CHART":
                chart = ChartFactory.getScatterChartView(this, dataset, multiRenderer);
                break;
            case "TIME_CHART":
                chart = ChartFactory.getTimeChartView(this, dataset, multiRenderer,"Time Chart");
                break;
            case "RANGEBAR_CHART":
                chart = ChartFactory.getRangeBarChartView(this, dataset, multiRenderer, RangeBarChart.Type.DEFAULT);
                break;
            case "CUBE_CHART":
                chart = ChartFactory.getCubeLineChartView(this, dataset, multiRenderer,2);
                break;
            default:
                chart = ChartFactory.getBarChartView(this, dataset, multiRenderer, BarChart.Type.DEFAULT);
                Log.e("Default","Wrong Chart Type Selection");
        }

        Log.i("Drawing",chartType);
        //adding the view to the Linearlayout
        chartContainer.addView(chart);
    }
}
