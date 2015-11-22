package com.tobesafe.tobesafe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class Database extends SQLiteOpenHelper
{
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "package";

		
		//PLACES TABLE-COLUMN NAMES
		private static final String PLACE_ID = "place_id";
				public static final String PLACE_NAME = "place_name";
				public static final String REFERENCE= "reference";
				public static final String PLACE_ICON = "icon";
				public static final String VICINITY= "vicinity";
				public static final String ADDRESS = "address";
				public static final String PHONE_NUMBER= "phone_number";
				public static final String GEOMETRY= "geometry";
				public static final String PLACE_LONGITUDE= "lng";
				public static final String PLACE_LATITUDE= "lat";
				
				public static String KEY_REFERENCE = "reference"; // id of the place
				public static String KEY_NAME = "name"; // name of the place
				public static String KEY_VICINITY = "vicinity"; // Place area name
				public static String KEY_ICON = "icon"; // Place area name
	
	//PLACES TABLE CREATE STATEMENT
	private static final String CREATE_TABLE_PLACES = "CREATE TABLE " + TABLE_PLACES + "(" + ID + " INTEGER PRIMARY KEY NOT NULL, " + PLACE_ID + " VARCHAR(100), " + PLACE_NAME + " VARCHAR(100), " + PLACE_ICON + " VARCHAR(100)," + REFERENCE + " VARCHAR(100),"+ VICINITY + " VARCHAR(100), " + ADDRESS + " VARCHAR(100), " + PHONE_NUMBER + " VARCHAR(12),"+ GEOMETRY + " VARCHAR(12),"+ PLACE_LONGITUDE + " VARCHAR(12),"+ PLACE_LATITUDE + " VARCHAR(12))";
	
	SQLiteDatabase db = this.getWritableDatabase();
	
	public Database(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		
		onCreate(db);
	}
	
	public void dropTables()
	{
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
		
	}
	
	public void createTables()
	{
		
		try{
			if(!tableExists(TABLE_PLACES))
			{
				db.execSQL(CREATE_TABLE_PLACES);
			}
		}
		catch(Exception ex)
		{
			Log.i("Createtable", ex.getMessage());
		}
		
	}	
	
	
	public long createPlace(HashMap<String, String> Places) 
	{
		/*SQLiteDatabase db = this.getWritableDatabase();
		
		String sql = "INSERT INTO "+TABLE_PLACES;
		sql += "("+PLACE_ID+","+PLACE_NAME+","+PLACE_ICON+","+REFERENCE+","+VICINITY+","+ADDRESS+","+PHONE_NUMBER+")";
		sql += " VALUES ";
		sql += "('" + Places.get(PLACE_ID)  + "','" + Places.get(PLACE_NAME)  + "', '" + Places.get(PLACE_ICON)  + "', '" + Places.get(REFERENCE)  + "', '" + Places.get(VICINITY)  + "', '" + Places.get(ADDRESS)  + "', '" + Places.get(PHONE_NUMBER)  +"')";
		
		
		try 
		{
			db.execSQL(sql);
		} 
		catch (SQLException e) 
		{
			return e.getMessage().toString().trim();
		}

		return sql;*/
		long placeCreated=0;
		//SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		try
		{
			values.put(PLACE_ID, Places.get(PLACE_ID));
			values.put(PLACE_NAME, Places.get(PLACE_NAME));
			values.put(PLACE_ICON, Places.get(PLACE_ICON));
			values.put(REFERENCE, Places.get(REFERENCE));
			values.put(VICINITY, Places.get(VICINITY));
			values.put(ADDRESS, Places.get(ADDRESS));
			values.put(PHONE_NUMBER, Places.get(PHONE_NUMBER));
			values.put(GEOMETRY,Places.get(GEOMETRY));
			values.put(PLACE_LONGITUDE,Places.get(PLACE_LONGITUDE));
			values.put(PLACE_LATITUDE,Places.get(PLACE_LATITUDE));
			
			placeCreated= db.insert(TABLE_PLACES, null, values);
		}
		catch(Exception ex)
		{
			ex.getMessage();
		}
		return placeCreated;
	}
	
	public ArrayList<HashMap<String, String>> getplaces()
	{
		ArrayList<HashMap<String, String>> places = new ArrayList<HashMap<String,String>>();
		//HashMap<String, String> map = new HashMap<String, String>();
		//SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_PLACES;

		try
		{
			Cursor cursor = db.rawQuery(selectQuery, null);
			
			if (cursor.moveToFirst()) 
			{
				
				do 
				{ 
					//cursor.moveToFirst();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(PLACE_ID, cursor.getString(cursor.getColumnIndex(PLACE_ID)));
					map.put(PLACE_NAME, cursor.getString(cursor.getColumnIndex(PLACE_NAME)));
					map.put(REFERENCE, cursor.getString(cursor.getColumnIndex(REFERENCE)));
					map.put(PLACE_ICON, cursor.getString(cursor.getColumnIndex(PLACE_ICON)));
					map.put(VICINITY, cursor.getString(cursor.getColumnIndex(VICINITY)));
					map.put(ADDRESS, cursor.getString(cursor.getColumnIndex(ADDRESS)));
					map.put(PHONE_NUMBER,cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
					map.put(GEOMETRY,cursor.getString(cursor.getColumnIndex(GEOMETRY)));
					map.put(PLACE_LONGITUDE,cursor.getString(cursor.getColumnIndex(PLACE_LONGITUDE)));
					map.put(PLACE_LATITUDE,cursor.getString(cursor.getColumnIndex(PLACE_LATITUDE)));
					places.add(map);
				} 
				while (cursor.moveToNext());
				
			}
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return places;
	}
	
	public void deletePlace(String place_id) 
	{
		//SQLiteDatabase db = this.getWritableDatabase();
		
		String sql = "DELETE FROM "+TABLE_PLACES+" WHERE place_id='" + place_id + "'";
		db.execSQL(sql);	
	}
	
	
	public boolean tableExists(String tableName)
	{
		//SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name ='" + tableName + "'", null);
		
		if (cursor!=null) 
		{
			if (cursor.getCount() > 0) 
			{
				cursor.close();
				return true;
			}
			else
			{
				cursor.close();
				return false;
			}			
		}
		else
		{
			return false;
		}
	}
	
}
