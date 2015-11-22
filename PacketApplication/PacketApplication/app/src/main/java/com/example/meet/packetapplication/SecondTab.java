package com.example.meet.packetapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SecondTab extends Activity {
    ArrayList<LatLng> locationlist=new ArrayList<LatLng>();
    ArrayList<LatLng> mylocationlist=new ArrayList<LatLng>();
    ArrayList<Marker> markers=new ArrayList<>();
    final Context context = this;
    private LatLng LOCATION_MONTREAL=new LatLng(45.5017,-73.5673);
    private LatLng LOCATION_TEMP1=null;
    //private LatLng LOCATION_TEMP2=new LatLng(45.4940054,-73.57857739999997);
    private GoogleMap mymap;


    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_tab);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try{
            initializeMap();
        }catch(Exception e){e.printStackTrace();}

        locationlist.add(LOCATION_MONTREAL);
        locationlist.add(LOCATION_TEMP1);
        //locationlist.add(LOCATION_TEMP2);

        new SendHttpRequest().start();



    }

    private void initializeMap() {
        if(mymap==null) {
            mymap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            mymap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //CameraUpdate update= CameraUpdateFactory.newLatLngZoom(LOCATION_MONTREAL, 13);
            //mymap.animateCamera(update);
            //final Marker m1=mymap.addMarker(new MarkerOptions().position(LOCATION_TEMP1).title("Packet1").snippet("Packet details will be shown here"));
            //final Marker m2=mymap.addMarker(new MarkerOptions().position(LOCATION_TEMP2).title("Packet2").snippet("Packet details will be shown here"));

            mymap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(final Marker marker) {
                    marker.hideInfoWindow();
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setView(R.layout.marker_dialog);
                    //TextView t=(TextView)findViewById(R.id.rd);
                    //t.setText(marker.getSnippet());
                    builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("Request",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"Request sent!",Toast.LENGTH_SHORT).show();
                            marker.setVisible(false);
                        }
                    });
                    builder.show();

                }
            });

            /*mymap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setView(R.layout.marker_dialog);
                    builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("Request",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"Request sent!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();


                    //Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            */
        }
        //shows current location with blue dot
        mymap.setMyLocationEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SendHttpRequest {

        public void start() {
            String response;

            /*dialog=new ProgressDialog(activity);
            dialog.setMessage("message");
            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog.show();
            */

            List<NameValuePair> namevaluepair = new ArrayList<NameValuePair>();
            namevaluepair.add(new BasicNameValuePair("userName", "deeep"));
            namevaluepair.add(new BasicNameValuePair("time", "4"));
            namevaluepair.add(new BasicNameValuePair("lang", "-73.5781708653683"));
            namevaluepair.add(new BasicNameValuePair("lant", "45.496416112158"));
            namevaluepair.add(new BasicNameValuePair("reward", "200"));
            //httpPost.setEntity(new UrlEncodedFormEntity(namevaluepair));
            HttpClient httpClient = new DefaultHttpClient();
            //HttpPost httpPost = new HttpPost("http://45.56.67.168:8080/users/registeredUsers");
            HttpGet httpPost = new HttpGet("http://45.56.67.168:8080/packages/");
            //httpPost.addHeader("Accept", "application/json");
            //httpPost.addHeader("Content-Type"," text/plain; charset=UTF-8");
            //HttpGet httpPost = new HttpGet("http://207.34.244.247:88/erp/punch.nsf/(getIDUsers)?openagent&comp="+txtcompanycode.getText()+"&share=oui&time=2014-10-13T21:00:00-05:00&mac=-ltadresse%20mac-gt&lat=latitude&lon=lontitude&alt=altitude&version=1.00&veri=1.00");

            try {
                JSONObject obj = new JSONObject();
                obj.put("userName", "deeep");
                obj.put("time", "4");
                obj.put("lang", "-73.5781708653683");
                obj.put("lant", "45.496416112158");
                obj.put("reward", "200");
                //httpPost.setEntity(new StringEntity(obj.toString(), "UTF-8"));

                //httpPost.setEntity(new UrlEncodedFormEntity(namevaluepair));
                HttpResponse responce = httpClient.execute(httpPost);
                HttpEntity httpEntity = responce.getEntity();

                response = EntityUtils.toString(httpEntity);
                Log.d("response is", response);
                JSONObject jobj = new JSONObject(response).getJSONObject("_embedded");

                JSONArray array = jobj.getJSONArray("boxPackages");
                //String latt = array.getString("lant");
                for(int x = 0; x < array.length(); x++)
                {
                    //Toast.makeText(getApplicationContext(),"posting",Toast.LENGTH_LONG).show();
                    LOCATION_TEMP1=new LatLng(Double.parseDouble(array.getJSONObject(x).getString("lant")),Double.parseDouble(array.getJSONObject(x).getString("lang")));
                    mymap.addMarker(new MarkerOptions().position(LOCATION_TEMP1).title("Package " + x).snippet(array.getJSONObject(x).getString("reward")));
                }
                CameraUpdate update= CameraUpdateFactory.newLatLngZoom(LOCATION_MONTREAL, 13);
                mymap.animateCamera(update);



                //return new JSONObject(response);
                //JSONObject jsonObject = new JSONObject(response.toString());
                //JSONObject mainObject = jsonObject.getJSONObject("_embedded");
                //JSONObject boxObject = jsonObject.getJSONObject("boxPackages");
                //String latt = boxObject.getString("lant");
                //Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();

                /*
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String body = "";
                String _data = "";
                while ((body=rd.readLine())!=null){
                    _data=_data+"\n"+body;
                }
                //Toast.makeText(getApplicationContext(), _data, Toast.LENGTH_LONG).show();
                */
                Log.d("Http Response:", response.toString());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Error:", e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }


        }

    }

}
