package deep.mainpackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import deep.mainpackage.R;


public class FirstActivity extends ActionBarActivity {

    private SQLiteAdapter sqliteAdapter;
    Activity activity;
    Context context;
    boolean check=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Intent exitIntent=getIntent();
        if(exitIntent.getBooleanExtra("EXIT", false))
        {
            System.gc();
            finish();
        }
        else
        {
            activity = this;
            context = getBaseContext();

            sqliteAdapter = new SQLiteAdapter(context);
            sqliteAdapter.openToRead();
            check = sqliteAdapter.restaurantOwnerExists();
            sqliteAdapter.close();

            if (check) {
                Intent intent = new Intent(context, MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(context, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}
