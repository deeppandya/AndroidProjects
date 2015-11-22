package deep.mainpackage;

import android.os.AsyncTask;
import android.util.Log;

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
 * Created by Deep on 15/11/2015.
 */
public class UpdateGCMId extends AsyncTask<Void, Void, Void> {

    private final HttpClient Client = new DefaultHttpClient();
    HttpResponse response;
    private String restaurantID="";
    private String GCMID="";
    private SQLiteAdapter sqliteAdapter;

    public UpdateGCMId(String restaurantID, String GCMID)
    {
        this.restaurantID=restaurantID;
        this.GCMID=GCMID;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://159.203.1.98:8081/updateGcmID");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

            nameValuePairs.add(new BasicNameValuePair("id", restaurantID ));
            nameValuePairs.add(new BasicNameValuePair("gcmid", GCMID));
            nameValuePairs.add(new BasicNameValuePair("component", "0" ));

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
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String jsonString;
            if((jsonString=reader.readLine())!=null){
                JSONObject object=new JSONObject(jsonString);
            }
            else {
                //Toast.makeText(context, "Add driver failed.\nPlease try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


            /*if (response != null) {
                try {
                    JSONObject object=new JSONObject(response.toString());
                    sqliteAdapter = new SQLiteAdapter(context);
                    sqliteAdapter.openToWrite();

                    sqliteAdapter.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                Log.e("Output : ","Error");

            }*/
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.i("inside"," onCancelled");
    }
}
