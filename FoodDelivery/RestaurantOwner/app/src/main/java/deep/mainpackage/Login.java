package deep.mainpackage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import deep.mainpackage.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class Login extends AppCompatActivity {
    private Toolbar toolbar;

    Activity activity;
    Context context;
    String regId;
    GoogleCloudMessaging gcm;
    public static final String REG_ID = "regId";
    static final String TAG = "Register Activity";

   /* public EditText txtUsername;
    EditText txtPassword;
    AppCompatButton btnLogin;*/

    @InjectView(R.id.input_username) EditText txtUsername;
    @InjectView(R.id.input_password) EditText txtPassword;
    @InjectView(R.id.btn_login)
    AppCompatButton btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity=this;
        context=getBaseContext();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        ButterKnife.inject(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {

        if (validate()) {

            /*Intent intent=new Intent(getBaseContext(),MenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/

            restaurantOwnerLoginAsync login=new restaurantOwnerLoginAsync(txtUsername.getText().toString(),txtPassword.getText().toString());
            login.execute();

        }

    }

    private boolean validate() {
        boolean valid = true;

        String email = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        if (email.isEmpty()) {
            txtUsername.setError("cannot be empty");
            valid = false;
        } else {
            txtUsername.setError(null);
        }

        if (password.isEmpty()) {
            txtPassword.setError("cannot be empty");
            valid = false;
        } else {
            txtPassword.setError(null);
        }

        return valid;

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage("You are exiting the application.\nAre you sure ?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            try
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(context, FirstActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
            catch(Exception ex)
            {
                Log.e("OnBackPressed", ex.getMessage());
            }
        }
    };

    public String registerGCM(String restaurantID) {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground(restaurantID);

            Log.d("RegisterActivity",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);
        } else {
            Toast.makeText(getApplicationContext(),
                    "RegId already available. RegId: " + regId,
                    Toast.LENGTH_LONG).show();
        }
        return regId;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                FirstActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        return registrationId;
    }

    private void registerInBackground(final String restaurantID) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(Config.GOOGLE_PROJECT_NUMBER);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;
                    if(!regId.equals(""))
                    {

                        UpdateGCMId updateGCM=new UpdateGCMId(restaurantID,regId);
                        updateGCM.execute();
                    }

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(),
                        "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                        .show();
                saveRegisterId(context, regId);
            }
        }.execute(null, null, null);
    }

    private void saveRegisterId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(
                FirstActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        Log.i(TAG, "Saving regId on app version ");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.commit();
    }

    class restaurantOwnerLoginAsync extends AsyncTask<Void, Void, Void> {

        String unm,pwd;
        private SQLiteAdapter sqliteAdapter;
        restaurantOwnerLoginAsync(String unm,String pwd){
            this.unm=unm;
            this.pwd=pwd;
        }

        HttpResponse response;
        final String url = "http://159.203.1.98:8081/restaurantLogin";
        public List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Login.this, R.style.AppTheme_ProgressDialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                parameters.add(new BasicNameValuePair("uname",unm));
                parameters.add(new BasicNameValuePair("password",pwd));
                // Add your data
                httppost.setEntity(new UrlEncodedFormEntity(parameters));

                // Execute HTTP Post Request
                response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                Log.e("Error",e.getMessage());
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(Void unused) {


            try {

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String jsonString;
                if((jsonString=reader.readLine())!=null){
                    JSONObject jsonObject=new JSONObject(jsonString);

                    sqliteAdapter = new SQLiteAdapter(context);
                    sqliteAdapter.openToWrite();
                    sqliteAdapter.insertRestaurant(jsonObject.getString("uname"), jsonObject.getString("pwd"), jsonObject.getString("name"), jsonObject.getString("address"), jsonObject.getString("contactName"), jsonObject.getString("contactNumber"),jsonObject.getString("deliveryCompany_id"), jsonObject.getString("_id"));
                    sqliteAdapter.close();

                    progressDialog.dismiss();
                    Log.i("response",jsonString.toString());

                    regId = registerGCM(jsonObject.getString("_id"));

                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                //String jsonString = reader.readLine();
                else {
                    Toast.makeText(getApplicationContext(), "not valid credentials", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }

    }
}
