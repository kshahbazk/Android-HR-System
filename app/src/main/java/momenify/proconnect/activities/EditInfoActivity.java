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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.parse.Parse;
import com.parse.ParseUser;

import momenify.proconnect.navigationviewpagerliveo.NavigationMain;
import momenify.proconnect.navigationviewpagerliveo.R;

public class EditInfoActivity extends ActionBarActivity {


    private ParseUser currentUser;
    private String role;
    private String lead;
    private String wave;
    private String shift;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orion_edit_profile);

        Parse.initialize(this, "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Set Action bar title
        ActionBar ab = getSupportActionBar();

        ab.setTitle("Edit Info");
        ab.setDisplayHomeAsUpEnabled(true);

         //Set current user
        currentUser = ParseUser.getCurrentUser();

        // Set all the spinners edittexts and buttons

        Spinner roleSpinner = (Spinner) findViewById(R.id.selectRole);
        Spinner leadSpinner = (Spinner) findViewById(R.id.selectLead);
        Spinner waveSpinner = (Spinner) findViewById(R.id.selectWave);
        Spinner shiftSpinner = (Spinner) findViewById(R.id.selectShiftSpinner);

        RelativeLayout submitButton = (RelativeLayout) findViewById(R.id.submitButton);

        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(this,
                R.array.role_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> leadAdapter = ArrayAdapter.createFromResource(this,
                R.array.lead_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> waveAdapter = ArrayAdapter.createFromResource(this,
                R.array.wave_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> shiftAdapter = ArrayAdapter.createFromResource(this,
                R.array.shift_array, android.R.layout.simple_spinner_item);

        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shiftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        roleSpinner.setAdapter(roleAdapter);
        leadSpinner.setAdapter(leadAdapter);
        waveSpinner.setAdapter(waveAdapter);
        shiftSpinner.setAdapter(shiftAdapter);


        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                role = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        leadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lead = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        waveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wave = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        shiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shift = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(role != null) {
                    currentUser.put("role", role);
                }

                if(lead != null) {
                    currentUser.put("team_lead", lead);
                }

                if(wave != null) {
                    currentUser.put("wave", wave);
                }

                if(shift != null) {
                    currentUser.put("shift", shift);
                }

                currentUser.saveInBackground();

                Intent i = new Intent(EditInfoActivity.this, NavigationMain.class);
                startActivity(i);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_info, menu);
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
