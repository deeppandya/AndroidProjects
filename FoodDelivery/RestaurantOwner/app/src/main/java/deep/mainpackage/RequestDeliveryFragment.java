package deep.mainpackage;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDeliveryFragment extends android.support.v4.app.Fragment {

    private boolean isPre=false;
    View rootView;
    Activity activity;
    Context context;
    EditText deliveryAddress;
    EditText txtDateTime;
    private SQLiteAdapter sqliteAdapter;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    private Toolbar toolbar;

    public RequestDeliveryFragment() {
    }

    public RequestDeliveryFragment(boolean isPre) {
        this.isPre=isPre;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_request_delivery, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity=getActivity();
        context=activity.getBaseContext();

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        deliveryAddress=(EditText)rootView.findViewById(R.id.txtaddress);
        txtDateTime=(EditText)rootView.findViewById(R.id.txtDate);
        txtDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeDialog();
            }
        });

        if(isPre)
        {
            toolbar.setTitle(getResources().getString(R.string.menu_request_pre_delivery));
            txtDateTime.setVisibility(View.VISIBLE);
        }
        else
        {
            toolbar.setTitle(getResources().getString(R.string.menu_request_delivery));
            txtDateTime.setVisibility(View.GONE);
        }

        AppCompatButton btnSubmit=(AppCompatButton)rootView.findViewById(R.id.btnsubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    sqliteAdapter = new SQLiteAdapter(context);
                    sqliteAdapter.openToWrite();
                    HashMap<String,String> restaurantOwner=sqliteAdapter.getRestaurantOwner();
                    String restaurantID=restaurantOwner.get(SQLiteAdapter.RESTAURANTID);
                    String deliveryCompanyID=restaurantOwner.get(SQLiteAdapter.DELIVERYCOMPANYID);
                    sqliteAdapter.close();
                    addDeliveryRequest addRequest=new addDeliveryRequest(activity,context,restaurantID,deliveryCompanyID,deliveryAddress.getText().toString(),txtDateTime.getText().toString(),String.valueOf(isPre));
                    addRequest.execute();
                }
            }
        });

        /*final Spinner companySpinner=(Spinner)rootView.findViewById(R.id.companyspinner);
        companySpinner.setVisibility(View.VISIBLE);
        setupSpinner(companySpinner);

        companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int selected = 0;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    private boolean validate() {
        boolean valid = true;

        String address = deliveryAddress.getText().toString();

        if (address.isEmpty()) {
            deliveryAddress.setError("cannot be empty");
            valid = false;
        } else {
            deliveryAddress.setError(null);
        }
        return valid;

    }

    public void setDateTimeDialog()
    {
        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //txtDateTime.setText(dateFormatter.format(newDate.getTime()));
                setTimePicker(year,monthOfYear,dayOfMonth);
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.setTitle("Select Date");
        datePicker.show();
    }

    public void setTimePicker(final int year, final int monthOfYear, final int dayOfMonth)
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar calendar = new GregorianCalendar(year,monthOfYear,dayOfMonth,selectedHour,selectedMinute);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                txtDateTime.setText(sdf.format(calendar.getTime()));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    /*public void setupSpinner(Spinner spin){
        try
        {
            ArrayList<HashMap<String,String>> items=getDataSet();
            spinnerAdapter spinneradapter = new spinnerAdapter(activity,context);
            spinneradapter.addItems(items);
            spin.setAdapter(spinneradapter);
        }
        catch(Exception ex)
        {
            Log.e("setupSpinner", ex.getMessage());
        }
    }

    private ArrayList<HashMap<String,String>> getDataSet() {

        sqliteAdapter = new SQLiteAdapter(context);
        sqliteAdapter.openToRead();
        ArrayList<HashMap<String,String>> results =sqliteAdapter.getAllDeliveryCompany();
        sqliteAdapter.close();

        return results;
    }*/


}
