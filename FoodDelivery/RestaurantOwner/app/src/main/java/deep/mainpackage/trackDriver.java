package deep.mainpackage;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class trackDriver extends android.support.v4.app.Fragment {

    View rootView;
    private Activity activity;
    private Context context;
    private GoogleMap googleMap;
    private Timer timer;
    private Marker marker=null;
    CircleOptions circleOptions = new CircleOptions();
    Circle mapCircle=null;
    private Marker Firstmarker;
    private Toolbar toolbar;
    boolean isFirst=true;
    Spinner driverSpinner;


    public trackDriver() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_track_driver, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity=getActivity();
        context=activity.getBaseContext();

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Track driver");

        driverSpinner=(Spinner)toolbar.findViewById(R.id.driverSpinner);
        driverSpinner.setVisibility(View.VISIBLE);

        googleMap = getMapFragment().getMap();
        getDriverLocationAsync();
        //getAllonlineDriversAsync();
    }

    public SupportMapFragment getMapFragment() {
        android.support.v4.app.FragmentManager fm = null;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            fm = ((FragmentActivity)getActivity()).getSupportFragmentManager();
        } else {
            fm = getChildFragmentManager();
        }

        return (SupportMapFragment) fm.findFragmentById(R.id.trackDriverMap);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    public void getDriverLocationAsync() {
        final Handler handler = new Handler();
        timer=new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            getDriverLocation driverLocation=new getDriverLocation(1);
                            driverLocation.execute();

                            getAllonlineDrivers driversLocation=new getAllonlineDrivers();
                            driversLocation.execute();

                        } catch (Exception e) {
                            Toast.makeText(context, "CallAsyncError", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000); //execute in every 5000 ms
    }

    public void getAllonlineDriversAsync() {
        final Handler handler = new Handler();
        timer=new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            getAllonlineDrivers driverLocation=new getAllonlineDrivers();
                            driverLocation.execute();
                        } catch (Exception e) {

                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 20000); //execute in every 5000 ms
    }

    public void drawPath(LatLng departure,LatLng arrival)
    {
        PolylineOptions options = new PolylineOptions();

        options.color( getResources().getColor(R.color.darkPrimaryColor) );
        options.width( 10 );
        options.visible( true );
        /*for ( Location locRecorded : listLocsToDraw )
        {
            options.add( new LatLng( locRecorded.getLatitude(),
                    locRecorded.getLongitude() ) );
        }*/
        options.add( departure );
        options.add(arrival);

        googleMap.addPolyline(options);
    }

    class getDriverLocation extends AsyncTask<Void, Void, Void> {
        int _id;
        HttpResponse response;

        public getDriverLocation(int _id){
            this._id=_id;
        }

        final String url="http://159.203.1.98:8081/getDriverLocation?_id="+_id;
        public List<NameValuePair> parameters = new ArrayList<NameValuePair>();

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
                        JSONObject jsonObject=new JSONObject(jsonString);
                        JSONObject jsonObject2= (JSONObject) jsonObject.get("location");
                        LatLng driverLocation=new LatLng(Double.parseDouble(jsonObject2.get("latitude").toString()),Double.parseDouble(jsonObject2.get("longitude").toString()));
                        if(marker!=null)
                        {
                            marker.remove();
                        }
                        marker= googleMap.addMarker(new MarkerOptions().position(driverLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)));
                        if(mapCircle!=null)
                        {
                            mapCircle.remove();
                        }
                        circleOptions.center(marker.getPosition()).radius(50).strokeColor(Color.parseColor("#CC303F9F")).strokeWidth(2);
                        circleOptions.fillColor(Color.parseColor("#33C5CAE9"));
                        mapCircle=googleMap.addCircle(circleOptions);

                        CircleOptions dotCircleOptions = new CircleOptions();
                        Circle dotCircle=null;
                        dotCircleOptions.center(marker.getPosition()).radius(1).strokeColor(Color.parseColor("#CC303F9F")).strokeWidth(2);
                        dotCircleOptions.fillColor(getResources().getColor(R.color.darkPrimaryColor));
                        dotCircle=googleMap.addCircle(dotCircleOptions);

                        if(isFirst)
                        {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                            isFirst=false;
                        }
                    }
                    //String jsonString = reader.readLine();
                    else {
                        Toast.makeText(context, "not valid credentials", Toast.LENGTH_SHORT).show();
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

    class getAllonlineDrivers extends AsyncTask<Void, Void, Void> {

        HttpResponse response;

        public getAllonlineDrivers(){
        }

        final String url="http://159.203.1.98:8081/getAllOnlineDrivers";

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
                        ArrayList<String> nameList=new ArrayList<String>();
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject object= (JSONObject) jsonArray.get(i);
                            String name=object.get("name").toString();
                            nameList.add(name);
                        }

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(activity,R.layout.spinner_item, nameList);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                        driverSpinner.setAdapter(spinnerArrayAdapter);

                    }
                    else {
                        Toast.makeText(context, "not valid credentials", Toast.LENGTH_SHORT).show();
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

}
