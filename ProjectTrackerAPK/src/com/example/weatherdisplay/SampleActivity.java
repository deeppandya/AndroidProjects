package com.example.weatherdisplay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.example.weatherdisplay.R;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.widget.AdapterView.OnItemSelectedListener;

public class SampleActivity extends Activity {
	
	String position = "1";
	
	String weather = "";
	String temperature = "";
	String windSpeed = "";
	String iconfile = "";
	String project_type_response = "";
	String company_list_response = "";
	String client_list_response = "";
	Context _context = this;
	Boolean _isstart;
	//String startdate = "";
	//String starttime = "";
	Boolean _isnewproject;
	
	String project_name = "";
	String bug_id = "";
	String company_name = "";
	String client_name = "";
	String project_type = "";
	String start_date = "";
	String start_time = "";
	String description = "";
	String actions_taken = "";
	String contact_person = "";
	String isProjectAdded = "";
	String isProjectUpdated = "";
	String startdatetime = "";
	String enddatetime = "";
	String session_id ="";
	String spinner_selected_item = "";
	String status = "";
	String global_company_id = "";
	String global_client_id = "";
	String global_project_type_id = "";
	String selected_company_name = "";
	String selected_client_name = "";
	List<String> _project_type_list;
	List<String> _company_list;
	List<String> _client_list;
	List<HashMap<String,String>> _service_company_list;
	List<HashMap<String,String>> _service_client_list;
	List<HashMap<String,String>> _service_project_type_list;
	HashMap<String,String> data= null;
	
	//TextView tvcity;
	TextView tvtemp;
	TextView tvwindspeed;
	TextView tvstarttime;
	TextView tvendtime;
	TextView tvdescription;
	TextView tvActions;
	TextView tvUser;
	
	//TextView tvCondition;*/
	
	EditText tvBugID; //Bug ID
	EditText txtDescription;
	EditText txtActions;
	EditText txtUser;
	EditText txtCompany;
	EditText txtClient;
	
	Button btnStartDTpicker;
	Button btnEndDTpicker;
	Button btnSubmit;
	
	Spinner spinner1;
	Spinner spinner2Company;
	Spinner spinner3Clients;
	
	SharedPreferences pref;
	Editor editor;
	String current_user_id = "";
	
	//SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
	//Editor editor = pref.edit();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpagenew);
        addItemOnSpinner2CompanyList();
        addItemsOnSpinner1();
        
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        
		try {
			current_user_id = pref.getString("user_id", null);
			tvstarttime = (TextView) findViewById(R.id.txtStart);
	    	tvendtime = (TextView) findViewById(R.id.txtEnd);
			tvBugID = (EditText) findViewById(R.id.txtBUGID);
			btnStartDTpicker = (Button) findViewById(R.id.btnStartDateTimePicker);
			btnEndDTpicker = (Button) findViewById(R.id.btnEndDateTimePicker);
			btnSubmit = (Button) findViewById(R.id.btnSubmit);
			txtDescription = (EditText) findViewById(R.id.txtDescription);
			txtActions = (EditText) findViewById(R.id.txtAction);
			txtUser = (EditText) findViewById(R.id.txtUser);
			spinner1 = (Spinner) findViewById(R.id.spinner1);
			spinner2Company = (Spinner) findViewById(R.id.spinner2);
			spinner3Clients = (Spinner) findViewById(R.id.spinner3);
			//txtCompany = (EditText) findViewById(R.id.txtCompany);
			txtCompany = (EditText) findViewById(R.id.txtCompany);
			txtClient = (EditText) findViewById(R.id.txtClient);
			// Get position to display
	        Intent i = getIntent();
	        _isnewproject = i.getBooleanExtra("_isnewproject", false);
	        if(_isnewproject)
	        {
	        }
	        else
	        {
	        	//project_name = i.getStringExtra("project_name");
	        	company_name = i.getStringExtra("company_name");
	        	client_name = i.getStringExtra("client_name");
	        	bug_id = i.getStringExtra("bug_id");
	        	startdatetime = i.getStringExtra("start_date");
	        	enddatetime = i.getStringExtra("end_date");
	        	description = i.getStringExtra("description");
	        	actions_taken = i.getStringExtra("actions_taken");
	        	contact_person = i.getStringExtra("contact_person");
	        	session_id = i.getStringExtra("session_id");
	        	status = i.getStringExtra("status");
	        	
	        	
	        	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aa");
	        	SimpleDateFormat new_format = new SimpleDateFormat("MM/dd/yyyy,HH:mm");
	        	Date start_dt = new Date();
	        	Date end_dt = new Date();
	        	 try
	           	 {
	 	        	start_dt = format.parse(startdatetime);	 	        	
		        	end_dt = format.parse(enddatetime);
		        	startdatetime = new_format.format(start_dt);
		        	enddatetime = new_format.format(end_dt);
	           	 }
	           	 catch (ParseException e) 
	           	 {  
	           		    // TODO Auto-generated catch block  
	           		    e.printStackTrace();  
	           	 }


	        	//tvProjectName.setText(project_name);
	        	tvBugID.setText(bug_id);
	        	tvstarttime.setText(startdatetime);
	        	tvendtime.setText(enddatetime);
	        	txtDescription.setText(description);
	        	txtActions.setText(actions_taken);
	        	txtUser.setText(contact_person);
	        	txtCompany.setText(company_name);
	        	txtClient.setText(client_name);
	        	
	        	
	        	tvBugID.setEnabled(false);
	        	tvstarttime.setVisibility(tvstarttime.VISIBLE);
	        	tvendtime.setVisibility(tvendtime.VISIBLE);
	        	spinner2Company.setVisibility(spinner2Company.GONE);
	        	txtCompany.setVisibility(txtCompany.VISIBLE);
	        	spinner3Clients.setVisibility(spinner3Clients.GONE);
	        	txtClient.setVisibility(txtClient.VISIBLE);
	        	
	        }
		
		    btnStartDTpicker.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	            	 _isstart = true;
	                 // Perform action on click
	            	 Intent popUpIntent = new Intent(_context, DateTimePicker.class);
	            	 popUpIntent.putExtra("isstart", _isstart);
	            	 startActivityForResult(popUpIntent,123);
	            	 
	             }
	         });
		    
		    btnEndDTpicker.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	            	 _isstart = false;
	                 // Perform action on click
	            	 Intent popUpIntent = new Intent(_context, DateTimePicker.class);
	            	 popUpIntent.putExtra("isstart", _isstart);
	            	 startActivityForResult(popUpIntent,123);
	            	 
	             }
	         });
		    
		    spinner2Company.setOnItemSelectedListener(new OnItemSelectedListener() {
		        @Override
		        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		            // your code here
		        	String company_name = spinner2Company.getSelectedItem().toString();
		        	String company_id = "";
		        	String client_id = "";
		        	
		        	for (HashMap<String, String> company_list : _service_company_list)
		        		for(Entry<String, String> mapEntry: company_list.entrySet()) {
		        			if(mapEntry.getValue().equals(company_name))
		        			{
		        				global_company_id = mapEntry.getKey();
		        			}
		        	    }        	
		        	addItemOnSpinner3CompanyClients();
		        }

		        @Override
		        public void onNothingSelected(AdapterView<?> parentView) {
		            // your code here
		        }

		    });
		    
		    btnSubmit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					// TODO Auto-generated method stub
				bug_id = tvBugID.getText().toString().toUpperCase();
				project_type = spinner1.getSelectedItem().toString().toUpperCase();
				
				for (HashMap<String, String> project_type_list : _service_project_type_list)
	        		for(Entry<String, String> mapEntry: project_type_list.entrySet()) {
	        			if(mapEntry.getValue().equals(project_type))
	        			{
	        				global_project_type_id = mapEntry.getKey();
	        			}
	        	    }    
				
				 if(_isnewproject)
			     {
						selected_company_name = spinner2Company.getSelectedItem().toString().toUpperCase();
						
						for (HashMap<String, String> company_list : _service_company_list)
			        		for(Entry<String, String> mapEntry: company_list.entrySet()) {
			        			if(mapEntry.getValue().equals(selected_company_name))
			        			{
			        				global_company_id = mapEntry.getKey();
			        			}
			        	    } 
						
						selected_client_name = spinner3Clients.getSelectedItem().toString().toUpperCase();
						
						for (HashMap<String, String> client_list : _service_client_list)
			        		for(Entry<String, String> mapEntry: client_list.entrySet()) {
			        			if(mapEntry.getValue().equals(selected_client_name))
			        			{
			        				global_client_id = mapEntry.getKey();
			        			}
			        	    } 
			     }
				description = txtDescription.getText().toString().toUpperCase();
				actions_taken = txtActions.getText().toString().toUpperCase();
				contact_person = txtUser.getText().toString().toUpperCase();
				if(//!project_name.equals("") && 
						!bug_id.equals("") 
					&&  !description.equals("") && !actions_taken.equals("") 
					&& !contact_person.equals("") && !project_type.equals(" ") 
					&& !startdatetime.equals(""))
				{
					if(_isnewproject)
					{
						webserviceforAddProject ws = new webserviceforAddProject();
						ws.start();
						Intent spinnerintent=new Intent(_context,spinner.class);
						startActivity(spinnerintent);

					}
					else
					{
						if(enddatetime.equals(""))
						{
							if(status.contains("N"))
							{
								Toast.makeText(SampleActivity.this, "End Time has not been added for this project!", Toast.LENGTH_SHORT).show();
							}
						}
						webserviceforEditProject ws = new webserviceforEditProject();
						ws.start();
						Intent spinnerintent=new Intent(_context,spinner.class);
						startActivity(spinnerintent);
					}
				}
				else
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SampleActivity.this);
					alertDialogBuilder.setTitle("Required fields are empty");
					alertDialogBuilder.setMessage("All Fields are Compulsory.")
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
		
		catch (Exception ex) {
			Log.e("Error", "Loading exception");
		}
		
    }
    
    
    protected void addItemsOnSpinner1() {
  	  
  		spinner1 = (Spinner) findViewById(R.id.spinner1);
  		webserviceforGetProjectType ws = new webserviceforGetProjectType();
		ws.start();
		try {
			ws.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_project_type_list = new ArrayList<String>();
		_service_project_type_list =  new ArrayList<HashMap<String,String>>();
		data = new HashMap<String, String>();
		String project_type_id = "";
		String project_type_desc = "";
		if(project_type_response != null)
		{
			try {
				JSONArray finalResult = new JSONArray(new JSONTokener(project_type_response));
				for (int i = 0; i < finalResult.length(); i++) {
				    JSONObject message = finalResult.getJSONObject(i);
				    project_type_id = message.getString("PROJECT_TYPE_ID");
				    project_type_desc = message.getString("DESCRIPTION");				    
				    _project_type_list.add(project_type_desc);	
				    data.put(project_type_id, project_type_desc);
				    _service_project_type_list.add(data);
				}
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
  		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
  			android.R.layout.simple_spinner_item, _project_type_list);
  		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  		spinner1.setAdapter(dataAdapter);
  	  }
    
    protected void addItemOnSpinner2CompanyList()
    {
    	spinner2Company = (Spinner) findViewById(R.id.spinner2);
    	webserviceforCompanyList ws = new webserviceforCompanyList();
		ws.start();
		try {
			ws.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_company_list = new ArrayList<String>();
		_service_company_list =  new ArrayList<HashMap<String,String>>();
		data = new HashMap<String, String>();
    	String company_id = "";
    	String company_name = "";
    	if(company_list_response != null)
    	{
    		try
    		{
    			JSONArray finalResult = new JSONArray(new JSONTokener(company_list_response));
    			for (int i = 0; i < finalResult.length(); i++) {
    			JSONObject message = finalResult.getJSONObject(i);
    			company_id = message.getString("COMPANY_ID");
    			company_name = message.getString("NAME");
			    _company_list.add(company_name);
			    data.put(company_id, company_name);
			    _service_company_list.add(data);
    			}
    		}
    		catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
    	  		android.R.layout.simple_spinner_item, _company_list);
    	  		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	  		spinner2Company.setAdapter(dataAdapter);
    	}
    }
    
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
    {
    	String company_name = spinner2Company.getSelectedItem().toString();
    	String company_id = "";
    	String client_id = "";
    	

    	
    	if(company_name.equals("CMAS"))
    	{
    		global_company_id = "COMP";
    		addItemOnSpinner3CompanyClients();
    	}
    }
    
    protected void addItemOnSpinner3CompanyClients()
    {
    	spinner3Clients = (Spinner) findViewById(R.id.spinner3);
    	webserviceforClientList ws = new webserviceforClientList();
    	ws.start();
    	try {
			ws.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	_client_list = new ArrayList<String>();
    	_service_client_list = new ArrayList<HashMap<String,String>>();
		data = new HashMap<String, String>();
    	String client_id = "";
    	String client_name = "";
    	if(client_list_response !=null)
    	{
    		try
    		{
    			JSONArray finalResult = new JSONArray(new JSONTokener(client_list_response));
    			for (int i = 0; i < finalResult.length(); i++) {
    			JSONObject message = finalResult.getJSONObject(i);
    			client_id = message.getString("CLIENT_ID");
    			client_name = message.getString("CLIENT_NAME");
    			_client_list.add(client_name);
    			data.put(client_id, client_name);
			    _service_client_list.add(data);
    			}
    		}
    		catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
    	  		android.R.layout.simple_spinner_item, _client_list);
    	  		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	  		spinner3Clients.setAdapter(dataAdapter);
    	}
    }
	 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sampleactivitymenu, menu);
        SubMenu submenu = menu.addSubMenu(0, Menu.FIRST, Menu.NONE, "Sign Out");
        //submenu.add(0, 10, Menu.NONE, "LogOut");
        //inflater.inflate(R.menu.main, submenu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case Menu.FIRST:
        	Toast.makeText(SampleActivity.this, "You have successfully Signed Out!", Toast.LENGTH_SHORT).show();
        	RedirectToLogin();
        	finish();
        default:
            return super.onOptionsItemSelected(item);
    	}
    }
    
    public void RedirectToLogin()
    {
    	editor = pref.edit();
    	editor.clear();
    	editor.commit();
    	Intent _loginIntent = new Intent(SampleActivity.this, MainActivity.class);
    	_loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	_loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);     
    	startActivity(_loginIntent);
    	finish();
    }
  
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == RESULT_OK && requestCode == 123) {
        if (data.hasExtra("starttime") && data.hasExtra("startdate")) {
          //Toast.makeText(this, data.getExtras().getString("starttime"),
            //Toast.LENGTH_SHORT).show();
        	startdatetime = "";
        	startdatetime = data.getExtras().getString("startdate") +","+ data.getExtras().getString("starttime");
    		String _compareStartDT = tvstarttime.getText().toString();
    		String _compareEndDT = tvendtime.getText().toString();
    		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy,HH:mm");
    		
    		Date _startDT = new Date();
    		Date _endDT = new Date();
        	if(_isstart== true && startdatetime != null && !startdatetime.equals(""))
        	{    	
        		 try
	           	 {
        			_startDT = format.parse(startdatetime);
        			//_endDT = format.parse(enddatetime);
	           	 }
	           	 catch (ParseException e) 
	           	 {  
	           		    // TODO Auto-generated catch block  
	           		    e.printStackTrace();  
	           	 }
        		
        		 if (_startDT.getTime() > System.currentTimeMillis())
        		 {
        			 Toast.makeText(SampleActivity.this, "Time cannot be greater than System Time. Please Try Again!", Toast.LENGTH_SHORT).show();
        		 }
        		 else
        		 {
        			 tvstarttime.setVisibility(tvstarttime.VISIBLE);
             		 tvstarttime.setText(startdatetime);
        		 }
        		
        	}
        	if(_isstart== false && startdatetime != null && !startdatetime.equals("") )
        	{
        		enddatetime = startdatetime;

        		 try
	           	 {
        			_startDT = format.parse(_compareStartDT);
        			_endDT = format.parse(enddatetime);
	           	 }
	           	 catch (ParseException e) 
	           	 {  
	           		    // TODO Auto-generated catch block  
	           		    e.printStackTrace();  
	           	 }
        		 if(_startDT.getTime() > _endDT.getTime())
        		 {
        			 Toast.makeText(SampleActivity.this, "Start Date Cannot Be Greater Than End Date. Please Try Again!", Toast.LENGTH_SHORT).show();
        		 }
        		
        		 else if(_endDT.getTime() > System.currentTimeMillis())
        		 {
        			 Toast.makeText(SampleActivity.this, "Time cannot be greater than System Time. Please Try Again!", Toast.LENGTH_SHORT).show();
        		 }
        		 else
        		 {
             		tvendtime.setVisibility(tvendtime.VISIBLE);
            		tvendtime.setText(enddatetime);
        		 }
        	}
        }
      }
    }
    
    public class webserviceforAddProject extends Thread
    {
    	public void run()
		{
    		Looper.prepare();
			//ExternalFunctionality _external=new ExternalFunctionality(context);
			CallSoap cs=new CallSoap();
			isProjectAdded = cs.AddProject(global_company_id,global_client_id, bug_id, global_project_type_id, 
					tvstarttime.getText().toString(), tvendtime.getText().toString(), description, actions_taken,current_user_id, contact_person);
			
			if(isProjectAdded.equals("true"))
			{
				Toast.makeText(SampleActivity.this, "Project Successfully Added", Toast.LENGTH_SHORT).show();
				Intent listActivityIntent = new Intent(SampleActivity.this, ListViewActivity.class);
				startActivity(listActivityIntent);				
			}
			else
			{
				Intent listActivityIntent = new Intent(SampleActivity.this, ListViewActivity.class);
				startActivity(listActivityIntent);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_context);
				alertDialogBuilder.setTitle("Process Failed!");
				alertDialogBuilder.setMessage("An Error Occured. Please Try Again!")
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
    
    public class webserviceforEditProject extends Thread
    {
    	public void run()
		{
    		Looper.prepare();
    		CallSoap cs=new CallSoap();
    		isProjectUpdated = cs.EditProject(global_company_id, global_client_id, bug_id, global_project_type_id, 
    				startdatetime, enddatetime, description, actions_taken, contact_person, session_id, current_user_id);
    		if(isProjectUpdated.equals("true"))
    		{
    			Toast.makeText(SampleActivity.this, "Project Successfully Updated", Toast.LENGTH_SHORT).show();
				Intent listActivityIntent = new Intent(SampleActivity.this, ListViewActivity.class);
				startActivity(listActivityIntent);	
    		}
    		else
			{
				Intent listActivityIntent = new Intent(SampleActivity.this, ListViewActivity.class);
				startActivity(listActivityIntent);	
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_context);
				alertDialogBuilder.setTitle("Process Failed!");
				alertDialogBuilder.setMessage("An Error Occured. Please Try Again!")
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
    
    public class webserviceforGetProjectType extends Thread
    {
    	public void run()
		{
    		//Looper.prepare();
    		CallSoap cs=new CallSoap();
    		project_type_response = cs.GetProjectTypes();
    		//Looper.loop();
		}
    }
    
    public class webserviceforCompanyList extends Thread
    {
    	public void run()
		{
    		//Looper.prepare();
    		CallSoap cs=new CallSoap();
    		company_list_response = cs.GetCompany();
    		//Looper.loop();
		}
    }
    
    public class webserviceforClientList extends Thread
    {
    	public void run()
    	{
    		CallSoap cs=new CallSoap();
    		client_list_response = cs.GetCompanyClients(global_company_id);
    	}
    }
}
