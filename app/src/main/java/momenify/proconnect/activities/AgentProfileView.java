package momenify.proconnect.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import momenify.proconnect.navigationviewpagerliveo.R;

public class AgentProfileView extends ActionBarActivity  {


    ParseUser selectedUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile_view);


        Parse.initialize(this, "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Grab Intent from Previous Adapter
        Intent i = getIntent();
//        // Get the result of name
        String iname = i.getStringExtra("name");
        // Get the result of email
        String iemail = i.getStringExtra("email");
        // Get the result of status
        String istatus = i.getStringExtra("status");
        // Get the result of lunch
        String ilunch = i.getStringExtra("lunch");
        // Get the result of name
        String itime_in = i.getStringExtra("time_in");
        // Get the result of email
        String itime_out = i.getStringExtra("time_out");
        // Get the result of status
        String irole = i.getStringExtra("role");
        // Get the result of lunch
        String iteam_lead = i.getStringExtra("team_lead");
        // Get the result of status
        String iwave = i.getStringExtra("wave");
        // Get the result of lunch
        String ishift = i.getStringExtra("shift");

        String objectId = i.getStringExtra("objectId");

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        try {
             selectedUser = query.get(objectId).fetchIfNeeded();
        } catch (ParseException e)
        {

        }

        ParseFile pic = selectedUser.getParseFile("photo");

        ActionBar ab = getSupportActionBar();

        ab.setTitle(iname +"'s profile");
        ab.setDisplayHomeAsUpEnabled(true);





        // set all text views

        TextView fullname = (TextView) findViewById(R.id.nameLabel);
        TextView email = (TextView) findViewById(R.id.emailLabel);

        ParseImageView image = (ParseImageView) findViewById(R.id.mys1);

        TextView currentStat = (TextView) findViewById(R.id.putStat);
        TextView lunch = (TextView) findViewById(R.id.putlunch);
        TextView timein = (TextView) findViewById(R.id.puttimein);
        TextView timeout = (TextView) findViewById(R.id.puttimeout);

        TextView role = (TextView) findViewById(R.id.putRole);
        TextView lead = (TextView) findViewById(R.id.putLead);
        TextView wave = (TextView) findViewById(R.id.putWave);
        TextView shift = (TextView) findViewById(R.id.putshift);

        //Populate the textviews

        // Populate textviews
        fullname.setText(iname);
        email.setText(iemail);

        currentStat.setText(istatus);
        lunch.setText(ilunch);
        timein.setText(itime_in);
        timeout.setText(itime_out);

        role.setText(irole);
        lead.setText(iteam_lead);
        wave.setText(iwave);
        shift.setText(ishift);

        if(pic!=null)
        {
            image.setParseFile(pic);
            image.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_angent_profile_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
