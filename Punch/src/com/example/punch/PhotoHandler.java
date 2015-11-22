package com.example.punch;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.Deflater;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PhotoHandler implements PictureCallback {

  private final Context context;
  private static final String TAG = "CameraDemo";
  MainActivity main=new MainActivity();
  String share,language,companycode,xmldata;
  String employee_id,action,url,password;
  ProgressDialog pDialog;
  GPSTracker gps;
  Double lng,lat,altitude;
  File pictureFile;

  public PhotoHandler(Context context,String Action) {
    this.context = context;
    action=Action;
    gps=new GPSTracker(context);
  }

  @Override
  public void onPictureTaken(byte[] data, Camera camera) {
	  File pictureFileDir = Common.getDir();

	    if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
	      Toast.makeText(context, "Can't create directory to save image.",
	          Toast.LENGTH_LONG).show();
	      return;

	    }
	    String photoFile = "Picture_" + Common.GetCurrentDate() + ".jpg";

	    String filename = pictureFileDir.getPath() + File.separator + photoFile;

	    pictureFile = new File(filename);

	    try {
	      //FileOutputStream fos = new FileOutputStream(pictureFile);
	      //fos.write(data);
	      //fos.close();
	      Toast.makeText(context, "New Image saved:" + photoFile,
		          Toast.LENGTH_LONG).show();
	      /*if(gps.canGetLocation())
          {
          	lng=gps.getLongitude();
          	lat=gps.getLatitude();
          	altitude=gps.getAltitude();
          	SendAction action=new SendAction(data);
    	      action.execute();
          }
    		else
    		{
    			lng=0.0;
          	lat=0.0;
          	altitude=0.0;
          	gps.showSettingsAlert();
    		}*/
	      SubmitAction submitaction=new SubmitAction(context, action, data);
	      submitaction.start();
	    } catch (Exception error) {
	      Toast.makeText(context, "Image could not be saved.",
	          Toast.LENGTH_LONG).show();
	    }
	    camera.startPreview();
	    }

  
  
  
  /*public class SendAction extends AsyncTask<String, Integer, Integer> 
	{ 
	    private String email_address;
	    private String password;
	    String body = "";
	    byte[] imagedata=null;
	    
	    SendAction(byte[] data) 
	    {
	        imagedata=data;
	    }
	    
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage(Html.fromHtml("<b>Loading...</b>"));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
	    
		@SuppressWarnings("deprecation")
		protected Integer doInBackground(String... parameters)
		{
			Looper.prepare();
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);    
	        share=settings.getString("share", "");
	        language=settings.getString("language", "");
	        companycode=settings.getString("companycode", "");
	        employee_id=settings.getString("employeecode", "");
	        url=settings.getString("url", "");
	        password=settings.getString("password", "");
	        
	          HttpClient httpClient = new DefaultHttpClient();
	          try {
	              Drawable drawable = context.getResources().getDrawable(R.drawable.logo);
	              Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
	              ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
	              bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
	              byte[] bitmapdata = stream1.toByteArray();

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
	              xmldata=_data;
	              pDialog.dismiss();
	              Log.d("Http Response:", _data);
	          } catch (Exception e) {
	              e.printStackTrace();
	          }
			return 0;
		}
		
		@SuppressWarnings("deprecation")
		protected void onPostExecute(Integer result)
		{			
			try
		  	{
		  		Document doc = Jsoup.parse(xmldata);
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
		      		context.startActivity(new Intent(context, EmployeeCode.class));
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
	}*/

} 
