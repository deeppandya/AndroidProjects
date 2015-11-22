package com.example.weatherdisplay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallActivity extends BroadcastReceiver {
	
    Context _context;
    public void onReceive(Context context, Intent intent) {
        
    try {
               // TELEPHONY MANAGER class object to register one listner
    	if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE)
                || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                        TelephonyManager.EXTRA_STATE_OFFHOOK)) 
    	{
                TelephonyManager tmgr = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                        _context=context;
                        //Toast toast = Toast.makeText(_context, "hello", Toast.LENGTH_LONG);
                        //toast.show();
                //Create Listner
                MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
                
                // Register listener for LISTEN_CALL_STATE
                tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    	}
        
        } catch (Exception e) {
            Log.e("Phone Receive Error", " " + e);
        }

    }

    private class MyPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
        
            Log.d("MyPhoneListener",state+"   incoming no:"+incomingNumber);
            //Toast toast2 = Toast.makeText(_context, incomingNumber, Toast.LENGTH_LONG);
            //toast2.show();
            //if (state == 1) {

                //String msg = "New Phone Call Event. Incomming Number : "+incomingNumber;
                //int duration = Toast.LENGTH_LONG;
                //Toast toast = Toast.makeText(_context, msg, duration);
                //toast.show();
                Intent intent = new Intent(_context, CallPopup.class);
                intent.putExtra("phno", incomingNumber);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		_context.startActivity(intent);

            //}
            //if(incomingNumber.startsWith("+91873"))
            //{
            	

            //}
        }
    }

}
