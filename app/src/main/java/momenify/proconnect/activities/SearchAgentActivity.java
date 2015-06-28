package momenify.proconnect.activities;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import momenify.proconnect.adapter.OrionUniversalAdapter;
import momenify.proconnect.navigationviewpagerliveo.R;


public class SearchAgentActivity extends ActionBarActivity {

    ListView listview;
    List<ParseUser> searchedAgent;
    ProgressDialog mProgressDialog;
    OrionUniversalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_agent);
        Parse.initialize(this, "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        new RemoteDataTask().execute();

        ActionBar ab = getSupportActionBar();

        ab.setTitle("Search Results...");

        ab.setDisplayHomeAsUpEnabled(true);


    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(SearchAgentActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Listing Users");
            // Set progressdialog message
            mProgressDialog.setMessage("Searching...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {


            try {
                //Query for all users
                ParseQuery<ParseUser> UserQuery = ParseUser.getQuery();
                Bundle extras = getIntent().getExtras();
                String thesearch = extras.getString("Search");
                if (thesearch.contains("@")) {
                    searchedAgent = UserQuery.whereMatches("email", "(" + thesearch + ")", "i").find();

                } else {
                    searchedAgent = UserQuery.whereMatches("name", "(" + thesearch + ")", "i").find();

                }





            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.searchlistview);
            // Pass the results into ListViewAdapter.java
            adapter = new OrionUniversalAdapter(SearchAgentActivity.this,
                    searchedAgent);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_agent, menu);
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
