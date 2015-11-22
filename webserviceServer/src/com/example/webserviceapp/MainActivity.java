package com.example.webserviceapp;

import org.kobjects.util.Csv;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainthread _main=new mainthread();
		_main.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public class mainthread extends Thread
	{
		public void run()
		{
			CallSoap cs=new CallSoap();
			String str=cs.Call("sbhfgh","parth","007");
		}
	}

}
