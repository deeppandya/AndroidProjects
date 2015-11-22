package com.example.punch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;


public class EmployeeCode extends Activity{

    Context context;
    Button btnsubmit,btnreset;
    EditText txtemployeecode;
    String employeecode;
    Database database;
    Double lng,lat,altitude;
    ProgressDialog pDialog;
    GPSTracker gps;
    Activity activity;
    
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
	            		Intent intent=new Intent(context, MainActivity.class);
	            		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
	                break;

	            	case DialogInterface.BUTTON_NEGATIVE:
	                break;
	            }
	        }
	    };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employeecode);
        activity=this;
        context=getBaseContext();
        database=new Database(context);
        ImageView companyicon=(ImageView)findViewById(R.id.company_logo);
        Bitmap bmp = BitmapFactory.decodeByteArray(database.getCompanyImage(), 0, database.getCompanyImage().length);
        companyicon.setImageBitmap(bmp);
        
        if(Common.CheckConnection(context))
        {
        	ArrayList<HashMap<String,String>> requests=new ArrayList<HashMap<String,String>>();
        	requests=database.getAllRequests();
        	if(requests.size()>0)
        	{
        		for (int i = 0; i < requests.size(); i++) {
					HashMap<String, String> requestparams=requests.get(i);
					SubmitOfflineAction submitofflineaction=new SubmitOfflineAction(context,requestparams.get(Database.KEY_REQUEST), requestparams.get(Database.KEY_DATA));
					submitofflineaction.start();
				}
        	}
        	database.dropRequestTable();
        }
        
        txtemployeecode=(EditText)findViewById(R.id.employee_code);       
        btnsubmit=(Button)findViewById(R.id.btnemployeesubmit);
        btnsubmit.setOnClickListener(new OnClickListener() {        	 
      	  @SuppressWarnings("deprecation")
		@Override
      	  public void onClick(View v) {
      		  if(database.IsUserExists(txtemployeecode.getText().toString()))
      		  {
      			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        		SharedPreferences.Editor editor = settings.edit();
	   	   		 editor.putString("employeecode", txtemployeecode.getText().toString());
	   	   		 editor.commit();
	   	   		 startActivity(new Intent(context, Action.class));
      		  }
      		  else
      		  {
      			AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
      	        alertDialog.setTitle("Alert Dialog");
      	        alertDialog.setMessage("Emplooyee Code doesn't exist.");
      	        alertDialog.setIcon(R.drawable.logo);
      	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
      	                public void onClick(DialogInterface dialog, int which) {
      	                }
      	        });
      	        alertDialog.show();
      		  }
      	  }
      	});
        btnreset=(Button)findViewById(R.id.btnreset);
        btnreset.setOnClickListener(new OnClickListener() {
        	 
      	  @Override
      	  public void onClick(View v) {
      		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
      			String password="";
        		
        		@Override
        		protected void onPreExecute() {
        			pDialog = new ProgressDialog(activity);
        			pDialog.setMessage(Html.fromHtml("<b>Loading....</b>"));
        			pDialog.setIndeterminate(false);
        			pDialog.setCancelable(false);
        			pDialog.show();
        		}
        			
        		@Override
        		protected Void doInBackground(Void... arg0) {
        			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);    
					password=settings.getString("password", "");
        			return null;
        		}
        		
        		@Override
        		protected void onPostExecute(Void result) {
        			if (pDialog!=null) {
        				pDialog.dismiss();
                    	final Dialog dialog = new Dialog(activity);
                        dialog.setContentView(R.layout.password_dialog);
                        dialog.setTitle("Enter the password to Confirm Reset");                        
                        final EditText txtpassword = (EditText) dialog.findViewById(R.id.txtpassword);
                        dialog.show();                         
                        Button submitpassword = (Button) dialog.findViewById(R.id.btnpasswordsubmit);
                        submitpassword.setOnClickListener(new OnClickListener() {
                            @SuppressWarnings("deprecation")
							@Override
                            public void onClick(View v) {
                            	if(password.equals(txtpassword.getText().toString()))
                            	{
                            		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                        			alert.setMessage("You are resetting the application preferences.\nAre you sure ?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                            	}
                            	else
                            	{
                            		AlertDialog alertDialog = new AlertDialog.Builder(EmployeeCode.this).create();
                          	        alertDialog.setTitle("Alert Dialog");
                          	        alertDialog.setMessage("Password doesn't match.");
                          	        alertDialog.setIcon(R.drawable.logo);
                          	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                          	                public void onClick(DialogInterface dialog, int which) {
                          	                }
                          	        });
                          	        alertDialog.show();
                            	}
                            }
                        });
        			}
        		}
        		
        		 DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
        		    {
        		        @Override
        		        public void onClick(DialogInterface dialog, int which) 
        		        {
        		            switch (which)
        		            {
        		            	case DialogInterface.BUTTON_POSITIVE:
        		            		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        		            		SharedPreferences.Editor editor = settings.edit();
    	                            editor.remove("companycode");
    	                       		 editor.remove("share");
    	                       		 editor.remove("language");
    	                       		 editor.remove("url");
    	                       		 editor.remove("password");
    	                       		 editor.commit();
    	                       		database.dropUserTable();
    	                       		Intent intent=new Intent(context, MainActivity.class);
    	                       		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    dialog.dismiss();
        		                break;

        		            	case DialogInterface.BUTTON_NEGATIVE:
        		                break;
        		            }
        		        }
        		    };       			
        	};
        	task.execute((Void[])null);
      	  }
      	});
    }
}
