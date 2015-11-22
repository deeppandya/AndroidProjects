package com.example.punch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
import android.util.Log;

public class GetUsers extends Thread {
	
	String body = "";
    String _data="";
    Context context;
    Database data;
    Activity activity;
    SharedPreferences settings;
    String companycode,url,Password,language;
    boolean first;
    ProgressDialog dialog;
    GPSTracker gps;
    Double lng,lat,altitude;
	
    GetUsers(Context context,Activity activity,Boolean first)
    {
    	this.context=context;
    	this.activity=activity;
    	this.first=first;
    	settings = PreferenceManager.getDefaultSharedPreferences(context);
    	data=new Database(context);
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
	 	companycode=settings.getString("companycode", "");
	 	url=settings.getString("url", "");
	 	Password=settings.getString("password", "");
        language=settings.getString("language", "");
        dialog=new ProgressDialog(activity);
		dialog.setMessage("message");
		dialog.setCancelable(false);
		dialog.setInverseBackgroundForced(false);
		dialog.show();
        HttpClient httpClient = new DefaultHttpClient();
        //HttpGet httpPost = new HttpGet("http://207.34.244.247:88/erp/punch.nsf/(firstconnexion)?openagent&comp="+txtcompanycode.getText()+"&share=oui&time=2014-10-13T21:00:00-05:00&mac=%3Cadresse%20mac%3E&lat=latitude&lon=lontitude&alt=altitude&version=1.00&veri=1.00");
        HttpGet httpPost = new HttpGet("http://"+url+"/erp/punch.nsf/(getIDUsers)?openagent&comp="+companycode+"&password="+Password+"&time="+Common.GetCurrentDate()+"&mac="+Common.GetMACAddress(context)+"&lat="+lat+"&lon="+lng+"&alt="+altitude+"&version=1.00&veri=1.00");
        
        try {
            HttpResponse response = httpClient.execute(httpPost);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));            
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
        //Looper.loop();
	}
	
	@SuppressWarnings({ "deprecation", "deprecation" })
	public void xmldata(String html)
    {
		try
    	{
    		Document doc = Jsoup.parse(_data);
        	Elements elements = doc.select("body").first().children();

        	DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
        	 DocumentBuilder db = dbf.newDocumentBuilder();
        	 InputSource is = new InputSource();
        	 StringReader sr=new StringReader(elements.toString());
        	 is.setCharacterStream(sr);
        	 org.w3c.dom.Document xmldoc = db.parse(is);
        	 NodeList nodes = xmldoc.getElementsByTagName("user");
        	 data.dropUserTable();
        	 for (int temp = 0; temp < nodes.getLength(); temp++) {
        		 
        			Node nNode = nodes.item(temp);
        	 
        			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        	 
        				org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
        				String fname=eElement.getAttribute("firstname");
        				String lname=eElement.getAttribute("lastname");
        				data.addUser(eElement.getTextContent().toString().replaceAll("(\\r|\\n)", "").trim(),fname,lname);
        			}
        	 }
        	 dialog.hide();
        	 if(first)
             {
        		 data.dropRequestTable();
        		 Intent intent=new Intent(context, EmployeeCode.class);
        		 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 context.startActivity(intent);
             }
    	}
    	catch(Exception e)
    	{
    		e.getMessage();
    	}
    }	
}