package sajid.bussinesssale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.internal.Util;
import sajid.bussinesssale.Charts.ChartEngineActivity;
import sajid.bussinesssale.Database.DBHelper;
import sajid.bussinesssale.Helper.UserMessage;
import sajid.bussinesssale.Helper.Utils;
import sajid.bussinesssale.Interfaces.ResponseCallBack;
import sajid.bussinesssale.Prediction.NeuralNetworkController;
import sajid.bussinesssale.PrimarySharedData.AppData;
import sajid.bussinesssale.Charts.ChartInfo;
import sajid.bussinesssale.Server.VolleyServerHandler;
import sajid.bussinesssale.Model.Sales;

/**
 * Created by aazib on 08-Jun-17.
 */

public class Controller {

    private static Controller controller;
    public static Activity activity;
    DBHelper dbHelper;

    AppData appData;            // Whole Data -- Complete list
    ChartInfo chartInfo;    // Selected Data to Compare

    String[] chartTypes;

    private Controller() {
        chartInfo = new ChartInfo();
        appData = new AppData();
        dbHelper = new DBHelper(activity);
        setupLocalData();
    }

    public static Controller getInstance(Activity act) {
        activity = act;
        return controller;
    }

    public static void setup(Activity activity) {
        Controller.activity = activity;
        controller = new Controller();
    }

    void setupLocalData() {

        chartTypes = new String[] {
                "BAR_CHART" , "LINE_CHART", "SCATTER_CHART",
                "TIME_CHART", "CUBE_CHART"
        };

        chartInfo.setMonths( new String[] {
                "Jan", "Feb" , "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        } );
    }

    public void initializeData(final ResponseCallBack controllerResponse) {

        if ( Utils.isNetworkAvailable(activity) ) {

            final int currentDBVersion = dbHelper.getDbVersion();

            VolleyServerHandler.isServerUpdatesAvailable(currentDBVersion,new ResponseCallBack() {

                @Override
                public void onSuccess(Object obj) {
                    if( (boolean) obj ) {
                        Log.e("Server","Updates Available");

                        UserMessage.showLong("Loading...");
                        VolleyServerHandler.getServerData(new ResponseCallBack() {

                            @Override
                            public void onSuccess(Object obj) {

                                JSONObject jsonObject = (JSONObject) obj;

                                if (jsonObject != null) {  // there is no problem in fetching data from server
                                    dbHelper.updateDatabase(jsonObject,currentDBVersion);
                                    controllerResponse.onSuccess( appData.NO_ARGUMENT );
                                }

                                getDataFromDatabase(controllerResponse);
                            }

                            @Override
                            public void onFailure(String error) {
                                Log.e("ServerData", error);
                                getDataFromDatabase(controllerResponse);
                            }
                        });
                    }
                    else {
                        Log.e("Server", "No Updates Available");
                        getDataFromDatabase(controllerResponse);
                    }
                }

                @Override
                public void onFailure(String error) {
                    Log.e("Server", error);
                    getDataFromDatabase(controllerResponse);
                }
            });
        }
        else
            getDataFromDatabase(controllerResponse);
    }

    public void getDataFromDatabase(final ResponseCallBack databaseResponse) {

        List<ArrayList<String>> DB_Data = dbHelper.fetchDBData();

        if(!DB_Data.isEmpty()) {

            String[] CompanyList = DB_Data.get(0).toArray(new String[0]);
//            String[] ProductList = DB_Data.get(1).toArray(new String[0]);
            String[] ProductList = {"Select Company to get Product"};
            String[] RegionList = DB_Data.get(1).toArray(new String[0]);
            String[] YearList = DB_Data.get(2).toArray(new String[0]);

            appData = new AppData(CompanyList, ProductList, RegionList, YearList);

            databaseResponse.onSuccess(true);
        }
        else
            databaseResponse.onFailure("Check your internet Connection");
    }

    public void setChartTypes(String[] chartTypes) {
        this.chartTypes = chartTypes;
    }

    public String[] getChartTypes() {
        return chartTypes;
    }

    public void setProductListValues(String companyName, int index) {

        String[] products = dbHelper.getProducts(companyName).toArray(new String[0]);

        if(index==1)
            appData.setProductsList_1(products);
        else if(index==2)
            appData.setProductsList_2(products);
        else
            Log.e("index","Wrong Index Parsed");
    }


    public void setSalesValuesInCompareObject(Sales obj1, Sales obj2, boolean isPredictEnabled) {

        float[] salesSeries = new float[chartInfo.getMonths().length];

        if(!isPredictEnabled) {
            HashMap<Integer, Float> salesMap;

//            For Sales Object 1
            salesMap = dbHelper.getProductSalesValues(obj1.getCompany(),
                            obj1.getProduct(), obj1.getRegion(), obj1.getYear());
            salesSeries = manipulateSalesSeries(salesMap);
            obj1.setSalesPerMonth(salesSeries);

//            Utils.printValues(salesSeries,"Sales Obj1");

//            For Sales Object 2
            salesMap = dbHelper.getProductSalesValues(obj2.getCompany(),
                            obj2.getProduct(), obj2.getRegion(), obj2.getYear());
            salesSeries = manipulateSalesSeries(salesMap);
            obj2.setSalesPerMonth(salesSeries);

//            Utils.printValues(salesSeries,"Sales Obj2");
        }
        else {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
//            For Sales Object 1
            ArrayList<float[]> recentPrediction = new ArrayList<>();
            for(int year=currentYear;year<Integer.parseInt(obj1.getYear());year++) {
                salesSeries = getSalesForObject(obj1, recentPrediction);
//                Utils.printValues(salesSeries, "Predicted Sales Obj1");
            }
            obj1.setSalesPerMonth(salesSeries);

//            For Sales Object 2
            recentPrediction.clear();

            for(int year=currentYear;year<Integer.parseInt(obj2.getYear());year++) {
                salesSeries = getSalesForObject(obj2, recentPrediction);
//                Utils.printValues(salesSeries, "Predicted Sales Obj2");
            }
            obj2.setSalesPerMonth(salesSeries);
        }

        obj1.setColor(Color.GREEN);
        obj2.setColor(Color.BLACK);
    }

    public float[] getSalesForObject(Sales obj,ArrayList<float[]> recentPrediction) {
        float[] salesSeriesTemp = new float[chartInfo.getMonths().length];

        // getting each month Data of every Year
        for(int i=0;i<chartInfo.getMonths().length;i++) {
            ArrayList<Float> dbResult =
                    dbHelper.getProductSalesValuesByMonth(obj.getCompany(),obj.getProduct(), obj.getRegion(),i+1);

            if(!dbResult.isEmpty()) {

                for(int k=0;k<recentPrediction.size();k++) {  // Taking also input from previus predictions
                    dbResult.add(recentPrediction.get(k)[i]);
                }

                float[] salesOfMonth = Utils.toFloat(dbResult);

                Utils.printValues(salesOfMonth,"Data Passing to ANN");
//         Artificial Neural Network Prediction Algorithm
                NeuralNetworkController salesPredictor = new NeuralNetworkController();
                double[] predictedValues = salesPredictor.doPrediction(Utils.convertFloatsToDoubles(salesOfMonth));

                salesSeriesTemp[i] = Math.round(predictedValues[0]);    // Predicted Output Value for Month i
                Log.e("Predicted","Predicted Data "+salesSeriesTemp[i]+" of Month "+(i+1));
            }
            else {
                salesSeriesTemp[i] = 0;
                Log.e("NotFound","No Data Month "+(i+1));
            }
        }
        recentPrediction.add(salesSeriesTemp);
        return salesSeriesTemp;
    }

    public float[] manipulateSalesSeries(HashMap<Integer,Float> sales) {
        float saleSeries[] = new float[chartInfo.getMonths().length];
        for(int i=0;i<saleSeries.length;i++)
            saleSeries[i] = 0;

        Iterator it = sales.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            int key = (int) entry.getKey();
            saleSeries[key-1] = sales.get(key);     // 1-12 Months, 0-11 int
        }

        return saleSeries;
    }
    public void resetDB() {
        dbHelper.resetLookUp();
    }

    public void setCompareObjectsForChartInfo(Sales obj1, Sales obj2) {
        // Setting data to compare in Chart Activity
        chartInfo.resetComparableObjects();
        chartInfo.addItemInComparableObject(obj1);
        chartInfo.addItemInComparableObject(obj2);
    }

    public void signupUser(String username, String password) {
//        VolleyServerHandler.signupUser(username,password);
    }

    public void setSelectedChartType(String value) {
        chartInfo.setChartTypeSelected( value );
    }

    public void showChart() {
        Intent intent = new Intent(activity, ChartEngineActivity.class);
        activity.startActivity(intent);
    }

    public AppData getAppData() {
        return appData;
    }

    public ChartInfo getChartInfo() {
        return chartInfo;
    }

    public String[] getPredictionYears() {
        int NoOfNextYears = 5;

        String[] years = new String[NoOfNextYears];
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=0;i<NoOfNextYears;i++) {
            years[i] = String.valueOf(year+i+1);
        }
        return years;
    }
}