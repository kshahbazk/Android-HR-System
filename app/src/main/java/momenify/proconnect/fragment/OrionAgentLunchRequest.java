package momenify.proconnect.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import momenify.proconnect.activities.OrionLunchAcceptance;
import momenify.proconnect.activities.SearchAgentActivity;
import momenify.proconnect.navigationviewpagerliveo.LoginActivity;
import momenify.proconnect.navigationviewpagerliveo.NavigationMain;
import momenify.proconnect.navigationviewpagerliveo.R;

/**
 * A simple {@link Fragment} subclass.
 */


public class OrionAgentLunchRequest extends Fragment {

    private TimePicker timePicker1;

    private String format = "";
    private boolean mSearchCheck;
    private ParseUser currentUser;
    public OrionAgentLunchRequest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.orion_agent_lunch_request, container, false);
        Parse.initialize(getActivity(), "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        currentUser = ParseUser.getCurrentUser();




        timePicker1 = (TimePicker) v.findViewById(R.id.timePicker);




        RelativeLayout submitButton = (RelativeLayout) v.findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser.getString("lunch").equals("pending approval")) {

                    Toast.makeText(getActivity(), "Lunch request is pending",
                            Toast.LENGTH_LONG).show();
                }else
                {
                    ParseObject lunchRequest = new ParseObject("lunchObject");
                    lunchRequest.put("agentObjectId", currentUser.getObjectId());
                    lunchRequest.put("name", currentUser.getString("name"));
                    if(currentUser.getString("shift")!=null) {
                        lunchRequest.put("shift", currentUser.getString("shift"));
                    }
                    else
                    {
                        lunchRequest.put("shift", "none selected");
                    }
                    lunchRequest.put("time", getTime());
                    lunchRequest.put("accepted", false);
                    lunchRequest.put("active", true);
                    currentUser.put("lunch", "pending approval");
                    lunchRequest.saveInBackground();
                    currentUser.saveInBackground();


                    Toast.makeText(getActivity(), "Lunch request sent",
                            Toast.LENGTH_LONG).show();

                    Intent i = new Intent(getActivity(), NavigationMain.class);
                    startActivity(i);
                }

            }
        });



        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(getActivity(), "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);


    }

    public void setTime(View view) {


    }

    public String getTime() {

        int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        String curTime = String.format("%02d:%02d", hour, min);
        return new StringBuilder().append(curTime)
                .append(" ").append(format).toString();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        searchView.setQueryHint(this.getString(R.string.search));

        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                .setHintTextColor(getResources().getColor(R.color.nliveo_white));
        searchView.setOnQueryTextListener(onQuerySearchView);

        menu.findItem(R.id.menu_add).setVisible(true);
        menu.findItem(R.id.menu_search).setVisible(true);

        mSearchCheck = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case R.id.menu_add:

                if(currentUser.getBoolean("onShift")) {
                    currentUser.put("onShift", false);

                    currentUser.put("status", "Off Shift");
                    //    currentUser.put("lunch", "Off Shift");


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    String timeout = sdf.format(Calendar.getInstance().getTime());

                    //   currentUser.put("time_in", "Off Shift");
                    currentUser.put("time_out", timeout);



                    currentUser.saveInBackground();
                }




                currentUser.logOut();

                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                getActivity().finish();
                break;

            case R.id.menu_search:
                mSearchCheck = true;

                break;
        }
        return true;
    }

    private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {

            if (mSearchCheck) {
                if (s != null) {
                    Intent i2 = new Intent(getActivity(), SearchAgentActivity.class);
                    i2.putExtra("Search", s);
                    startActivity(i2);
                }
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {

            return false;
        }
    };


}
