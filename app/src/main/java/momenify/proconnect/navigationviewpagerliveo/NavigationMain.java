package momenify.proconnect.navigationviewpagerliveo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginDispatchActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.navigationliveo.NavigationLiveo;
import momenify.proconnect.fragment.FeedbackFragment;
import momenify.proconnect.fragment.FragmentMain;
import momenify.proconnect.fragment.FragmentViewPager;
import momenify.proconnect.fragment.LeadFragmentViewPager;
import momenify.proconnect.fragment.OrionCsvFragment;
import momenify.proconnect.fragment.OrionLeadLunchAcceptance;
import momenify.proconnect.fragment.Orion_Profile_Page;
import momenify.proconnect.fragment.OrionAgentLunchRequest;

public class NavigationMain extends NavigationLiveo implements NavigationLiveoListener {

    private List<String> mListNameItem;
    private ParseUser myUser;
    private String name;
    private String email;
    private ParseObject myLunch;
    private static final int TEXT_ID = 0;


    private static final int LOGIN_REQUEST = 0;

    @Override
    public void onInt(Bundle bundle) {


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Parse.initialize(this, "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        this.setNavigationListener(this);

        mListNameItem = new ArrayList<>();
        mListNameItem.add(0, "Profile");
        mListNameItem.add(1, "Dashboard");
        mListNameItem.add(2, "Lunch Requests");
        mListNameItem.add(3, "Send CSV");
        mListNameItem.add(4, "more"); //This item will be a subHeader
        mListNameItem.add(5, "Feedback");



        List<Integer> mListIconItem = new ArrayList<>();
        mListIconItem.add(0, R.drawable.ic_person_grey600_48dp);
        mListIconItem.add(1, R.drawable.ic_group_grey600_48dp);;
        mListIconItem.add(2, R.drawable.ic_pages_grey600_48dp);
        mListIconItem.add(3, R.drawable.ic_share_grey600_48dp);
        mListIconItem.add(4, 0); //When the item is a subHeader the value of the icon 0
        mListIconItem.add(5, R.drawable.ic_action_chat);



        // Set current times
        if(myUser.getBoolean("onShift")==false) {
            myUser.put("onShift", true);

            myUser.put("status" , "Please update status");
            myUser.put("lunch" , "Pending");


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String timein = sdf.format(Calendar.getInstance().getTime());

            myUser.put("time_in", timein);
            myUser.put("time_out", "Currently on shift");


        }



        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("lunchObject");
            query.whereEqualTo("agentObjectId", myUser.getObjectId());
            myLunch = query.getFirst();
        }
        catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        if(myLunch!=null) {
            if (!myLunch.getBoolean("active")) {
                if (myLunch.getBoolean("accepted")) {
                     myUser.put("lunch", myLunch.getString("time"));
                    myLunch.deleteInBackground();
                } else {
                    myUser.put("lunch", "rejected please reselect");
                    myLunch.deleteInBackground();
                }
            }
        }

        myUser.saveInBackground();


        List<Integer> mListHeaderItem = new ArrayList<>(); //indicate who the items is a subheader
        mListHeaderItem.add(4);


        SparseIntArray mSparseCounterItem = new SparseIntArray(); //indicate all items that have a counter
        mSparseCounterItem.put(2, 1);


        this.setElevationToolBar(this.getCurrentPosition() != 1 ? 15 : 0);

        this.setFooterInformationDrawer("Enter Productivity", R.drawable.ic_mood_grey600_48dp);

        this.setNavigationAdapter(mListNameItem, mListIconItem, mListHeaderItem, mSparseCounterItem);
    }


    public class SampleDispatchActivity extends ParseLoginDispatchActivity {
        @Override
        protected Class<?> getTargetClass() {
            return NavigationMain.class;
        }
    }

    @Override
    public void onUserInformation() {

        myUser = ParseUser.getCurrentUser();
        name = myUser.getString("name");
        email = myUser.getEmail();

        this.mUserName.setText(name);
        this.mUserEmail.setText(email);
     //   this.mUserPhoto.setImageResource(R.drawable.mys);
        this.mUserBackground.setImageResource(R.drawable.ic_user_background);

    }

    @Override
    public void onItemClickNavigation(int position, int layoutContainerId) {

        Fragment mFragment;
        FragmentManager mFragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:
                mFragment = new Orion_Profile_Page();
                break;
            case 1:
                if(myUser.getBoolean("lead"))
                {
                    mFragment = new LeadFragmentViewPager();
                }
                else
                {
                    mFragment = new FragmentViewPager();
                }


                break;
//
            case 2:
                if(myUser.getBoolean("lead"))
                {
                    mFragment = new OrionLeadLunchAcceptance();
                }
                else
                {
                    mFragment = new OrionAgentLunchRequest();
                }

                break;
            case 3:

                mFragment = new OrionCsvFragment();
                break;


            case 5:

                mFragment = new FeedbackFragment();
                break;


            default:
                mFragment = new FragmentMain().newInstance(mListNameItem.get(position));
        }

        if (mFragment != null) {
            mFragmentManager.beginTransaction().replace(layoutContainerId, mFragment).commit();
        }

        setElevationToolBar(position != 1 ? 15 : 0);
    }

    @Override
    public void onPrepareOptionsMenuNavigation(Menu menu, int position, boolean visible) {
        switch (position) {
            case 0:
                menu.findItem(R.id.menu_add).setVisible(!visible);
                menu.findItem(R.id.menu_search).setVisible(!visible);
                break;
        }
    }

    @Override
    public void onClickFooterItemNavigation(View v) {

        doAlertBox(v);

    }

    @Override
    public void onClickUserPhotoNavigation(View v) {




    }

    public void doAlertBox(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NavigationMain.this);


        alertDialogBuilder.setTitle("Productivity");

        alertDialogBuilder.setMessage("Enter total number of apps reviewed on shift");

        // set positive button: Yes message



        final EditText input = new EditText(this);
        input.setId(TEXT_ID);
        alertDialogBuilder.setView(input);

        alertDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                // go to a new activity of the app

                String value = input.getText().toString();

                myUser.put("productivity", value);

                Toast.makeText(getApplicationContext(), "Submited",
                        Toast.LENGTH_SHORT).show();


            }

        });

        // set negative button: No message

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                // cancel the alert box and put a Toast to the user

                Toast.makeText(getApplicationContext(), "Canceled",
                        Toast.LENGTH_SHORT).show();



                dialog.cancel();


            }

        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        // show alert

        alertDialog.show();
    }
}
