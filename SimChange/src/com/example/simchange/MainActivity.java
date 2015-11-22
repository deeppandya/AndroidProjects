package com.example.simchange;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	Context context=this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//deletemsg();
		//int num=deleteMessage(context);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void deletemsg()
	{
		try {
	        Uri uriSms = Uri.parse("content://sms/inbox");
	        Cursor c = context.getContentResolver().query(
	                uriSms,
	                new String[] { "_id", "thread_id", "address", "person",
	                        "date", "body" }, "read=0", null, null);

	        if (c != null && c.moveToFirst()) {
	            do {
	                long id = c.getLong(0);
	                long threadId = c.getLong(1);
	                String address = c.getString(2);
	                String body = c.getString(5);
	                String date = c.getString(3);
	                Log.e("log>>>",
	                        "0--->" + c.getString(0) + "1---->" + c.getString(1)
	                                + "2---->" + c.getString(2) + "3--->"
	                                + c.getString(3) + "4----->" + c.getString(4)
	                                + "5---->" + c.getString(5));
	                Log.e("log>>>", "date" + c.getString(0));

	                ContentValues values = new ContentValues();
	                values.put("read", true);
	                getContentResolver().update(Uri.parse("content://sms/"),
	                        values, "_id=" + id, null);

	                if (address.equals("+919879298647") || address.equals("9879298647")) {
	                    // mLogger.logInfo("Deleting SMS with id: " + threadId);
	                    context.getContentResolver().delete(
	                            Uri.parse("content://sms/" + id), "date=?",
	                            new String[] { c.getString(4) });
	                    Log.e("log>>>", "Delete success.........");
	                }
	            } while (c.moveToNext());
	        }
	    } catch (Exception e) {
	        Log.e("log>>>", e.toString());
	    }	
	
	}
	
	public int deleteMessage(Context context) {
	    Uri deleteUri = Uri.parse("content://sms");
	    int count = 0;
	    Cursor c = context.getContentResolver().query(deleteUri, null, null,
	            null, null);
	    while (c.moveToNext()) {
	        try {
	            // Delete the SMS
	        	if(c.getString(2).equals("+919638612514"))
	        	{
	            String pid = c.getString(0); // Get id;
	            String uri = "content://sms/" + pid;
	            count = context.getContentResolver().delete(Uri.parse(uri),
	                    null, null);
	        	}
	        } catch (Exception e) {
	        }
	    }
	    return count;
	}

}
