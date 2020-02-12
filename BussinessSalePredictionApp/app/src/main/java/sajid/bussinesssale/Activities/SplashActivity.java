package sajid.bussinesssale.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;

import sajid.bussinesssale.Controller;
import sajid.bussinesssale.Helper.UserMessage;
import sajid.bussinesssale.Interfaces.ResponseCallBack;
import sajid.bussinesssale.R;

public class SplashActivity extends AppCompatActivity {

    Controller controller;
    int TIME_TO_DISPLAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        Controller.setup(this);
        controller = Controller.getInstance(this);

        controller.initializeData(new ResponseCallBack() {

            @Override
            public void onSuccess(Object obj) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Prefs.getBoolean("isSignedUp", false)) {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(SplashActivity.this, SignupActivity.class));
                            finish();
                        }
                    }
                }, TIME_TO_DISPLAY);
                Log.e("Splash","Data Found");
            }

            @Override
            public void onFailure(String error) {
                UserMessage.show(error);
                Log.e("Splash",error);
            }
        });
    }
}
