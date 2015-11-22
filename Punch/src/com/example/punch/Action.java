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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.LabeledIntent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class Action extends Activity{

	Context context=this;
	Database database=new Database(context);
	Spinner spinner;
	String action,image;
	ImageView btnstartwork,btnfinishwork,btnstartbreak,btnfinishbreak,btnstartlunch,btnfinishlunch,btnstartextra,btnfinishextra;
	String share,language,employeecode,firstname;
	Switch switchprivate;
	TextView txtgreet;
	ArrayList<HashMap<String, String>> userlist=new ArrayList<HashMap<String, String>>();
	SharedPreferences settings;
	Editor editor;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.action);
	        settings= PreferenceManager.getDefaultSharedPreferences(context);
	        editor=settings.edit();
	        btnstartwork=(ImageView)findViewById(R.id.btnstartwork);
        	btnfinishwork=(ImageView)findViewById(R.id.btnfinishwork);
        	btnstartbreak=(ImageView)findViewById(R.id.btnstartbreak);
        	btnfinishbreak=(ImageView)findViewById(R.id.btnfinishbreak);
        	btnstartlunch=(ImageView)findViewById(R.id.btnstartlunch);
        	btnfinishlunch=(ImageView)findViewById(R.id.btnfinishlunch);
        	btnstartextra=(ImageView)findViewById(R.id.btnstartextra);
        	btnfinishextra=(ImageView)findViewById(R.id.btnfinishextra);
        	txtgreet=(TextView)findViewById(R.id.txtgreet);
        	switchprivate=(Switch)findViewById(R.id.switchprivate);
        	if(switchprivate.isChecked())
        	{
        		share="oui";
        	}
        	else if(switchprivate.isChecked())
        	{
        		share="non";
        	}
        	switchprivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub

                    if (buttonView.isChecked()) {
                    	share="oui";
                    	editor.putString("share", share);
                    	editor.commit();
                    }
                    else
                    {
                    	share="non";
                    	editor.putString("share", share);
                    	editor.commit();
                    }
                }
            });
        	
	        btnstartwork.setOnClickListener(onClickListener);
	        
	        btnfinishwork.setOnClickListener(onClickListener);
	        
	        btnstartbreak.setOnClickListener(onClickListener);
	        
	        btnfinishbreak.setOnClickListener(onClickListener);
	        
	        btnstartlunch.setOnClickListener(onClickListener);
	        
	        btnfinishlunch.setOnClickListener(onClickListener);
	        
	        btnstartextra.setOnClickListener(onClickListener);
	        
	        btnfinishextra.setOnClickListener(onClickListener);
	        language=settings.getString("language", "");
	        employeecode=settings.getString("employeecode", "");
	        
	        firstname=database.GetUserById(employeecode.trim()).get(Database.KEY_FIRSTNAME);
	        
	        if(language.equals("fr"))
	        {
	        	btnstartwork.setImageResource(R.drawable.frstartwork);
	         	btnfinishwork.setImageResource(R.drawable.frfinishwork);
	         	btnstartbreak.setImageResource(R.drawable.frstartbreak);
	         	btnfinishbreak.setImageResource(R.drawable.frfinishbreak);
	         	btnstartlunch.setImageResource(R.drawable.frstartlunch);
	         	btnfinishlunch.setImageResource(R.drawable.frfinishlunch);
	         	btnstartextra.setImageResource(R.drawable.frstartextra);
	         	btnfinishextra.setImageResource(R.drawable.frfinishextra);
	         	txtgreet.setText("Bonjour ,  "+firstname);
	         	switchprivate.setText("Part ?");
	        }
	        else if(language.equals("en"))
	        {
	        	btnstartwork.setImageResource(R.drawable.startwork);
	         	btnfinishwork.setImageResource(R.drawable.finishwork);
	         	btnstartbreak.setImageResource(R.drawable.havebreak);
	         	btnfinishbreak.setImageResource(R.drawable.finishbreak);
	         	btnstartlunch.setImageResource(R.drawable.startlunch);
	         	btnfinishlunch.setImageResource(R.drawable.finishlunch);
	         	btnstartextra.setImageResource(R.drawable.startextra);
	         	btnfinishextra.setImageResource(R.drawable.finishextra);
	         	txtgreet.setText("Hello , "+firstname);
	         	switchprivate.setText("Share ?");	         	
	        }
	    }
	 
	 private OnClickListener onClickListener = new OnClickListener() {
		    @Override
		    public void onClick(final View v) {
		    	ImageView imagebtn=(ImageView)findViewById(v.getId());
		    	imagebtn.setColorFilter(Color.argb(206, 216, 246, 255));
		        switch(v.getId()){
		            case R.id.btnstartwork:
		                action="Travail";
		            break;
		            case R.id.btnfinishwork:
		            	action="sortie";
		            break;
		            case R.id.btnstartbreak:
		            	action="Pause";
		            break;
		            case R.id.btnfinishbreak:
		            	action="endpause";
		            break;
		            case R.id.btnstartlunch:
		            	action="lunch";
		            break;
		            case R.id.btnfinishlunch:
		            	action="endlunch";
		            break;
		            case R.id.btnstartextra:
		            	action="Extra";
		            break;
		            case R.id.btnfinishextra:
		            	action="endextra";
		            break;
		        }
		        Intent intent=new Intent(Action.this,Middleware.class);
				intent.putExtra("action", action);
	            startActivityForResult(intent, 2);
		    }
		};
	 
	 /*private void loadSpinnerData()
	 {
		 userlist=database.getAllUsers();
		 List<String> lables=new ArrayList<String>();
		 for(int i=0;i<userlist.size();i++)
		 {
			 HashMap<String, String> map=userlist.get(i);
			 lables.add(map.get("user"));
		 }
		 ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, lables);
		 dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 spinner.setAdapter(dataAdapter);
	 }*/
		
	 @Override  
     protected void onActivityResult(int requestCode, int resultCode, Intent data)  
     {  
               super.onActivityResult(requestCode, resultCode, data);
               
                 if(requestCode==2)  
                       {
                	 	
                       }  
     }	 
}
