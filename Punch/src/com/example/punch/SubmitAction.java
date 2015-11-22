package com.example.punch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class SubmitAction extends Thread {
	
	String body = "";
    String _data="";
    Context context;
    Database database;
    private String email_address;
    private String password;
    SharedPreferences settings;
    ProgressDialog dialog;
    String companycode,url,Password,language,share,employee_id,action,firstname;
    boolean first;
    GPSTracker gps;
    Double lng,lat,altitude;
    byte[] imagedata=null;
	
    SubmitAction(Context context,String action,byte[] data)
    {
    	this.context=context;
    	this.action=action;
    	imagedata=data;
    	settings = PreferenceManager.getDefaultSharedPreferences(context);
    	database=new Database(context);
    	gps=new GPSTracker(context);
    	if(gps.canGetLocation())
        {
        	lng=gps.getLongitude();
        	lat=gps.getLatitude();
        	altitude=gps.getAltitude();
        }
  		else
  		{
  			lng=0.0;
        	lat=0.0;
        	altitude=0.0;
        	gps.showSettingsAlert();
  		}
    }
    
	@Override
    public void run() 
	{
		Looper.prepare();		
		share=settings.getString("share", "");
        language=settings.getString("language", "");
        companycode=settings.getString("companycode", "");
        employee_id=settings.getString("employeecode", "");
        url=settings.getString("url", "");
        password=settings.getString("password", "");
        //firstname=database.GetUserById(employee_id.trim()).get(Database.KEY_FIRSTNAME);
        HttpClient httpClient = new DefaultHttpClient();
		if(Common.CheckConnection(context))
		{
			try {
	            /*Drawable drawable = context.getResources().getDrawable(R.drawable.logo);
	            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
	            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
	            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
	            byte[] bitmapdata = stream1.toByteArray();*/
				dialog=new ProgressDialog(context);
				dialog.setMessage("Loading...");
				dialog.setCancelable(false);
				dialog.setInverseBackgroundForced(false);
				dialog.show();
	      String encodedString = Base64.encodeToString(imagedata, 0);
			HttpPost httpPost = new HttpPost("http://"+url+"/erp/punch.nsf/(punch)?openagent&version=1.00&flux=1.00&comp="+companycode+"&password="+password+"&id="+employee_id.replaceAll("[\t\n\r]", "").trim()+"&share="+share+"&mac="+Common.GetMACAddress(context)+"&lat="+lat+"&lon="+lng+"&alt="+altitude+"&time="+Common.GetCurrentDate()+"&action="+action+"&lang="+language);
			List<NameValuePair> namevaluepair=new ArrayList<NameValuePair>();
	      namevaluepair.add(new BasicNameValuePair("file", encodedString));
	      httpPost.setEntity(new UrlEncodedFormEntity(namevaluepair));
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
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		else
		{
			try {
			String encodedString = Base64.encodeToString(imagedata, 0);
			String request="http://"+url+"/erp/punch.nsf/(punch)?openagent&version=1.00&flux=1.00&comp="+companycode+"&password="+password+"&id="+employee_id.replaceAll("[\t\n\r]", "").trim()+"&share="+share+"&mac="+Common.GetMACAddress(context)+"&lat="+lat+"&lon="+lng+"&alt="+altitude+"&time="+Common.GetCurrentDate()+"&action="+action+"&lang="+language;
			database.addRequest(request, encodedString);
			context.startActivity(new Intent(context, EmployeeCode.class));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
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
	      	dialog.hide();
	      	if(check)
	      	{
	      		
	      		/*AlertDialog alertDialog = new AlertDialog.Builder(context).create();
      	        alertDialog.setTitle("Success");
      	        alertDialog.setMessage("");
      	        alertDialog.setIcon(R.drawable.logo);
      	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
      	                public void onClick(DialogInterface dialog, int which) {
      	                	
      	                }
      	        });
      	        alertDialog.show();*/
	      		//Toast.makeText(context, successmsg, Toast.LENGTH_LONG).show();
	      		Intent intent=new Intent(context, SuccessActivity.class);
	      		intent.putExtra("action", action);
	      		context.startActivity(intent);
	      		//context.startActivity(new Intent(context, EmployeeCode.class));
	      	}
	      	else
	      	{
	      		AlertDialog alertDialog = new AlertDialog.Builder(
	                      context).create();
	      		alertDialog.setTitle("Alert Dialog");
	      		alertDialog.setMessage(data);
	      		alertDialog.setIcon(R.drawable.logo);
	      		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	              public void onClick(DialogInterface dialog, int which) {
	            	  context.startActivity(new Intent(context, Action.class));
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
