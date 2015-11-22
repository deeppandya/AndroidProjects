package com.example.weatherdisplay;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class spinner extends Activity
{
	ProgressBar progress;
	TextView txtmsg;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
		String _check=pref.getString("back", null);
		//if(_check!=null && _check.equals("true"))
	//	{
		//	Intent intent = new Intent(this, MainPage.class);
			//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//intent.putExtra("EXIT", true);
			//startActivity(intent);
		//}
		//else
		//{
		setContentView(R.layout.spinner);
		progress=(ProgressBar)findViewById(R.id.progressBar);
		txtmsg=(TextView)findViewById(R.id.textView1);
		//txtmsg.setTextColor("#3B5998");
		//}
	}
	@Override
	public void onBackPressed() {

	}
}
