package com.example.weatherdisplay;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ShareActionProvider extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_action_provider);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.share_action, menu);
		return true;
	}

}
