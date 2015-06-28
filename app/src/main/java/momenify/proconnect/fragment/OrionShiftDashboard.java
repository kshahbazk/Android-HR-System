package momenify.proconnect.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;

import com.parse.ParseUser;


import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.List;


import momenify.proconnect.activities.ChangeStatusActivity;
import momenify.proconnect.activities.SearchAgentActivity;
import momenify.proconnect.adapter.OrionUniversalAdapter;
import momenify.proconnect.navigationviewpagerliveo.LoginActivity;
import momenify.proconnect.navigationviewpagerliveo.R;



/**
 * A simple {@link Fragment} subclass.
 */
public class OrionShiftDashboard extends Fragment {

    ListView listview;
    TextView num;
    private boolean mSearchCheck;
    List<ParseUser> onShift;
    ProgressDialog mProgressDialog;
    OrionUniversalAdapter adapter;
    View v;
    TextView currentStatus;


    private ParseUser currentUser;
    WeakReference<RemoteDataTask> asyncTaskWeakRef;

    public OrionShiftDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentUser = ParseUser.getCurrentUser();
         v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Parse.initialize(getActivity(), "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);


        RelativeLayout updateStatus = (RelativeLayout) v.findViewById(R.id.refreshlayout);
        updateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChangeStatusActivity.class);
                startActivity(i);
            }
        });
        //setRetainInstance(true);
        startNewAsyncTask();

        return v;

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

//    private boolean isAsyncTaskPendingOrRunning() {
//        return this.asyncTaskWeakRef != null &&
//                this.asyncTaskWeakRef.get() != null &&
//                !this.asyncTaskWeakRef.get().getStatus().equals(Status.FINISHED);
//    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<OrionShiftDashboard> fragmentWeakRef;
        private RemoteDataTask (OrionShiftDashboard fragment) {
            this.fragmentWeakRef = new WeakReference<OrionShiftDashboard>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Agents on shift");
            // Set progressdialog message
            mProgressDialog.setMessage("Fetching info");

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
                onShift = UserQuery.whereEqualTo("onShift" , true).find();



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

                // Locate the listview in fragment_dashboard.xml
                listview = (ListView) getView().findViewById(R.id.dashboadlistview);
                num = (TextView) getView().findViewById(R.id.num);

                TextView updatestatus = (TextView) v.findViewById(R.id.status);


                if(ParseUser.getCurrentUser().getString("status").equals("Please update status"))
                {
                    updatestatus.setTextColor(Color.RED);
                }
                else
                {


                    updatestatus.setTextColor(Color.parseColor("#6365ff"));
                }


                currentStatus = (TextView) v.findViewById(R.id.status);

                currentStatus.setText(ParseUser.getCurrentUser().getString("status"));
                // Pass the results into ListViewAdapter.java
                adapter = new OrionUniversalAdapter(getActivity(), onShift);
                // Binds the Adapter to the ListView
                listview.setAdapter(adapter);
                num.setText(onShift.size() + "");
                // Close the progressdialog
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
                  //  currentUser.put("lunch", "Off Shift");


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
