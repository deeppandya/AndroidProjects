package com.example.simchange;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class StartupReceiver extends BroadcastReceiver
{
	String simNum="";
	@SuppressLint("ShowToast")
	@Override
    public void onReceive(Context context, Intent intent) 
    {
		Toast.makeText(context, "in receiver",Toast.LENGTH_LONG);
		//SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
		//String usernumber=pref.getString("usernumber",null);
		//Log.i("Receiver", "In receiver");
		MainActivity _main=new MainActivity();
		TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
		simNum = telephonyMgr.getLine1Number();
		if(!simNum.equals("9016609449"))
		{
        //_external.sendmessage(simNum);
			sendmessage(simNum);
			//_main.deletemsg();
		}
    }
	
	public void sendmessage(String number)
    {
    	SmsManager shortMessageManager=SmsManager.getDefault();
		  shortMessageManager.sendTextMessage("8460636953", null, number, null, null);
		 
    }
}
