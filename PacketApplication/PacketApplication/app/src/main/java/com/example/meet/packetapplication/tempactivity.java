package com.example.meet.packetapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class tempactivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        //SendHttpRequest obj=new SendHttpRequest();
        //obj.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tempactivity, menu);
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

    /*public void ShowDateTime(String str)
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        final String value=str;
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        sdate=year+ "-"+ (monthOfYear + 1)+"-" +dayOfMonth;
                        if(value=="Start")
                        {
                            start_date.setText(sdate);
                        }
                        else if(value=="End")
                        {
                            end_date.setText(sdate);
                        }

                        //ShowTime(value);
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void ShowTime(String str)
    {
        // Process to get Current Time
        final Calendar _time = Calendar.getInstance();
        mHour = _time.get(Calendar.HOUR_OF_DAY);
        mMinute = _time.get(Calendar.MINUTE);
        final String value=str;
        // Launch Time Picker Dialog
        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // Display Selected time in textbox

                        if(value=="Start")
                        {
                            sdate=start_date.getText().toString();
                            start_date.setText(sdate+" "+hourOfDay + ":" + minute);
                        }
                        else if(value=="End")
                        {
                            sdate=end_date.getText().toString();
                            end_date.setText(sdate+" "+hourOfDay + ":" + minute);
                        }


                    }
                }, mHour, mMinute, false);
        tpd.show();
    }*/




    public class SendHttpRequest extends Thread {
        @Override
        public void run() {
            Looper.prepare();
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
                HttpResponse response = httpClient.execute(httpPost);


                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String body = "";
                String _data = "";
                while ((body=rd.readLine())!=null){
                    _data=_data+"\n"+body;
                }
                Toast.makeText(getApplicationContext(), _data, Toast.LENGTH_LONG).show();

                Log.d("Http Response:",_data);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Looper.loop();
        }

    }
}
