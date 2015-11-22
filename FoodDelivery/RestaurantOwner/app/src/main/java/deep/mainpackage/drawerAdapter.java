package deep.mainpackage;

/**
 * Created by Deep on 19/10/2015.
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import deep.mainpackage.R;

/**
 * Created by Deep on 09-Oct-15.
 */
public class drawerAdapter extends RecyclerView.Adapter<drawerAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    //List<Information> data = Collections.emptyList();
    Context context;
    AppCompatActivity activity;
    //private ClickListener clickListener;
    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java

    private String name;        //String Resource for header View Name


    public drawerAdapter(AppCompatActivity activity, Context context, String Titles[], int Icons[], String Name) {
        this.activity=activity;
        this.context=context;
        mNavTitles = Titles;
        mIcons = Icons;
        name = Name;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_menu, parent, false); //Inflating the layout

            MyViewHolder vhItem = new MyViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_menu, parent, false); //Inflating the layout

            MyViewHolder vhHeader = new MyViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (holder.Holderid == 1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.title.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.icon.setImageResource(mIcons[position - 1]);// Setting the image with array of our icons
            holder.title.setTextSize(18);
            /*Information current=data.get(position-1);
            holder.title.setText(current.title);
            holder.icon.setImageResource(current.iconID);*/

        } else {

            //holder.profile.setImageResource(profile);           // Similarly we set the resources for header view
            holder.Name.setText(name);
        }

    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length + 1; // the number of items in the list will be +1 the titles including the header view.
    }


    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        TextView title;
        int Holderid;
        ImageView profile;
        TextView Name;
        TextView email;
        private SQLiteAdapter sqliteAdapter;


        public MyViewHolder(View itemView, int viewType) {
            super(itemView);

            //itemView.setClickable(true);
            //itemView.setOnClickListener(this);
            if (viewType == TYPE_ITEM) { //for item
                title = (TextView) itemView.findViewById(R.id.listText); // Creating TextView object with the id of textView from item_row.xml
                icon = (ImageView) itemView.findViewById(R.id.listIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
                itemView.setOnClickListener(this);
            } else {                       //for header

                Name = (TextView) itemView.findViewById(R.id.name);
                //profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }

        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(context,"Item clicked at "+getPosition(),Toast.LENGTH_SHORT).show();
            //DrawerScreenAfterLogin mainActivity = (DrawerScreenAfterLogin) context ;
            MenuActivity mainActivity = (MenuActivity) activity ;
            mainActivity.drawerLayout.closeDrawers();

            FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();

            Fragment frag = null;
            //Toast.makeText(context,"The Item Clicked is: "+getPosition(), Toast.LENGTH_SHORT).show();
            switch (getPosition()) {
                //dont include case 0: its for header
                case 1:
                    frag = new testFragment();
                    break;
                case 2:
                    frag = new RequestDeliveryFragment(false);
                    break;
                case 3:
                    frag = new RequestDeliveryFragment(true);
                    break;
                case 4:
                    frag = new trackDriver();
                    break;
                case 5:
                    frag = new testFragment();
                    break;
                case 6:
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setMessage("You are exiting the application.\nAre you sure ?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                    break;
                default:
                    break;
            }

            if(frag!=null)
            {
                fragmentTransaction.replace(R.id.containerView, frag);
                fragmentTransaction.commit();
            }

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

                            wipeDatabase();

                            Intent intent = new Intent(context, FirstActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            activity.startActivity(intent);
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

        public void wipeDatabase()
        {
            sqliteAdapter = new SQLiteAdapter(context);
            sqliteAdapter.openToWrite();
            sqliteAdapter.deleteRestaurantOwner();
            sqliteAdapter.deleteDeliveryRequests();
            sqliteAdapter.close();
        }
    }
}