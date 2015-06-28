package momenify.proconnect.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import momenify.proconnect.activities.SearchAgentActivity;
import momenify.proconnect.adapter.OrionUniversalAdapter;
import momenify.proconnect.navigationviewpagerliveo.LoginActivity;
import momenify.proconnect.navigationviewpagerliveo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrionCsvFragment extends Fragment {


    private boolean mSearchCheck;
    List<ParseUser> onShift;
    ProgressDialog mProgressDialog;
    OrionUniversalAdapter adapter;
    View v;
    private String selectedShift;
    static final int REQUEST_SEND_CSV = 1;


    private ParseUser currentUser;
    WeakReference<RemoteDataTask> asyncTaskWeakRef;

    public OrionCsvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        currentUser = ParseUser.getCurrentUser();
        View v = inflater.inflate(R.layout.fragment_orion_csv, container, false);

        RelativeLayout submit = (RelativeLayout) v.findViewById(R.id.submitButton);

        Spinner shiftSpinner = (Spinner) v.findViewById(R.id.selectShift);
        ArrayAdapter<CharSequence> shiftAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.shift_array, android.R.layout.simple_spinner_item);

        shiftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        shiftSpinner.setAdapter(shiftAdapter);

        shiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedShift = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewAsyncTask();
            }
        });


        return v;
    }


    public void createCSVfile(List<ParseUser> list)
    {
        String columnString = "\"Shift\",\"Name\",\"Time in\",\"Time out\",\"Lunch in\",\"Lunch out\",\"Productivity\"";
        String bottomString = "";
        for(ParseUser p : list)
        {
            bottomString = bottomString + createCSVRow(p) + "\n";
        }

        String combinedString = columnString + "\n" + bottomString;

        File file = null;
        File root = Environment.getExternalStorageDirectory();
        if (root.canWrite()) {
            File dir = new File(root.getAbsolutePath() + "/PersonData");
            dir.mkdirs();
            file = new File(dir, selectedShift+".csv");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.write(combinedString.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Uri u1 = null;
        u1 = Uri.fromFile(file);

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Person Details");
        sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
        sendIntent.setType("text/html");

        startActivity(sendIntent);
    }

    public String createCSVRow(ParseUser currentUser)
    {

        String row;
        String shift="not entered";
        String name="not entered";
        String timein="not entered";
        String timeout="not entered";
        String lunch="not entered";
        String backlunch="not entered";
        String productivity="not entered";

        if(currentUser.getString("shift")!=null)
        {
            shift = currentUser.getString("shift");
        }
        if(currentUser.getString("name")!=null)
        {
            name = currentUser.getString("name");;
        }
        if(currentUser.getString("time_in")!=null)
        {
             timein=currentUser.getString("time_in");
        }
        if(currentUser.getString("time_out")!=null)
        {
             timeout=currentUser.getString("time_out");
        }
        if(currentUser.getString("actual_lunch")!=null)
        {
            lunch = currentUser.getString("actual_lunch");
        }
        if(currentUser.getString("back_lunch")!=null)
        {
            backlunch = currentUser.getString("back_lunch");
        }
        if(currentUser.getString("productivity")!=null)
        {
            productivity = currentUser.getString("productivity");
        }

        row = "\"" + shift + "\",\"" + name + "\",\"" + timein + "\",\"" +
                timeout + "\",\"" + lunch + "\",\"" + backlunch + "\",\"" + productivity + "\"";

        return row;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(getActivity(), "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

//        //setRetainInstance(true);
//        startNewAsyncTask();

    }

    private void startNewAsyncTask() {
        RemoteDataTask asyncTask = new RemoteDataTask(this);
        this.asyncTaskWeakRef = new WeakReference<RemoteDataTask>(asyncTask);
        asyncTask.execute();
    }


    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<OrionCsvFragment> fragmentWeakRef;
        private RemoteDataTask (OrionCsvFragment fragment) {
            this.fragmentWeakRef = new WeakReference<OrionCsvFragment>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Creating CSV");
            // Set progressdialog message
            mProgressDialog.setMessage("Creating...");

            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                //Query for all users
                ParseQuery<ParseUser> UserQuery = ParseUser.getQuery();

                //Fill the list of ParseUsers with our query, in this case every one on shift
                onShift = UserQuery.whereEqualTo("shift" , selectedShift).find();



            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }


            return null;
        }



        @Override
        protected void onPostExecute(Void result) {

            if (this.fragmentWeakRef.get() != null) {
                //TODO: treat the result

              createCSVfile(onShift);




                mProgressDialog.dismiss();
            }
        }
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
        //            currentUser.put("lunch", "Off Shift");


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    String timeout = sdf.format(Calendar.getInstance().getTime());

          //          currentUser.put("time_in", "Off Shift");
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




