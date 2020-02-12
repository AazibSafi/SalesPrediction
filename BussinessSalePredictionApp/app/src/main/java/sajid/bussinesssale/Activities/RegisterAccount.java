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
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;
import sajid.bussinesssale.R;
import sajid.bussinesssale.Model.User;

public class RegisterAccount extends AppCompatActivity {

    private Realm realm;
    int id = -1;
     EditText _txtfullname;
     EditText _txtemail;
     EditText _txtpass;
     EditText _txtmobile;
    Button _btnreg;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        realm = Realm.getDefaultInstance();

        //To hide AppBar for fullscreen.
        ActionBar ab = getSupportActionBar();
        ab.hide();

        //Referencing EditText widgets and Button placed inside in xml layout file
         _txtfullname = (EditText) findViewById(R.id.txtname_reg);
         _txtemail = (EditText) findViewById(R.id.txtemail_reg);
       _txtpass = (EditText) findViewById(R.id.txtpass_reg);
        _txtmobile = (EditText) findViewById(R.id.txtmobile_reg);
         _btnreg = (Button) findViewById(R.id.btn_reg);

        _btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_txtemail.getText().toString().isEmpty() || _txtfullname.getText().toString().isEmpty() || _txtpass.getText().toString().isEmpty() || _txtmobile.getText().toString().isEmpty()) {

                    Toast.makeText(RegisterAccount.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                }else {

                 regTask task = new regTask();
                    task.execute();
                }
            }
        });


        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RegisterAccount.this,SignupActivity.class));
                finish();
            }
        });



    }


    private class regTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);



            Realm realm = Realm.getDefaultInstance();


            RealmResults<User> results = realm.where(User.class).findAll();
            if(results.size()  == 0) {

                id = 1;

            } else {

                // max is an aggregate function to check maximum value
                id= results.max("id").intValue() + 1;

            }

            realm.beginTransaction();
            User user = realm.createObject(User.class);
            user.setId(id);
            user.setFullname(_txtfullname.getText().toString());
            user.setEmail(_txtemail.getText().toString());
            user.setPassword(_txtpass.getText().toString());
            user.setMobile(_txtmobile.getText().toString());
            realm.commitTransaction();

            pd.dismiss();

            Toast.makeText(RegisterAccount.this, "Registration Complete", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterAccount.this,SignupActivity.class));
            finish();
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
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(RegisterAccount.this);
            pd.setTitle("Uploading");
            pd.setMessage("Please wait for a moment.");
            pd.setIndeterminate(false);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();
        }
    }
}
