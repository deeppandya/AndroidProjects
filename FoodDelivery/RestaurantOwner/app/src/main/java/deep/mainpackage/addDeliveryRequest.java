package deep.mainpackage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deep on 18/11/2015.
 */
public class addDeliveryRequest extends AsyncTask<Void, Void, Void> {

    private final HttpClient Client = new DefaultHttpClient();
    HttpResponse response;

    private String restaurantID="";
    private String deliveryCompanyID="";
    private String deliveryAddress="";
    private String preDate="";
    private String isPre="";

    Activity activity;
    Context context;

    private SQLiteAdapter sqliteAdapter;
    private ProgressDialog progressDialog;

    public addDeliveryRequest(Activity activity,Context context,String restaurantID, String deliveryCompanyID,String deliveryAddress,String preDate,String isPre)
    {
        this.activity=activity;
        this.context=context;
        this.restaurantID=restaurantID;
        this.deliveryCompanyID=deliveryCompanyID;
        this.deliveryAddress=deliveryAddress;
        this.preDate=preDate;
        this.isPre=isPre;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(activity, R.style.AppTheme_ProgressDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://159.203.1.98:8081/addDeliveryRequest");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("deliverCompany_id", deliveryCompanyID ));
            nameValuePairs.add(new BasicNameValuePair("deliveryAddress", deliveryAddress));
            nameValuePairs.add(new BasicNameValuePair("restaurant_id", restaurantID ));
            nameValuePairs.add(new BasicNameValuePair("preDate", preDate ));
            nameValuePairs.add(new BasicNameValuePair("isPre", isPre ));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            response = httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            Log.i("ClientProtocolException", e.getMessage() + "");

        } catch (IOException e) {
            Log.i("IOException", e.getMessage() + "");

        }
        return null;
    }

    protected void onPostExecute(Void unused) {

        try {
            if(response!=null)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String jsonString;
                if((jsonString=reader.readLine())!=null){
                    JSONObject object=new JSONObject(jsonString);

                    sqliteAdapter = new SQLiteAdapter(context);
                    sqliteAdapter.openToWrite();
                    sqliteAdapter.insertDeliveryRequests(object.getString("_id"), object.getString("restaurant_id"), object.getString("deliveryCompany_id"), object.getString("deliveryAddress"), object.getString("driver_id"),object.getString("status"),object.getString("preDate"),object.getString("isPre"));
                    sqliteAdapter.close();

                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(context, "Add driver failed.\nPlease try again.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.i("inside"," onCancelled");
    }
}
