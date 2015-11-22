package com.example.punch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class SuccessActivity extends Activity{
	Activity activity;
	Context context;
	Database database;
	SharedPreferences settings;
    String companycode,url,Password,language,share,employee_id,action,firstname;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.successmsg);
	        activity=this;
	        context=getBaseContext();
	        settings = PreferenceManager.getDefaultSharedPreferences(context);
	    	database=new Database(context);
	        language=settings.getString("language", "");
	        employee_id=settings.getString("employeecode", "");
	        firstname=database.GetUserById(employee_id.trim()).get(Database.KEY_FIRSTNAME);
	        action=getIntent().getStringExtra("action");
	        String successmsg = null;
      		if(language.equals("fr"))
      		{
      			successmsg="Merci "+firstname+",\n\n Votre action ' "+action+" ' a été enregistré.";
      		}
      		else if(language.equals("en"))
      		{
      			successmsg="Thanks "+firstname+",\n\n Your action ' "+action+" ' has been saved.";
      		}
      		TextView txtmsg=(TextView)findViewById(R.id.successmsg);
      		txtmsg.setText(successmsg);
      		
      		Handler handler = new Handler();
      		handler.postDelayed(new Runnable() {
      		    public void run() {
      		    	startActivity(new Intent(context, EmployeeCode.class));
      		    }
      		}, 5000);
	    }
}
