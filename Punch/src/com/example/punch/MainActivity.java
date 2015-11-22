package com.example.punch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.SyncStateContract.Constants;
import android.text.Html;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{

	Camera mCamera;
    SurfaceView mPreview;
    static Context context;
    Database database;
    static final String KEY_USER = "user";
    ArrayList<HashMap<String, String>> userlist=new ArrayList<HashMap<String, String>>();
    Button btnsubmit;
    EditText txtcompanycode,txtpassword,txturl,txtcompanyimage;
    String share,language,xmldata,companycode,password,url;
    RadioButton rdyes,rdno,rdenglish,rdfrench;
    Double lng,lat,altitude;
    ProgressDialog dialog;
    GPSTracker gps;
    Date date = new Date();
    Activity activity;
    byte[] imagedata;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        activity=this;
        context=activity.getBaseContext();
        gps=new GPSTracker(context);
        
        Intent intent=getIntent();
        if(intent.getBooleanExtra("EXIT", false))
        {
        	finish();
        }
        else
        {
        	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);    
            companycode=settings.getString("companycode", "");
            password=settings.getString("password", "");
            language=settings.getString("language", "");
            url=settings.getString("url", "");
            
            if(companycode.equals("") || password.equals("") || url.equals("") || language.equals(""))
            {
            	txtcompanycode=(EditText)findViewById(R.id.company_code);
            	txtcompanycode.setText("DEMO");
                txtpassword=(EditText)findViewById(R.id.password);
                txtpassword.setText("!E*!");
                txturl=(EditText)findViewById(R.id.url);
                //txturl.setText("207.134.119.16:88");
                txturl.setText("granitedrc.no-ip.info:88");
                rdenglish=(RadioButton)findViewById(R.id.rdenglish);
                rdenglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // TODO Auto-generated method stub

                        if (buttonView.isChecked()) {
                        	language="en";
                        }
                    }
                });
                rdfrench=(RadioButton)findViewById(R.id.rdfrench);
                rdfrench.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // TODO Auto-generated method stub

                        if (buttonView.isChecked()) {
                        	language="fr";
                        }
                    }
                });
            	if(rdenglish.isChecked())
            	{
            		language="en";
            	}
            	else if(rdfrench.isChecked())
            	{
            		language="fr";
            	}
                database=new Database(context);
                txtcompanyimage=(EditText)findViewById(R.id.company_image);
                txtcompanyimage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					GetImageFromGallery();	
					}
				});
                btnsubmit=(Button)findViewById(R.id.btnsubmit);
                btnsubmit.setOnClickListener(new OnClickListener() {
                	 
              	  @Override
              	  public void onClick(View v) {
              		if(gps.canGetLocation())
                    {
                    	lng=gps.getLongitude();
                    	lat=gps.getLatitude();
                    	altitude=gps.getAltitude();
                    	SendHttpRequest _request=new SendHttpRequest();
                        _request.start();
                    }
              		else
              		{
              			lng=0.0;
                    	lat=0.0;
                    	altitude=0.0;
                    	gps.showSettingsAlert();
              		}
              	  }
               
              	});
            }
            else
            {
            	startActivity(new Intent(context, EmployeeCode.class));
            }
        }
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
	            switch (which)
	            {
	            	case DialogInterface.BUTTON_POSITIVE:
	            		finish();
	                break;
	
	            	case DialogInterface.BUTTON_NEGATIVE:
	                break;
	            }
	        }
	    };
	    
	    public void GetImageFromGallery()
	    {
	    	Intent i = new Intent(
	    	Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	    	startActivityForResult(i, 2);
	    }
	    
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	         
	        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = { MediaStore.Images.Media.DATA };
	    
	            Cursor cursor = getContentResolver().query(selectedImage,
	                    filePathColumn, null, null, null);
	            cursor.moveToFirst();
	    
	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String picturePath = cursor.getString(columnIndex);
	            cursor.close();
	            ByteBuffer buffer = ByteBuffer.allocate(BitmapFactory.decodeFile(picturePath).getByteCount()); //Create a new buffer
	            BitmapFactory.decodeFile(picturePath).copyPixelsToBuffer(buffer);
	            imagedata=buffer.array();
	            txtcompanyimage.setText(picturePath);
	            
	            // String picturePath contains the path of selected Image
	        }
	    }

    public class SendHttpRequest extends Thread
    {
    	@Override
        public void run() 
    	{
    		Looper.prepare();
    		if(Common.CheckConnection(context))
    		{

        		dialog=new ProgressDialog(activity);
        		dialog.setMessage("Loading...");
        		dialog.setCancelable(false);
        		dialog.setInverseBackgroundForced(false);
        		dialog.show();
    			HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpPost = new HttpGet("http://"+txturl.getText().toString()+"/erp/punch.nsf/(firstconnexion)?openagent&comp="+txtcompanycode.getText().toString()+"&password="+txtpassword.getText().toString()+"&time="+Common.GetCurrentDate()+"&mac="+Common.GetMACAddress(context)+"&lat="+lat+"&lon="+lng+"&alt="+altitude+"&version=1.00&veri=1.00");
                //HttpGet httpPost = new HttpGet("http://207.34.244.247:88/erp/punch.nsf/(getIDUsers)?openagent&comp="+txtcompanycode.getText()+"&share=oui&time=2014-10-13T21:00:00-05:00&mac=-ltadresse%20mac-gt&lat=latitude&lon=lontitude&alt=altitude&version=1.00&veri=1.00");
                
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String body = "";
                    String _data="";
                    while ((body = rd.readLine()) != null) 
                    {
                    _data=_data+"\n"+body;
                    }
                    xmldata(_data);
                    Log.d("Http Response:", _data);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();         
                }
    		}
    		else
    		{
    			Toast.makeText(context, "No Connection\n\nYou have to be connected for the first time", Toast.LENGTH_LONG).show();
    		}            
	        //Looper.loop();
    	}
    	
    	@SuppressWarnings({ "deprecation", "deprecation" })
    	public void xmldata(String html)
        {
        	try
        	{
        		Document doc = Jsoup.parse(html);
            	List<org.jsoup.nodes.Node> elements = doc.select("body").first().childNodes();
            	String data = "";
            	for (int i = 0; i < elements.size(); i++) {
    				data=data+elements.get(i).toString();
    			}
            	String str=data.replaceAll("[\t\n\r]", "");
            	str=str.trim();
            	boolean check=str.equals("0");
            	if(check)
            	{
            		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            		SharedPreferences.Editor editor = settings.edit();
            		 editor.putString("companycode", txtcompanycode.getText().toString());
            		 editor.putString("language", language);
            		 editor.putString("url", txturl.getText().toString());
            		 editor.putString("password", txtpassword.getText().toString());
            		 editor.commit();
            		 //GetallUsers users=new GetallUsers(context,activity,true);
                     //users.execute();
            		 database.dropCompanyImageTable();
     	             database.addCompanyImage(imagedata);
            		 GetUsers users=new GetUsers(context, activity, true);
            		 users.start();
            		 dialog.hide();
            	}
            	else
            	{
            		AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            		alertDialog.setTitle("Alert Dialog");
            		alertDialog.setMessage(data);
            		alertDialog.setIcon(R.drawable.logo);
            		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	
                    }
            });
            		alertDialog.show();
            	}
        	}
        	catch(Exception e)
        	{
        		e.getMessage();
        	}
        }
    	
    }
}
