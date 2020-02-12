package sajid.bussinesssale.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import sajid.bussinesssale.Controller;
import sajid.bussinesssale.Helper.UserMessage;
import sajid.bussinesssale.R;
import sajid.bussinesssale.Helper.Utils;
import sajid.bussinesssale.Interfaces.CallBack;
import sajid.bussinesssale.Model.Sales;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView _txt_year, _txt_chartType, _txt_company_l, _txt_company_r, _txt_product_l,_txt_product_r,_txt_region_l,_txt_region_r;
    RelativeLayout _btn_year, _btn_chartType, _btn_company_l, _btn_company_r, _btn_product_l,_btn_product_r,_btn_region_l,_btn_region_r;
    Button _btn_compare;
    Switch predictSwitch;

    Sales salesObj1, salesObj2;

    String companyHint = "Select Company";
    String productHint = "Select Product";
    String regionHint = "Select Region";
    String yearHint = "Select Year";
    String chartTypeHint = "Select Chart Type";

    Controller controller;
    String[] yearsList;
    boolean predictEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindUIElements();
        defaultNamesOfDropdowns();
        setupListener();

        controller = Controller.getInstance(this);

        salesObj1 = new Sales();
        salesObj2 = new Sales();

        predictEnabled = false;
    }

    void defaultNamesOfDropdowns() {
        _txt_company_l.setText(companyHint);
        _txt_company_r.setText(companyHint);
        _txt_product_l.setText(productHint);
        _txt_product_r.setText(productHint);
        _txt_region_l.setText(regionHint);
        _txt_region_r.setText(regionHint);
        _txt_year.setText(yearHint);
        _txt_chartType.setText(chartTypeHint);
    }

    boolean isAllFieldSelected() {
        if( _txt_company_l.getText().toString().equalsIgnoreCase(companyHint)
            || _txt_company_r.getText().toString().equalsIgnoreCase(companyHint)
            || _txt_product_l.getText().toString().equalsIgnoreCase(productHint)
            || _txt_product_r.getText().toString().equalsIgnoreCase(productHint)
            || _txt_region_l.getText().toString().equalsIgnoreCase(regionHint)
            || _txt_region_r.getText().toString().equalsIgnoreCase(regionHint)
            || _txt_year.getText().toString().equalsIgnoreCase(yearHint)
            || _txt_chartType.getText().toString().equalsIgnoreCase(chartTypeHint)
         )
            return false;

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.log_out:
                Prefs.putBoolean("isSignedUp",false);
                startActivity(new Intent(MainActivity.this,SplashActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void bindUIElements() {
        _btn_compare = (Button) findViewById(R.id.btn_compare);
        _txt_company_l = (TextView) findViewById(R.id.txt_company_l);
        _txt_company_r = (TextView) findViewById(R.id.txt_comapany_r);
        _txt_product_l = (TextView) findViewById(R.id.txt_product_l);
        _txt_product_r = (TextView) findViewById(R.id.txt_product_r);
        _txt_region_l = (TextView) findViewById(R.id.txt_region_l);
        _txt_region_r = (TextView) findViewById(R.id.txt_region_r);
        _txt_year = (TextView) findViewById(R.id.txt_year);
        _txt_chartType = (TextView) findViewById(R.id.txt_chartType);

        _btn_company_l = (RelativeLayout) findViewById(R.id.btn_company_l);
        _btn_company_r = (RelativeLayout) findViewById(R.id.btn_company_r);
        _btn_product_l = (RelativeLayout) findViewById(R.id.btn_product_l);
        _btn_product_r = (RelativeLayout) findViewById(R.id.btn_product_r);
        _btn_region_l = (RelativeLayout) findViewById(R.id.btn_region_l);
        _btn_region_r = (RelativeLayout) findViewById(R.id.btn_region_r);
        _btn_year = (RelativeLayout) findViewById(R.id.btn_year);
        _btn_chartType = (RelativeLayout) findViewById(R.id.btn_chartType);

        predictSwitch = (Switch) findViewById(R.id.PredictSwitch);
        predictSwitch.setChecked(false);
    }

    private void setupListener() {
        _btn_company_l.setOnClickListener(this);
        _btn_company_r.setOnClickListener(this);
        _btn_product_l.setOnClickListener(this);
        _btn_product_r.setOnClickListener(this);
        _btn_region_l.setOnClickListener(this);
        _btn_region_r.setOnClickListener(this);
        _btn_year.setOnClickListener(this);
        _btn_chartType.setOnClickListener(this);

        _btn_compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAllFieldSelected()) {
                    controller.setSalesValuesInCompareObject(salesObj1, salesObj2,predictEnabled);
                    controller.setCompareObjectsForChartInfo(salesObj1, salesObj2);
                    controller.showChart();
                }
                else
                    Toast.makeText(getApplicationContext(),"Select All Fields to Proceed",Toast.LENGTH_LONG).show();
            }
        });
        predictSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                salesObj1.setYear( "" );
                salesObj2.setYear( "" );
                _txt_year.setText(yearHint);

                if(predictSwitch.isChecked()) {
                    predictEnabled = true;
                    yearsList = controller.getAppData().getYearsList();

                    String[] predictYearList = controller.getPredictionYears();
                    controller.getAppData().setYearsList(predictYearList);
                    Log.e("Switch","on");
                }
                else {
                    predictEnabled = false;
                    controller.getAppData().setYearsList(yearsList);
                    Log.e("Switch","off");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btn_company_l) {

            Utils.showRadioChoiceDialog(MainActivity.this,controller.getAppData().getCompaniesList() ,companyHint,_txt_company_l,new CallBack(){
                @Override
                public void setValue(Object obj) {
                    salesObj1.setCompany((String) obj);
                    controller.setProductListValues((String) obj,1);
                }
            });

        } else if(id == R.id.btn_company_r) {

            Utils.showRadioChoiceDialog(MainActivity.this,controller.getAppData().getCompaniesList(),companyHint,_txt_company_r,new CallBack(){
                @Override
                public void setValue(Object obj) {
                    salesObj2.setCompany((String) obj);
                    controller.setProductListValues((String) obj,2);
                }
            });
        } else if(id == R.id.btn_product_l && checkifCompanySelected(_txt_company_l)) {

            Utils.showRadioChoiceDialog(MainActivity.this,controller.getAppData().getProductsList_1(), productHint, _txt_product_l, new CallBack() {
                @Override
                public void setValue(Object obj) {
                    salesObj1.setProduct((String) obj);
                }
            });

        } else if(id == R.id.btn_product_r && checkifCompanySelected(_txt_company_r)) {

            Utils.showRadioChoiceDialog(MainActivity.this,controller.getAppData().getProductsList_2(),productHint,_txt_product_r,new CallBack(){
                @Override
                public void setValue(Object obj) {
                    salesObj2.setProduct((String) obj);
                }
            });

        } else if(id == R.id.btn_region_l) {

            Utils.showRadioChoiceDialog(MainActivity.this,controller.getAppData().getRegionsList(),regionHint,_txt_region_l,new CallBack(){
                @Override
                public void setValue(Object obj) {
                    salesObj1.setRegion((String) obj);
                }
            });

        } else if(id == R.id.btn_region_r) {

            Utils.showRadioChoiceDialog(MainActivity.this,controller.getAppData().getRegionsList(), regionHint, _txt_region_r,new CallBack(){
                @Override
                public void setValue(Object obj) {
                    salesObj2.setRegion((String) obj);
                }
            });

        } else if(id == R.id.btn_year) {

            Utils.showRadioChoiceDialog(MainActivity.this,controller.getAppData().getYearsList(),yearHint,_txt_year,new CallBack(){
                @Override
                public void setValue(Object obj) {
                    salesObj1.setYear( (String) obj );
                    salesObj2.setYear( (String) obj );
                }
            });
        } else if(id == R.id.btn_chartType) {

            Utils.showRadioChoiceDialog(MainActivity.this,controller.getChartTypes(),chartTypeHint,_txt_chartType,new CallBack(){
                @Override
                public void setValue(Object obj) {
                    controller.setSelectedChartType( (String) obj );
                }
            });
        }
    }

    boolean checkifCompanySelected(TextView view) {
        if( view.getText().toString().equalsIgnoreCase(companyHint) ) {
            UserMessage.show("Select the Company");
            return false;
        }
        return true;
    }
}