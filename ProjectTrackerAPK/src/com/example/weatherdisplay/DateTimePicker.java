package com.example.weatherdisplay;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

public class DateTimePicker extends Activity {
	
	DatePicker datePicker;
	TimePicker timePicker;
	Button btnNext;
	Button btnSubmit;
	Context _context = this;
	int _day;
	int _month;
	int _year;
	String _startdate = "";
	String _starttime = "";
	Boolean _isstart;
	Date _currentdate;
	Date _currenttime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_time_picker);
		
		try
		{
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
			Editor editor = pref.edit();
			editor.clear();
			datePicker = (DatePicker) findViewById(R.id.datepicker);
			btnNext = (Button) findViewById(R.id.btnNext);
			timePicker = (TimePicker) findViewById(R.id.timepicker);
			btnSubmit = (Button) findViewById(R.id.btnSubmit);
			Calendar cal = Calendar.getInstance();
			_currenttime = cal.getTime();
			Intent i = getIntent();
			_isstart = Boolean.valueOf(i.getStringExtra("isstart"));
			btnNext.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 // Perform action on click
	            	 _day = datePicker.getDayOfMonth();
	            	 _month = datePicker.getMonth() + 1;
	            	 _year = datePicker.getYear();
	            	 
	            	 _startdate = checkDigit(_month) + "/" + checkDigit(_day) + "/" + checkDigit(_year) ;
	            	 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	            	 Date _selectedDate = new Date();
	            	 if(_startdate.equals("01/01/0001"))
	            	 {
	            		 _startdate = "";
	            	 }
	            	 else
	            	 {
		            	 try
		            	 {
		            		 _selectedDate = format.parse(_startdate);
		            	 }
		            	 catch (ParseException e) 
		            	 {  
		            		    // TODO Auto-generated catch block  
		            		    e.printStackTrace();  
		            	 }
	            		 if(new Date().before(_selectedDate))
	            		 {
	            			 Toast.makeText(DateTimePicker.this, "Future Date Cannot Be Selected.", Toast.LENGTH_SHORT).show();
	            		 }
	            		 else
	            		 {
			            	 //SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			            	 //String formatedDate = sdf.format(new Date(_year, _month, _day));
			            	 timePicker.setVisibility(timePicker.VISIBLE);
			            	 datePicker.setVisibility(datePicker.GONE);
			            	 btnNext.setVisibility(btnNext.GONE);
			            	 btnSubmit.setVisibility(btnSubmit.VISIBLE);
	            		 }
	            	 }
	             }
	         });
			
			btnSubmit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					_starttime = checkDigit(timePicker.getCurrentHour()) + ":" + checkDigit(timePicker.getCurrentMinute());
					Intent data = new Intent();
					data.putExtra("starttime", _starttime);
					data.putExtra("startdate", _startdate);
					setResult(RESULT_OK, data);
					finish();
				}
			});
			
			
			
		}
		
		catch (Exception ex) {
			Log.e("Error", "Loading exception");
		}
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                       if (year > _year)
                       view.updateDate(_year, _month, _day);

                       if (monthOfYear > _month && year == _month)
                       view.updateDate(_year, _month, _day);

                       if (dayOfMonth > _day && year == _year && monthOfYear == _month)
                       view.updateDate(_year, _month, _day);
            }
    };

	@Override
    protected Dialog onCreateDialog(int id) {
                    DatePickerDialog _date =  new DatePickerDialog(this,  mDateSetListener,
                    		_year, _month, _day){
               @Override
               public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
               {   
                    if (year > _year)
                       view.updateDate(_year, _month, _day);

                       if (monthOfYear > _month && year == _year)
                       view.updateDate(_year, _month, _day);

                       if (dayOfMonth > _day && year == _year && monthOfYear == _day)
                       view.updateDate(_year, _day, _day);

               }
           };
           return _date;

    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.date_time_picker, menu);
		return true;
	}
	
	public String checkDigit(int number)
    {
        return number<=9?"0"+number:String.valueOf(number);
    }
	

}
