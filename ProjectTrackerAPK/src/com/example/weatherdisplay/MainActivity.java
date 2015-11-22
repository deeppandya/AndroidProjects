package com.example.weatherdisplay;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
public class MainActivity extends Activity {

 Button btnLogin;
 Button btnRegister;
 
 String email_id = "";
 String password = "";
 String isUserAvailable = "";
 
 TextView lblErrorMessage;
 EditText txtEmailId;
 EditText txtPassword;
 SharedPreferences pref;
 String session_value = "";
 Editor editor;
 String isExit = "NO";
 
 
 Context _context = this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
     	// 0 - for private mode
    	pref = getApplicationContext().getSharedPreferences("MyPref", 0); 
    	session_value = pref.getString("user_id", null);
    	
    	//if(session_value.equals(null))
    	//{
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.activity_main);
		        btnLogin = (Button) findViewById(R.id.btnLogin);
		        btnRegister = (Button) findViewById(R.id.btnRegister);
		        
		        txtEmailId = (EditText) findViewById(R.id.txtUserID);
		        txtPassword = (EditText) findViewById(R.id.txtPassword);
		        
		        lblErrorMessage = (TextView) findViewById(R.id.lblErrorMessage);        
		        btnLogin.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
		
						
						
						email_id = txtEmailId.getText().toString().toUpperCase();
						password = txtPassword.getText().toString().toUpperCase();
						
						if(!email_id.equals("") && !password.equals(""))
						{
							editor = pref.edit();
							editor.putString("user_id", email_id);
							editor.putString("isExit", isExit);
							editor.commit(); 
							webServiceforcheckuser ws=new webServiceforcheckuser();
							ws.start();
							Intent spinnerintent=new Intent(_context,spinner.class);
							startActivity(spinnerintent);
						}
						else
						{
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_context);
							alertDialogBuilder.setTitle("Wrong Username OR Password");
							alertDialogBuilder.setMessage("Please Enter Correct Username And/Or Password!")
											  .setCancelable(false)
											  .setPositiveButton("Ok",new DialogInterface.OnClickListener() 
											  {
												  public void onClick(DialogInterface dialog,int id) 
												  {
											
												  }
											  });
									AlertDialog alertDialog = alertDialogBuilder.create();
									alertDialog.show();
						}
					}
				});
		        
		        btnRegister.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent _intentlistView = new Intent(_context,RegisterUser.class);
						startActivity(_intentlistView);
					}
				});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onBackPressed() {
    	moveTaskToBack(true);
    	isExit = "YES";
    	editor = pref.edit();
		editor.putString("isExit", isExit);
		editor.commit(); 
		
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case R.id.action_settings:
            //openSearch();
            return true;
        case 10:
            //Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
        	finish();
        	System.exit(0);
            return true;
        default:
            return super.onOptionsItemSelected(item);
    	}
    }
    
    public class webServiceforcheckuser extends Thread
	{
    	public void run()
		{
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		Looper.prepare();
			CallSoap cs=new CallSoap();
			isUserAvailable=cs.CheckUserLogin(email_id,password);
			if(isUserAvailable.equals("true"))
			{
				Intent intent=new Intent(_context,ListViewActivity.class);
				intent.putExtra("email_id", email_id);
				startActivity(intent);
			}
			else
			{
				Toast.makeText(_context, "Incorrect Username/Password. Please Try Again!", Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(_context,MainActivity.class);
				startActivity(intent);
				
			/*	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_context);
				alertDialogBuilder.setTitle("Wrong Username OR Password");
				alertDialogBuilder.setMessage("Check your inputs")
								  .setCancelable(false)
								  .setPositiveButton("Ok",new DialogInterface.OnClickListener() 
								  {
									  public void onClick(DialogInterface dialog,int id) 
									  {
								
									  }
								  });
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show(); */
			}
			Looper.loop();
		}
	}


}

