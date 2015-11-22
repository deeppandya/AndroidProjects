package deep.mainpackage;

/**
 * Created by Deep on 18/11/2015.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Deep on 29/10/2015.
 */
public class getAllDeliveryCompany extends AsyncTask<Void, Void, Void> {

    HttpResponse response;
    private SQLiteAdapter sqliteAdapter;
    Context context;

    public getAllDeliveryCompany(Context context)
    {
        this.context=context;
    }

    final String url="http://159.203.1.98:8081/getAllDeliveryCompany";

    @Override
    protected Void doInBackground(Void... voids) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        try {

            // Execute HTTP Post Request
            response = httpclient.execute(httpget);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }

    protected void onPostExecute(Void unused) {

        if (response != null) {

            try {

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String jsonString;
                if((jsonString=reader.readLine())!=null){
                    JSONArray jsonArray=new JSONArray(jsonString);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject object= (JSONObject) jsonArray.get(i);
                        sqliteAdapter = new SQLiteAdapter(context);
                        sqliteAdapter.openToWrite();
                        sqliteAdapter.insertDeliveryCompany(object.getString("name"), object.getString("_id"));
                        sqliteAdapter.close();
                    }
                }
                else {
                    //Toast.makeText(context, "not valid credentials", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("Output : ", response.toString());

        } else {

            Log.e("Output : ", "Error");

        }
    }

}
