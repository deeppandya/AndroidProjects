package deep.mainpackage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import deep.mainpackage.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;


public class MenuActivity extends AppCompatActivity {

    Activity activity;
    Context context;

    private Toolbar toolbar;
    public DrawerLayout drawerLayout;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;
    ActionBarDrawerToggle mDrawerToggle;

    public static final String PREF_FILE_NAME="testpref";
    public static final String KEY_USER_LEARNED_DRAWER="ser_learned_drawer";


    int ICONS[] = {R.drawable.ic_home,R.drawable.ic_requests,R.drawable.ic_requests,R.drawable.ic_track_driver,R.drawable.ic_add_driver,R.drawable.ic_sign_out};

    String NAME = "Welcome, Deep Pandya";

    private boolean mUserLearnedDrawer; //whether user is aware of drawer s existence or not
    private boolean mFromSavedInstanceState;    //whether this fragment is being started for first time
    // or its coming back from rotation


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        activity=this;
        context=getBaseContext();

        String TITLES[] = {getResources().getString(R.string.menu_home),getResources().getString(R.string.menu_request_delivery),getResources().getString(R.string.menu_request_pre_delivery),getResources().getString(R.string.menu_track_driver),getResources().getString(R.string.menu_history),getResources().getString(R.string.menu_sign_out)};

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        mAdapter = new drawerAdapter(this,getBaseContext(),TITLES,ICONS,NAME);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //getDriversByCompanyID getDrivers=new getDriversByCompanyID(context);
        //getDrivers.execute();

        /*NavigationDrawerFragment drawerFragment =
                (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        */



        /*drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, toolbar);*/
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();  //it will make your activity draw the actionbar again
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer){   //if the user has not the drawer ever; that means its for the first time
                    mUserLearnedDrawer=true;
                    saveToPreferences(getBaseContext(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+"");
                    invalidateOptionsMenu();  //it will make your activity draw the actionbar again
                }
            }
        };

        //if the app or this fragment is being opened for the first time, then you want to display your drawer to the user
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            View v = this.findViewById(R.id.recyclerView);
            drawerLayout.openDrawer(v);
        }
        drawerLayout.setDrawerListener(mDrawerToggle);

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });




        //Add the Very First i.e drawer home Fragment to the Container
        Fragment drawerHome = new testFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerView,drawerHome,null);
        fragmentTransaction.commit();
    }


    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage("You are exiting the application.\nAre you sure ?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            try
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(context, FirstActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
            catch(Exception ex)
            {
                Log.e("OnBackPressed", ex.getMessage());
            }
        }
    };
}