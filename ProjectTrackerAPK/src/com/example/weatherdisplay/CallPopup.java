package com.example.weatherdisplay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CallPopup extends Activity
{
	Button btnyes,btnno;
	Context _context=this;
	//SharedPreferences pref;
	 //Editor editor;
	 //String isExit = "NO";
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup);
        
        //pref = getApplicationContext().getSharedPreferences("MyPref", 0); 
        String phno=getIntent().getStringExtra("phno");
        TextView txtmsg=(TextView)findViewById(R.id.textView1);
        txtmsg.setText(txtmsg.getText()+phno);
        btnyes=(Button)findViewById(R.id.btnyes);
        btnyes.setOnClickListener(new OnClickListener() 
        {        
            public void onClick(View v) 
            {
            	//isExit = "NO";
            	//editor = pref.edit();
        		//editor.putString("isExit", isExit);
        		//editor.commit();
            	
            	
            	//CallAdd _call=new CallAdd();
            	//_call.start();
            	//Intent intent = new Intent(_context, spinner.class);
        		//_context.startActivity(intent);
            	
            	Intent intent = new Intent(_context, SampleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		_context.startActivity(intent);
        		finish();
            	
            }
        });
        
        btnno=(Button)findViewById(R.id.btnno);
        btnno.setOnClickListener(new OnClickListener() 
        {        
            public void onClick(View v) 
            {
            	finish();	
            }
        });
        
	}
	public class CallAdd extends Thread
	{
		@SuppressLint("DefaultLocale")
		public void run()
		{
			Intent intent = new Intent(_context, SampleActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		_context.startActivity(intent);
		}
		
	}
}
