package com.example.weatherdisplay;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.example.weatherdisplay.BinderData.ViewHolder;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

public class ListViewActivity extends Activity {
	 static final String KEY_TAG = "weatherdata"; // parent node
	    static final String KEY_ID = "id";
	    static final String KEY_CITY = "city";
	    static final String KEY_TEMP_C = "tempc";
	    static final String KEY_TEMP_F = "tempf";
	    static final String KEY_CONDN = "condition";
	    static final String KEY_SPEED = "windspeed";
	    static final String KEY_ICON = "icon";
	    
	    ListView list;
	    TextView tvProjectName;
	    
	    Context _context = this;
		
	    String response = "";
	    String current_email_id = "";
	    private static Looper threadLooper = null;
	    // List items 
	    
	    BinderData adapter = null;
	    List<HashMap<String,String>> weatherDataCollection;
		SharedPreferences pref;
		Editor editor;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);
		if(pref.getString("isExit", "NO").equals("YES"))
		{
			finish();
		}
		
		else
		{
			try {
				//Intent mainActivityIntent = getIntent();
				current_email_id = pref.getString("user_id", null);
				webserviceforGetProjects ws = new webserviceforGetProjects();
				ws.start();
				try {
					ws.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				weatherDataCollection = new ArrayList<HashMap<String,String>>();
				
				String project_name ="";
				String company_name = "";
				String client_name ="";
				String bug_id = "";
				String start_dt = "";
				String end_dt = "";
				String description = "";
				String actions_taken = "";
				String contact_person = "";
				String session_id = "";
				String status = "";
				HashMap<String,String> data= null;
				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
	        	SimpleDateFormat new_format = new SimpleDateFormat("MM/dd/yyyy,HH:mm");

				if(!response.contains("[]"))
				{
					try {
						JSONArray finalResult = new JSONArray(new JSONTokener(response));
						for (int i = 0; i < finalResult.length(); i++) {
							data = new HashMap<String,String>(); 
				        	Date startDt = new Date();
				        	Date endDt = new Date();
						    JSONObject message = finalResult.getJSONObject(i);
						    company_name = message.getString("NAME");
						    client_name = message.getString("CLIENT_NAME");
						    //project_name = message.getString("PROJECT_NAME");
						    bug_id = message.getString("BUG_ID");
						    start_dt = message.getString("START");
						    format.setLenient(false);
						    end_dt = message.getString("END");
						    try
				           	 {
						    	startDt = format.parse(start_dt);	 	
						    	start_dt = new_format.format(startDt);
						    	endDt = format.parse(end_dt);						    	
						    	end_dt = new_format.format(endDt);
				           	 }
				           	 catch (ParseException e) 
				           	 {  
				           		    // TODO Auto-generated catch block  
				           		    e.printStackTrace();  
				           	 }
						    description = message.getString("BUG_DESC");
						    actions_taken = message.getString("ACTION_TAKEN");
						    contact_person = message.getString("CONTACT_PERSON");
						    session_id = message.getString("SESSION_ID");
						    status = message.getString("STATUS");
						    data.put("NAME", company_name);
						    data.put("CLIENT_NAME", client_name);
						    data.put("BUG_ID", bug_id);
						    data.put("START", start_dt);
						    data.put("END", end_dt);
						    data.put("BUG_DESC", description);
						    data.put("ACTION_TAKEN", actions_taken);
						    data.put("CONTACT_PERSON", contact_person);
						    data.put("SESSION_ID", session_id);
						    data.put("STATUS", status);
						    weatherDataCollection.add(data);
						    //tvProjectName.setText(project_name);
						    
						    }
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_context);
					alertDialogBuilder.setTitle("No Project Issues Found");
					alertDialogBuilder.setMessage("Please Add an Issue to track!")
									  .setCancelable(false)
									  .setPositiveButton("Ok",new DialogInterface.OnClickListener() 
									  {
										  public void onClick(DialogInterface dialog,int id) 
										  {
											  AddNewProject();
										  }
									  });
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show(); 
				}
				
		        
		        list = (ListView) findViewById(R.id.list);
		        BinderData bindingData = new BinderData(this,weatherDataCollection);
				list.setAdapter(bindingData);
				list.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						
						
						Intent i = new Intent();
						i.setClass(ListViewActivity.this, SampleActivity.class);

						// parameters
						i.putExtra("_isnewproject", false);
						i.putExtra("position", String.valueOf(position + 1));					
						i.putExtra("project_name", weatherDataCollection.get(position).get("PROJECT_NAME"));
						i.putExtra("company_name", weatherDataCollection.get(position).get("NAME"));
						i.putExtra("client_name", weatherDataCollection.get(position).get("CLIENT_NAME"));
						i.putExtra("bug_id", weatherDataCollection.get(position).get("BUG_ID"));
						i.putExtra("start_date", weatherDataCollection.get(position).get("START"));
						i.putExtra("end_date", weatherDataCollection.get(position).get("END"));
						i.putExtra("description", weatherDataCollection.get(position).get("BUG_DESC"));
						i.putExtra("actions_taken", weatherDataCollection.get(position).get("ACTION_TAKEN"));
						i.putExtra("contact_person", weatherDataCollection.get(position).get("CONTACT_PERSON"));
						i.putExtra("session_id", weatherDataCollection.get(position).get("SESSION_ID"));
						i.putExtra("status", weatherDataCollection.get(position).get("STATUS"));
						// start the sample activity
						startActivity(i);
					}
				});

			}
			
			catch (Exception ex) {
				Log.e("Error", "Loading exception");
			}
		}

	}

	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.list_view, menu);
	        SubMenu submenu = menu.addSubMenu(0, 1, Menu.NONE, "Sign Out");
	        menu.addSubMenu(0, 2, Menu.FIRST, "Add Bug Details");
	        return true;
	    }
	 
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	switch (item.getItemId()) {
	        case 1:
	            Toast.makeText(ListViewActivity.this, "You have successfully Signed Out!", Toast.LENGTH_SHORT).show();
	            RedirectToLogin();
	            return true;
	        case 2:
	            AddNewProject();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    	}
	    }
	 
	 @Override
	 public void onBackPressed() {
	 }
	 
	 	public void AddNewProject()
	 	{
	 		Intent _sampleActivityIntent = new Intent(ListViewActivity.this, SampleActivity.class);
	 		_sampleActivityIntent.putExtra("_isnewproject", true);
	 		startActivity(_sampleActivityIntent);
	 	}
	 	
	    public void RedirectToLogin()
	    {
	    	
	    	pref = getApplicationContext().getSharedPreferences("MyPref", 0);
	    	editor = pref.edit();
	    	editor.clear();
	    	editor.commit();
	    	Intent _loginIntent = new Intent(ListViewActivity.this, MainActivity.class);
	    	_loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	_loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);       
	    	startActivity(_loginIntent);
	    	finish();
	    }

	    public class webserviceforGetProjects extends Thread
	    {
	    	public void run()
			{
	    		//Looper.prepare();
				CallSoap cs=new CallSoap();
				response=cs.GetProjects(current_email_id);
				//Looper.loop();
			}
	    }
}
