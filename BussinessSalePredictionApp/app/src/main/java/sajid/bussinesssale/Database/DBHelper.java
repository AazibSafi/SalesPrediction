package sajid.bussinesssale.Database;

/**
 * Created by aazib on 10-Jun-17.
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sajid.bussinesssale.Helper.Utils;

import static sajid.bussinesssale.Database.DB_Config.DATABASE_NAME;
import static sajid.bussinesssale.Database.DB_Config.SALES;
import static sajid.bussinesssale.Database.DB_Config.LOOKUP;

import static sajid.bussinesssale.Server.ServerConfig.RESPONSE;

public class DBHelper extends SQLiteOpenHelper {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS "+SALES.TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+LOOKUP.TABLE);

        String SalesTable = "CREATE TABLE " + SALES.TABLE + " (" +
                            SALES.ID + " int(11)," +
                            SALES.COMPANY + " varchar(255) NOT NULL," +
                            SALES.PRODUCT + " varchar(255) NOT NULL," +
                            SALES.REGION + " varchar(255) NOT NULL," +
                            SALES.AMOUNT + " int(11) NOT NULL," +
                            SALES.SALE_DATE + " date NOT NULL," +
                        "  PRIMARY KEY (`id`)" +
                    ")";

        String LookUpTable = "CREATE TABLE " + LOOKUP.TABLE + " (" +
                            LOOKUP.ID + " int(11)," +
                            LOOKUP.VERSION + " int(11) NOT NULL," +
                        "  PRIMARY KEY (`id`)" +
                        ")";

        db.execSQL(SalesTable);
        db.execSQL(LookUpTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+SALES.TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+LOOKUP.TABLE);
        onCreate(db);
    }

    public boolean insertSales (int id, String company, String product, String region, double amount, Date date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SALES.ID, id);
        contentValues.put(SALES.COMPANY, company);
        contentValues.put(SALES.PRODUCT, product);
        contentValues.put(SALES.REGION, region);
        contentValues.put(SALES.AMOUNT, amount);
        contentValues.put(SALES.SALE_DATE, sdf.format(date));
        db.insertWithOnConflict(SALES.TABLE, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);

//        Log.e("inserting Sale",contentValues.toString());
        return true;
    }

    public boolean insertLookUp (int version) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOOKUP.VERSION, version);
        db.insertWithOnConflict(LOOKUP.TABLE, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);

        Log.i("inserting Version",contentValues.toString());
        return true;
    }

    public ArrayList<String> getProducts(String company) {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT "+SALES.PRODUCT+" FROM " + SALES.TABLE + " WHERE "+SALES.COMPANY+" = '"+company+"'";
        Cursor res =  db.rawQuery(query, null );
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(SALES.PRODUCT)));
            Log.e("getting products",res.getString(res.getColumnIndex(SALES.PRODUCT)));
            res.moveToNext();
        }

        Log.i("Query Products",query);
//        String[] products = new String[] {
//                "Dove Intensive Repair Shampoo (500ml)",
//                "Dove Hair Fall Solution Shampoo (500ml)",
//                "Dove Dryness Care Shampoo(500ml)",
//                "Dove Daily Shine Shampoo (500ml)",
//                "Sunsilk Black Shine Shampoo(500ml)",
//                "Sunsilk Pink (Lusciously thick and Long shampoo) 500ml",
//                "Sunsilk Hair Fall Solution Shampoo (500ml)",
//                "Sunsilk perfect Straight Shampoo (500ml) purple",
//                "Sunsilk Dream soft and Smooth Shampoo(500ml) Yellow",
//                "Pantene Damage Detox Shampoo (500ml)",
//                "Pantene Smooth And Sleek Shampoo (500ml)",
//                "Pantene Color Preserve Shine Shampoo (500ml)",
//                "Head and Shoulder Daily Scalp Care Shampoo(500ml)",
//                "Head and Shoulder Classic Clean Shampoo(500ml)",
//                "Head and Shoulder Clinical Health Shampoo(Blue) (500ml)",
//                "Head and Shoulder Smooth and Silky Shampoo(500ml)",
//                "Head and Shoulder Anti Dandruff Shampoo(500ml)",
//                "Head and Shoulder Repair and Protect Shampoo(500ml)"
//        };
        return array_list;
    }

    ArrayList getCompanies() {
        ArrayList<String> array_list = new ArrayList<String>();

        String query = "SELECT DISTINCT "+SALES.COMPANY+" FROM " + SALES.TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(query, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(SALES.COMPANY)));
            res.moveToNext();
        }

        Log.i("Query Companies",query);

//        String[] companies= new String[] {
//                "Dove", "Head & Shoulders", "Garnier", "Herbal Essences", "L'Oreal", "Matrix"
//        };

        return array_list;
    }

    ArrayList getRegions() {

        ArrayList<String> array_list = new ArrayList<String>();

        String query = "SELECT DISTINCT "+SALES.REGION+" FROM " + SALES.TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(query, null);
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(SALES.REGION)));
            res.moveToNext();
        }

        Log.i("Query Region",query);

//        String[] regions = new String[] {
//                "Malir", "Gulshan", "Johar", "Defence", "Korangi",
//                "Gulshan-e-Ghazi", "Ittehad Town", "Islam Nagar", "Nai Abadi", "Saeedabad",
//                "Muslim Mujahid Colony", "Gulberg", "Rasheedabad", "Cattle Colony", "Qaidabad",
//                "Baldia", "Gulshan-e-Hadeed", "Kemari", "Shah Faisal Town", "Orangi",
//                "Landhi", "Bin Qasim", "Malir Town", "Gulshan-e-Iqbal", "Liaquatabad"
//        };

        return array_list;
    }

    ArrayList getYear() {

        ArrayList<String> array_list = new ArrayList<String>();

        String query = "SELECT DISTINCT strftime('%Y', "+SALES.SALE_DATE+") as valyear FROM "+SALES.TABLE+" ORDER BY valyear";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(query, null );
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("valyear")));
            res.moveToNext();
        }

        Log.i("Query Year",query);
        String[] years = new String[] {
                "2017", "2016", "2015", "2014",
                "2013", "2012", "2011", "2010"
        };

        return array_list;
    }

    public List fetchDBData() {
        List data = new ArrayList<ArrayList<String>>();

//        ArrayList temp = new ArrayList<>();
//        temp.add("Select Company to get Product");

        ArrayList companies = getCompanies();
        ArrayList regions = getRegions();
        ArrayList years = getYear();

        if(!companies.isEmpty() && !regions.isEmpty() && !years.isEmpty()) {
            data.add(getCompanies());
//        data.add(temp);
            data.add(getRegions());
            data.add(getYear());
        }

        return data;
    }

    public void testing() {
        String query =
//                "SELECT * FROM "+SALES.TABLE+"" +
//                " WHERE "+SALES.COMPANY+" = 'Pantene'" +
//                " AND "+SALES.PRODUCT+" = 'Pantene Damage Detox Shampoo (500ml)'" +
//                " AND "+SALES.REGION+"='Jauhar'";

//        "SELECT* FROM "+SALES.TABLE;

        "SELECT strftime('%m',"+SALES.SALE_DATE+") AS Months, SUM(Amount) AS total_Sale_Amount FROM " + SALES.TABLE +
                " WHERE company = 'Dove'" +
                " AND product = 'Dove Intensive Repair Shampoo (500ml)'" +
                " AND region = 'Malir' AND strftime('%Y',"+SALES.SALE_DATE+") = '2017'" +
                " GROUP BY Months";

//        "SELECT strftime('%m',"+SALES.SALE_DATE+") AS Months, SUM(Amount) AS total_Sale_Amount FROM sales" +
//                " WHERE company = 'Pantene' " +
//                " AND product = 'Pantene Damage Detox Shampoo (500ml)' AND region = 'Jauhar'" +
//                " AND strftime('%Y',"+SALES.SALE_DATE+") = '2016'" +
//                " GROUP BY Months";

        Log.e("Testing",query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(query, null);
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            Log.e("Record",Integer.parseInt(res.getString(res.getColumnIndex("Months"))) + " /-/ " +
                    res.getString(res.getColumnIndex("total_Sale_Amount")) + " /-/ ");
            res.moveToNext();
        }

//        while(res.isAfterLast() == false) {
//            Log.e("Record",res.getInt(res.getColumnIndex(SALES.ID)) + " /-/ " +
//                    res.getString(res.getColumnIndex(SALES.COMPANY)) + " /-/ " +
//                    res.getString(res.getColumnIndex(SALES.PRODUCT)) + " /-/ " +
//                    res.getString(res.getColumnIndex(SALES.REGION)) + " /-/ " +
//                    res.getFloat(res.getColumnIndex(SALES.AMOUNT)) + " /-/ " +
//                    res.getString(res.getColumnIndex(SALES.SALE_DATE)) + " /-/ ");
//            res.moveToNext();
//        }
    }

    public ArrayList<Float> getProductSalesValuesByMonth(String company, String product, String region, int monthNo) {

        String month = Utils.MonthStyle(monthNo);
        ArrayList<Float> salesEveryYear = new ArrayList<>();

        String query = "SELECT " + SALES.AMOUNT + " FROM " + SALES.TABLE +
                " WHERE " + SALES.COMPANY + " = '" + company + "' AND " + SALES.PRODUCT + " = '" + product + "'" +
                  " AND " + SALES.REGION + " = '"+region+"' AND strftime('%m',"+SALES.SALE_DATE+") = '" + month + "'" +
                " GROUP BY strftime('%m',"+SALES.SALE_DATE+"),strftime('%Y',"+SALES.SALE_DATE+")";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(query, null);
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            Log.e("Amount: ",res.getFloat(res.getColumnIndex(SALES.AMOUNT))+"");
            salesEveryYear.add(res.getFloat(res.getColumnIndex(SALES.AMOUNT)));
            res.moveToNext();
        }

        Log.e("Query SalesByMonth",query);
        return salesEveryYear;
    }

    public HashMap<Integer,Float> getProductSalesValues(String company, String product, String region, String year) {

        HashMap<Integer,Float> salesAmountPerMonth = new HashMap<Integer,Float>();

        String query = "SELECT strftime('%m',"+SALES.SALE_DATE+") AS Months, SUM("+SALES.AMOUNT+") AS total_Sale_Amount FROM " + SALES.TABLE +
                " WHERE "+ SALES.COMPANY +" = '"+company+"' AND "+ SALES.PRODUCT +" = '" + product + "'" +
                " AND "+ SALES.REGION +" = '" + region + "' AND strftime('%Y', "+SALES.SALE_DATE+") = '" + year + "'" +
                " GROUP BY Months";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(query, null);
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            salesAmountPerMonth.put(Integer.parseInt(res.getString(res.getColumnIndex("Months"))) ,
                                        res.getFloat(res.getColumnIndex("total_Sale_Amount")) );
            Log.e( Integer.parseInt(res.getString(res.getColumnIndex("Months")))+"",
                    res.getFloat(res.getColumnIndex("total_Sale_Amount"))+"" );
            res.moveToNext();
        }

        Log.e("Query SalesSeries",query);
//        printSalesSeries(salesAmountPerMonth);

        return salesAmountPerMonth;
    }

    void printSalesSeries(HashMap<Integer,Float> salesAmountPerMonth) {
        Log.e("SalesSeries","DB Record");
        Iterator it = salesAmountPerMonth.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            int key = (int)entry.getKey();
            float value = salesAmountPerMonth.get(key);
            Log.e("Key: "+key,"Value: "+value);
        }
    }

    public int getDbVersion() {
        int version = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT IFNULL(MAX("+LOOKUP.VERSION+"),"+DB_Config.DEFAULT_VERSION +") AS latest_db_version" +
                                        " FROM "+LOOKUP.TABLE, null );
        res.moveToFirst();

        version = res.getInt(res.getColumnIndex("latest_db_version"));

        Log.i("DB_Version","SqLite : "+version);

        return version;
    }

    public void resetLookUp() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+LOOKUP.TABLE);
    }

    public void updateDatabase(JSONObject jsonObject, int oldDBVersion) {
        try {

            int new_db_version = jsonObject.getInt(RESPONSE.LATEST_DB_VERSION);
            JSONArray SalesArray = jsonObject.getJSONArray(RESPONSE.TOTALSALES);

            Log.e(RESPONSE.LATEST_DB_VERSION,new_db_version+"");
            onUpgrade(this.getWritableDatabase(),oldDBVersion,new_db_version);
            insertLookUp(new_db_version);

            int i;
            for (i = 0; i < SalesArray.length(); i++) {

                JSONObject sale = SalesArray.getJSONObject(i);

                try {
                    insertSales( sale.getInt(SALES.ID),
                                sale.getString(SALES.COMPANY).toString(),
                                sale.getString(SALES.PRODUCT).toString(),
                                sale.getString(SALES.REGION).toString(),
                                sale.getDouble(SALES.AMOUNT),
                                sdf.parse(sale.getString(SALES.SALE_DATE).toString())
                            );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Log.e("Sales",i+" Records saved in DB");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}