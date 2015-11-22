package com.example.weatherdisplay;

import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterUser extends Activity {

	Button btnRegister;
	EditText txtEmailId;
	EditText txtPassword;
	EditText txtConfirmPassword;
	EditText txtFirstName;
	EditText txtLastName;
	EditText txtAbbreviation;
	
	String email_id = "";
	String password = "";
	String current_password = "";
	String first_name = "";
	String last_name = "";
	String response = "";
	String new_user = "";
	String abbreviation = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);
		
		btnRegister = (Button) findViewById(R.id.btnRegister);		
		txtEmailId = (EditText) findViewById(R.id.txtUserID);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
		txtFirstName = (EditText) findViewById(R.id.txtFirstName);
		txtLastName = (EditText) findViewById(R.id.txtLastName);
		txtAbbreviation = (EditText) findViewById(R.id.txtAbbreviation);
		
		btnRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				email_id = txtEmailId.getText().toString().toUpperCase();
				password = txtPassword.getText().toString().toUpperCase();
				current_password = txtConfirmPassword.getText().toString().toUpperCase();
				first_name = txtFirstName.getText().toString().toUpperCase();
				last_name = txtLastName.getText().toString().toUpperCase();
				abbreviation = txtAbbreviation.getText().toString().toUpperCase();
				if(password.equals(current_password))
				{
					if(!email_id.equals("") && !password.equals("") && !first_name.equals("") && !last_name.equals(""))
					{
						try
						{
							//Call Webservice to Register user
							webServiceforregister ws=new webServiceforregister();
							ws.start();
							Intent listActivityIntent = new Intent(RegisterUser.this, ListViewActivity.class);
							startActivity(listActivityIntent);
						}
						catch (Exception exception)
						{
							//response=exception.toString();
						}
					}
					else
					{
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterUser.this);
						alertDialogBuilder.setTitle("Required fields are empty");
						alertDialogBuilder.setMessage("E-Mail,Password,First Name ,Last Name & Abbreviation are required fields...")
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
				else
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterUser.this);
					alertDialogBuilder.setTitle("Confirm Password Failed");
					alertDialogBuilder.setMessage("Both Passwords should match. Please try again!")
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_user, menu);
		return true;
	}
	
	public class webServiceforregister extends Thread
	{
		public void run()
		{
			Looper.prepare();
			//ExternalFunctionality _external=new ExternalFunctionality(context);
			CallSoap cs=new CallSoap();
			new_user = cs.RegisterUser(email_id, password, first_name, last_name, abbreviation);
			if(new_user.equals("true"))
			{
				Intent _LoginIntent = new Intent(RegisterUser.this, MainActivity.class);
				startActivity(_LoginIntent);
				Toast.makeText(RegisterUser.this, "User Successfully Registered. Please Login", Toast.LENGTH_SHORT).show();
			}
			else if (new_user.equals("false"))
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterUser.this);
				alertDialogBuilder.setTitle("User Already Exists");
				alertDialogBuilder.setMessage("Please Sign In.")
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
			else
			{				
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterUser.this);
						alertDialogBuilder.setTitle("Error Occured");
						alertDialogBuilder.setMessage("Please Try Again!")
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
		Looper.loop();
		}
	}

}
