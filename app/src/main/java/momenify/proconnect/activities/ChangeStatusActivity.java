package momenify.proconnect.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import momenify.proconnect.navigationviewpagerliveo.NavigationMain;
import momenify.proconnect.navigationviewpagerliveo.R;
import momenify.proconnect.objects.Chronometer;

public class ChangeStatusActivity extends ActionBarActivity {

    private ParseUser currentUser;
    private Chronometer genque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orion_change_status);

        Parse.initialize(this, "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        genque =new Chronometer();

        ActionBar ab = getSupportActionBar();

        ab.setTitle("Please select your status");
        ab.setDisplayHomeAsUpEnabled(true);

        //Set Current User

        currentUser = ParseUser.getCurrentUser();

        // Set up Buttons
        RelativeLayout onlunch = (RelativeLayout) findViewById(R.id.lunchButton);
        RelativeLayout backFromLuch = (RelativeLayout) findViewById(R.id.backfromLunchButton);
        RelativeLayout onbreak = (RelativeLayout) findViewById(R.id.breakButton);
        RelativeLayout inGenQueue = (RelativeLayout) findViewById(R.id.genQueueButton);
        RelativeLayout inDFFQueue = (RelativeLayout) findViewById(R.id.dFFButton);
        RelativeLayout auto = (RelativeLayout) findViewById(R.id.autoButton);
        RelativeLayout onSpecial = (RelativeLayout) findViewById(R.id.specialButton);
        RelativeLayout t_c = (RelativeLayout) findViewById(R.id.tcButton);
        RelativeLayout esclations = (RelativeLayout) findViewById(R.id.escalationsButton);

        //Attach the onClick listeners

        onlunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.put("status", "On lunch");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String lunch_in = sdf.format(Calendar.getInstance().getTime());

                currentUser.put("actual_lunch", lunch_in);

                currentUser.saveInBackground();

                Toast.makeText(ChangeStatusActivity.this, "Status selected",
                        Toast.LENGTH_SHORT).show();

                Intent i = new Intent(ChangeStatusActivity.this, NavigationMain.class);
                startActivity(i);
            }
        });

        backFromLuch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.put("status", "Please update status");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String lunch_out = sdf.format(Calendar.getInstance().getTime());
                currentUser.put("back_lunch", lunch_out);
                currentUser.saveInBackground();

                Toast.makeText(ChangeStatusActivity.this, "Status selected",
                        Toast.LENGTH_SHORT).show();

                Intent i = new Intent(ChangeStatusActivity.this, NavigationMain.class);
                startActivity(i);
            }
        });

        onbreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.put("status", "On break");


                currentUser.saveInBackground();

                Toast.makeText(ChangeStatusActivity.this, "Status selected",
                        Toast.LENGTH_SHORT).show();

                Intent i = new Intent(ChangeStatusActivity.this, NavigationMain.class);
                startActivity(i);
            }
        });

        inGenQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(currentUser.getString("status").equals("In Gen queue"))
                {
                    Toast.makeText(ChangeStatusActivity.this, "Already in Gen Queue",
                            Toast.LENGTH_SHORT).show();

                }
                else {


                    currentUser.put("status", "In Gen queue");
                    currentUser.saveInBackground();

                    Toast.makeText(ChangeStatusActivity.this, "Status selected",
                            Toast.LENGTH_SHORT).show();


                    Intent i = new Intent(ChangeStatusActivity.this, NavigationMain.class);
                    startActivity(i);
                }
            }
        });

        inDFFQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.put("status", "In DFF queue");





                Toast.makeText(ChangeStatusActivity.this, "Status selected",
                        Toast.LENGTH_SHORT).show();


                currentUser.saveInBackground();
                Intent i = new Intent(ChangeStatusActivity.this, NavigationMain.class);
                startActivity(i);
            }
        });

        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.put("status", "C");
                currentUser.saveInBackground();




                Toast.makeText(ChangeStatusActivity.this, "Status selected",
                        Toast.LENGTH_SHORT).show();

                Intent i = new Intent(ChangeStatusActivity.this, NavigationMain.class);
                startActivity(i);
            }
        });

        onSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.put("status", "On Special Project");
                currentUser.saveInBackground();




                Toast.makeText(ChangeStatusActivity.this, "Status selected",
                        Toast.LENGTH_SHORT).show();

                Intent i = new Intent(ChangeStatusActivity.this, NavigationMain.class);
                startActivity(i);
            }
        });

        t_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.put("status", "In Training/Coaching");
                currentUser.saveInBackground();




                Toast.makeText(ChangeStatusActivity.this, "Status selected",
                        Toast.LENGTH_SHORT).show();

                Intent i = new Intent(ChangeStatusActivity.this, NavigationMain.class);
                startActivity(i);
            }
        });

        esclations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.put("status", "In Esclations queue");
                currentUser.saveInBackground();


             

                Toast.makeText(ChangeStatusActivity.this, "Status selected",
                        Toast.LENGTH_SHORT).show();

                Intent i = new Intent(ChangeStatusActivity.this, NavigationMain.class);
                startActivity(i);
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_status, menu);
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
