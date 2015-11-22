package com.example.contactlist;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;

import android.R.string;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	TextView call;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		call=(TextView)findViewById(R.id.textV);
		String str;
		try {
			str = getCallDetails();
			call.setText(str);
			call.setMovementMethod(ScrollingMovementMethod.getInstance());
			call.setTextColor(Color.BLACK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private String getCallDetails() throws IOException {

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
      	f.append("\n"+"Phone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration);
		str=str+"\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration;
		str=str+"\n----------------------------------";
		}
		managedCursor.close();
    	f.flush();
    	f.close();
		return str;
		//call.setText(sb);
		}

}
