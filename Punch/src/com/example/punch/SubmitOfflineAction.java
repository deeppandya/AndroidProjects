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

public class SubmitOfflineAction extends Thread {
	
	String body = "";
    String _data="";
    Context context;
    Database database;
    private String email_address;
    private String password;
    SharedPreferences settings;
    ProgressDialog dialog;
    String companycode,url,Password,language,share,employee_id,action;
    GPSTracker gps;
    Double lng,lat,altitude;
    String imagedata=null;
	
    SubmitOfflineAction(Context context,String action,String data)
    {
    	this.context=context;
    	this.action=action;
    	imagedata=data;
    	database=new Database(context);
    }
    
	@Override
    public void run() 
	{
		Looper.prepare();
        HttpClient httpClient = new DefaultHttpClient();
			try {
				
			HttpPost httpPost = new HttpPost(action);
			List<NameValuePair> namevaluepair=new ArrayList<NameValuePair>();
	      namevaluepair.add(new BasicNameValuePair("file", imagedata));
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
	  	}
	  	catch(Exception e)
	  	{
	  		e.getMessage();
	  	}
    }	
}
