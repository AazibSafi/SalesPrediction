package sajid.bussinesssale.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import io.realm.Realm;
import io.realm.RealmResults;
import sajid.bussinesssale.Controller;
import sajid.bussinesssale.R;
import sajid.bussinesssale.Model.User;

public class SignupActivity extends AppCompatActivity {

     EditText _txtemail;
     EditText _txtpass;
     Button _btnlogin;
    TextView _btnreg;
    private Realm realm;
    private ProgressDialog pd;

    Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ActionBar ab = getSupportActionBar();
        ab.hide();

        controller = Controller.getInstance(this);

        realm = Realm.getDefaultInstance();

        //Referencing UserEmail, Password EditText and TextView for SignUp Now
         _txtemail = (EditText) findViewById(R.id.txtemail);
       _txtpass = (EditText) findViewById(R.id.txtpass);
         _btnlogin = (Button) findViewById(R.id.btnsignin);
         _btnreg = (TextView) findViewById(R.id.btnreg);


        _btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(_txtemail.getText().toString().isEmpty() || _txtpass.getText().toString().isEmpty()) {

                    Toast.makeText(SignupActivity.this, "UserName and Password is required", Toast.LENGTH_SHORT).show();
                }else {

                    regTask task = new regTask();
                    task.execute();


                }
             }
        });


        // Intent For Opening RegisterAccountActivity
        _btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignupActivity.this,RegisterAccount.class);
                startActivity(intent);
            }
        });
    }


    private class regTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(SignupActivity.this);
            pd.setTitle("Uploading");
            pd.setMessage("Please wait for a moment.");
            pd.setIndeterminate(false);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for(int i = 0; i < 5; i++) {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();

            RealmResults<User> user = realm.where(User.class).equalTo("email",_txtemail.getText().toString())
                    .equalTo("password",_txtpass.getText().toString()).findAll();

            controller.signupUser(_txtemail.getText().toString(),_txtpass.getText().toString());

            if(user.size() != 0) {

                Toast.makeText(SignupActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                Prefs.putBoolean("isSignedUp",true);
                startActivity(new Intent(SignupActivity.this,MainActivity.class));
                finish();
            }

            else {
                Toast.makeText(SignupActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

            }


        }
    }
}
