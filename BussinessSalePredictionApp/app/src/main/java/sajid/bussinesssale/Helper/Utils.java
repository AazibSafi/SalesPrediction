package sajid.bussinesssale.Helper;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import sajid.bussinesssale.Database.DB_Config;
import sajid.bussinesssale.Interfaces.CallBack;
import sajid.bussinesssale.Server.ServerConfig;

/**
 * Created by hp on 4/4/2017.
 */

public class Utils {

    public static void showRadioChoiceDialog(final Context context, final String[] radioItems, String title, final TextView _tv, final CallBack call) {

        final String[] radioValue = {""};
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setSingleChoiceItems(radioItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String selectedValue = radioItems[i];
                        radioValue[0] = selectedValue;

                    }
                });

        builder.setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                _tv.setText(radioValue[0]);
                call.setValue(radioValue[0]);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static float maxElement(float[] arr,float[] arr2) {
        float x = maxElement(arr);
        float y = maxElement(arr2);

        return x > y ? x : y;
    }

    public static float maxElement(float[] arr) {
        int max = 0;
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] > arr[max]) {
                max = i;
            }
        }
        return arr[max];
    }

    public static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void showResponse(JSONObject JSONresponse) {
        try {
            int new_db_version = JSONresponse.getInt(ServerConfig.RESPONSE.LATEST_DB_VERSION);
            JSONArray SalesArray = JSONresponse.getJSONArray(ServerConfig.RESPONSE.TOTALSALES);

            Log.e(ServerConfig.RESPONSE.LATEST_DB_VERSION,new_db_version+"");

            int i;
            for (i = 0; i < SalesArray.length(); i++) {

                JSONObject sale = SalesArray.getJSONObject(i);

                Log.e("JSON-"+i, sale.getInt(DB_Config.SALES.ID) + " , " +
                        sale.getString(DB_Config.SALES.COMPANY).toString() + " , " +
                        sale.getString(DB_Config.SALES.PRODUCT).toString() + " , " +
                        sale.getString(DB_Config.SALES.REGION).toString() + " , " +
                        sale.getDouble(DB_Config.SALES.AMOUNT) + " , " +
                        sale.getString(DB_Config.SALES.SALE_DATE)
                );
            }

            Log.e("Sales",i+" Records saved in DB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static float[] toFloat(final ArrayList<Float> list) {

        final float[] returnArray = new float[list.size()];
        int valueIndex = 0;

        for (final Float value : list) {
            returnArray[valueIndex++] = value;
        }

        return returnArray;
    }

    public static double[] convertFloatsToDoubles(float[] input)
    {
        if (input == null)
        {
            return null; // Or throw an exception - your choice
        }
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++)
        {
            output[i] = input[i];
        }
        return output;
    }

    public static void printValues(float[] Value,String title) {
        System.out.print("\n"+title+": ");
        for(int i=0;i<Value.length;i++) {
            System.out.print(Value[i]+" , ");
        }
        System.out.println();
    }

    public static String MonthStyle(int monthNo) {
        if(monthNo < 10) {
            return "0" + monthNo;
        }
        return "" + monthNo;
    }
}
