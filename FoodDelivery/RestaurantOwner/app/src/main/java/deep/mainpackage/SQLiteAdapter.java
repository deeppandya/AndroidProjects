package deep.mainpackage;

/**
 * Created by Deep on 24/10/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteAdapter {

    public static final String DATABASE_NAME = "U-Track";
    public static final String RESTAURANTOWNERTABLE = "restaurantowner";
    public static final String DELIVERYCOMPANYTABLE = "deliverycompany";
    public static final String DELIVERYREQUESTSTABLE = "deliveryrequests";
    public static final int DATABASE_VERSION = 1;

    /*
    RESTAURANT OWNER TABLE COLUMNS
     */
    public static final String ID="_id";
    public static final String USERNAME="uname";
    public static final String PASSWORD="pwd";
    public static final String RESTAURANTNAME="restaurant_name";
    public static final String RESTAURANTADDRESS="address";
    public static final String CONTACTNAME="contact_name";
    public static final String CONTACTNUMBER="contact_number";
    public static final String RESTAURANTID="restaurant_id";

    /*
    DELIVERY COMPANY TABLE COLUMNS
     */
    public static final String REQUESTID="request_id";
    public static final String DELIVERYCOMPANYID="deliverycompany_id";
    public static final String DELIVERYADDRESS="delivery_address";
    public static final String DRIVERID="driver_id";
    public static final String STATUS="status";

    /*
    DELIVERY REQUESTS TABLE COLUMNS
     */
    public static final String COMPANYNAME="company_name";
    public static final String COMPANYID="company_id";
    public static final String PREDATE="predate";
    public static final String ISPRE="isdate";

    //CREATE RESTAURANT OWNER TABLE
    private static final String CREATE_RESTAURANT_OWNER_TABLE =
            "create table " + RESTAURANTOWNERTABLE + " ("
                    + ID + " integer primary key autoincrement, "
                    + USERNAME + " VARCHAR(10) not null,"
                    + PASSWORD +" VARCHAR(10) not null,"
                    + RESTAURANTNAME +" VARCHAR(10) not null,"
                    + RESTAURANTADDRESS +" VARCHAR(100) not null,"
                    + CONTACTNAME +" VARCHAR(10) not null,"
                    + CONTACTNUMBER +" VARCHAR(10) not null,"
                    + DELIVERYCOMPANYID +" VARCHAR(10) not null,"
                    + RESTAURANTID +" VARCHAR(100) not null );";

    //CREATE DELIVERY COMPANY TABLE
    private static final String CREATE_DELIVERY_COMPANY_TABLE =
            "create table " + DELIVERYCOMPANYTABLE + " ("
                    + ID + " integer primary key autoincrement, "
                    + COMPANYNAME + " VARCHAR(10) not null,"
                    + COMPANYID +" VARCHAR(100) not null );";

    //CREATE DELIVERY REQUESTS TABLE
    private static final String CREATE_DELIVERY_REQUESTS_TABLE =
            "create table " + DELIVERYREQUESTSTABLE + " ("
                    + ID + " integer primary key autoincrement, "
                    + REQUESTID + " VARCHAR(10) not null,"
                    + RESTAURANTID +" VARCHAR(10) not null,"
                    + DELIVERYCOMPANYID +" VARCHAR(10) not null,"
                    + DELIVERYADDRESS +" VARCHAR(100) not null,"
                    + DRIVERID +" VARCHAR(10) not null,"
                    + PREDATE +" VARCHAR(20) not null,"
                    + ISPRE +" VARCHAR(10) not null,"
                    + STATUS +" VARCHAR(10) not null );";

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    private Context context;

    public SQLiteAdapter(Context context){
        this.context = context;
    }

    public SQLiteAdapter openToRead() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    public SQLiteAdapter openToWrite() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        sqLiteHelper.close();
    }

    public long insertRestaurant(String username,String password,String restaurantName,String restaurantAddress,String contactName,String contactNumber,String deliveryCompanyID,String restaurantId){

        long result=0;

        try
        {
            ContentValues contentValues = new ContentValues();

            contentValues.put(USERNAME, username);
            contentValues.put(PASSWORD, password);
            contentValues.put(RESTAURANTNAME, restaurantName);
            contentValues.put(RESTAURANTADDRESS, restaurantAddress);
            contentValues.put(CONTACTNAME, contactName);
            contentValues.put(CONTACTNUMBER, contactNumber);
            contentValues.put(DELIVERYCOMPANYID, deliveryCompanyID);
            contentValues.put(RESTAURANTID, restaurantId);
            result= sqLiteDatabase.insert(RESTAURANTOWNERTABLE, null, contentValues);
        }
        catch(Exception ex)
        {
            Log.e("insertDriver", ex.getMessage());
        }
        return result;
    }

    public HashMap<String, String> getRestaurantOwner(){
        HashMap<String, String> restaurantOwner = new HashMap<String,String>();

        String selectQuery = "SELECT * FROM " + RESTAURANTOWNERTABLE ;

        try
        {
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst())
            {
                do
                {
                    restaurantOwner.put(ID, String.valueOf(cursor.getInt(cursor.getColumnIndex(ID))));
                    restaurantOwner.put(USERNAME, cursor.getString(cursor.getColumnIndex(USERNAME)));
                    restaurantOwner.put(PASSWORD, cursor.getString(cursor.getColumnIndex(PASSWORD)));
                    restaurantOwner.put(RESTAURANTNAME, cursor.getString(cursor.getColumnIndex(RESTAURANTNAME)));
                    restaurantOwner.put(RESTAURANTADDRESS, cursor.getString(cursor.getColumnIndex(RESTAURANTADDRESS)));
                    restaurantOwner.put(CONTACTNAME, cursor.getString(cursor.getColumnIndex(CONTACTNAME)));
                    restaurantOwner.put(CONTACTNUMBER, cursor.getString(cursor.getColumnIndex(CONTACTNUMBER)));
                    restaurantOwner.put(DELIVERYCOMPANYID, cursor.getString(cursor.getColumnIndex(DELIVERYCOMPANYID)));
                    restaurantOwner.put(RESTAURANTID, cursor.getString(cursor.getColumnIndex(RESTAURANTID)));
                }
                while (cursor.moveToNext());

            }
        }
        catch(Exception e)
        {
            e.getMessage();
        }
        return restaurantOwner;
    }

    public int deleteRestaurantOwner(){
        return sqLiteDatabase.delete(RESTAURANTOWNERTABLE, null, null);
    }

    public boolean restaurantOwnerExists()
    {
        try
        {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+RESTAURANTOWNERTABLE, null);

            if(cursor.getCount() > 0)
            {
                return true;
            }

        }
        catch(Exception ex)
        {
            Log.e("restaurantOwnerExists", ex.getMessage());
        }
        return false;
    }

    public long insertDeliveryCompany(String companyName,String companyId){

        long result=0;

        try
        {
            ContentValues contentValues = new ContentValues();

            contentValues.put(COMPANYNAME, companyName);
            contentValues.put(COMPANYID, companyId);
            result= sqLiteDatabase.insert(DELIVERYCOMPANYTABLE, null, contentValues);
        }
        catch(Exception ex)
        {
            Log.e("insertDeliveryCompany", ex.getMessage());
        }
        return result;
    }

    public ArrayList<HashMap<String, String>> getAllDeliveryCompany(){
        ArrayList<HashMap<String, String>> deliveryCompanies = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT * FROM " + DELIVERYCOMPANYTABLE ;

        try
        {
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst())
            {
                do
                {
                    HashMap<String,String> deliveryCompany=new HashMap<String,String>();
                    deliveryCompany.put(ID, String.valueOf(cursor.getInt(cursor.getColumnIndex(ID))));
                    deliveryCompany.put(COMPANYNAME, cursor.getString(cursor.getColumnIndex(COMPANYNAME)));
                    deliveryCompany.put(COMPANYID, cursor.getString(cursor.getColumnIndex(COMPANYID)));

                    deliveryCompanies.add(deliveryCompany);

                }
                while (cursor.moveToNext());

            }
        }
        catch(Exception e)
        {
            e.getMessage();
        }
        return deliveryCompanies;
    }

    public int deleteDeliveryCompany(){
        return sqLiteDatabase.delete(DELIVERYCOMPANYTABLE, null, null);
    }

    public boolean deliveryCompanyExists()
    {
        try
        {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+DELIVERYCOMPANYTABLE, null);

            if(cursor.getCount() > 0)
            {
                return true;
            }

        }
        catch(Exception ex)
        {
            Log.e("deliveryCompanyExists", ex.getMessage());
        }
        return false;
    }

    public long insertDeliveryRequests(String requestId,String restaurantid,String deliverycompanyId,String deliverAddress,String driverId,String status,String preDate,String isPre){

        long result=0;

        try
        {
            ContentValues contentValues = new ContentValues();

            contentValues.put(REQUESTID, requestId);
            contentValues.put(RESTAURANTID, restaurantid);
            contentValues.put(DELIVERYCOMPANYID, deliverycompanyId);
            contentValues.put(DELIVERYADDRESS, deliverAddress);
            contentValues.put(DRIVERID, driverId);
            contentValues.put(STATUS, status);
            contentValues.put(PREDATE, preDate);
            contentValues.put(ISPRE, isPre);
            result= sqLiteDatabase.insert(DELIVERYREQUESTSTABLE, null, contentValues);
        }
        catch(Exception ex)
        {
            Log.e("insertDeliveryRequests", ex.getMessage());
        }
        return result;
    }

    public ArrayList<HashMap<String, String>> getAllDeliveryRequests(){
        ArrayList<HashMap<String, String>> deliveryRequests = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT * FROM " + DELIVERYREQUESTSTABLE ;

        try
        {
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst())
            {
                do
                {
                    HashMap<String,String> deliveryRequest=new HashMap<String,String>();
                    deliveryRequest.put(ID, String.valueOf(cursor.getInt(cursor.getColumnIndex(ID))));
                    deliveryRequest.put(REQUESTID, cursor.getString(cursor.getColumnIndex(REQUESTID)));
                    deliveryRequest.put(RESTAURANTID, cursor.getString(cursor.getColumnIndex(RESTAURANTID)));
                    deliveryRequest.put(DELIVERYCOMPANYID, String.valueOf(cursor.getInt(cursor.getColumnIndex(DELIVERYCOMPANYID))));
                    deliveryRequest.put(DELIVERYADDRESS, cursor.getString(cursor.getColumnIndex(DELIVERYADDRESS)));
                    deliveryRequest.put(DRIVERID, cursor.getString(cursor.getColumnIndex(DRIVERID)));
                    deliveryRequest.put(STATUS, String.valueOf(cursor.getInt(cursor.getColumnIndex(STATUS))));
                    deliveryRequest.put(PREDATE, String.valueOf(cursor.getInt(cursor.getColumnIndex(PREDATE))));
                    deliveryRequest.put(ISPRE, String.valueOf(cursor.getInt(cursor.getColumnIndex(ISPRE))));

                    deliveryRequests.add(deliveryRequest);

                }
                while (cursor.moveToNext());

            }
        }
        catch(Exception e)
        {
            e.getMessage();
        }
        return deliveryRequests;
    }

    public int deleteDeliveryRequests(){
        return sqLiteDatabase.delete(DELIVERYREQUESTSTABLE, null, null);
    }

    public boolean deliveryRequestsExists()
    {
        try
        {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+DELIVERYREQUESTSTABLE, null);

            if(cursor.getCount() > 0)
            {
                return true;
            }

        }
        catch(Exception ex)
        {
            Log.e("deliveryCompanyExists", ex.getMessage());
        }
        return false;
    }

    public class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context, String name,
                            CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(CREATE_RESTAURANT_OWNER_TABLE);
            db.execSQL(CREATE_DELIVERY_REQUESTS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }
    }

}
