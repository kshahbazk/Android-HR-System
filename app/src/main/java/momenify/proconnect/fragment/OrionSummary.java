package momenify.proconnect.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
public class OrionSummary extends Fragment {

    ListView listview;
    TextView num;
    private boolean mSearchCheck;
    List<ParseUser> onShift;
    ProgressDialog mProgressDialog;
    OrionUniversalAdapter adapter;
    View v;

    private ParseUser currentUser;
    WeakReference<RemoteDataTask> asyncTaskWeakRef;


    public OrionSummary() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentUser = ParseUser.getCurrentUser();
        v = inflater.inflate(R.layout.orion_summary, container, false);
        Parse.initialize(getActivity(), "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);



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

    private class RemoteDataTask extends AsyncTask<List<ParseUser>, List<ParseUser>, List<ParseUser>> {

        private WeakReference<OrionSummary> fragmentWeakRef;
        private int total;
        private int totaloff;
        private RemoteDataTask (OrionSummary fragment) {
            this.fragmentWeakRef = new WeakReference<OrionSummary> (fragment);
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
        protected List<ParseUser> doInBackground(List<ParseUser>... params) {

            try {
                //Query for all users
                ParseQuery<ParseUser> UserQuery = ParseUser.getQuery();

                //Fill the list of ParseUsers with our query, in this case every one on shift
                 onShift = UserQuery.whereEqualTo("onShift" , true).find();




            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }


            return onShift;
        }



        @Override
        protected void onPostExecute(List<ParseUser> result) {

            if (this.fragmentWeakRef.get() != null) {
                //TODO: treat the result


                setViews(onShift);

                mProgressDialog.dismiss();
            }
        }
    }

    public void setViews(List<ParseUser> result)
    {

        TextView total_p = (TextView) v.findViewById(R.id.putTotal);
        TextView gen = (TextView) v.findViewById(R.id.putGenQueue);
        TextView dff = (TextView) v.findViewById(R.id.putDffQueue);
        TextView esc = (TextView) v.findViewById(R.id.putEscalationsQueue);
        TextView train = (TextView) v.findViewById(R.id.putTraingCoaching);
        TextView auto = (TextView) v.findViewById(R.id.putPa);
        TextView special = (TextView) v.findViewById(R.id.putSpecial);
        TextView total_off = (TextView) v.findViewById(R.id.putTotalOff);
        TextView lunch = (TextView) v.findViewById(R.id.putLunch);
        TextView onbreak = (TextView) v.findViewById(R.id.putBreak);
        TextView TotalOnShift = (TextView) v.findViewById(R.id.TotalLabelput);

        int genCounter = 0;
        int dffCounter = 0;
        int escCounter = 0;
        int trainCounter = 0;
        int autoCounter = 0;
        int specialCounter = 0;
        int lunchCounter = 0;
        int breakCounter = 0;
        int totalp;
        int totalo;
        int overAll;



        for(ParseUser p : result)
        {
            if(p.getString("status").equals("In Gen queue"))
            {
                genCounter++;
            }

            if(p.getString("status").equals("In DFF queue"))
            {
                dffCounter++;
            }

            if(p.getString("status").equals("In Play Auto queue"))
            {
                escCounter++;
            }

            if(p.getString("status").equals("On Special Project"))
            {
                trainCounter++;
            }

            if(p.getString("status").equals("In Training/Coaching"))
            {
                autoCounter++;
            }

            if(p.getString("status").equals("In Esclations queue"))
            {
                specialCounter++;
            }

            if(p.getString("status").equals("On break"))
            {
                lunchCounter++;
            }

            if(p.getString("status").equals("On lunch"))
            {
                breakCounter++;
            }
        }

        totalp=genCounter+dffCounter+escCounter+trainCounter+autoCounter+specialCounter;
        totalo=lunchCounter+breakCounter;
        overAll = totalo + totalp;

        total_p.setText(Integer.toString(totalp));
        gen.setText(Integer.toString(genCounter));
        dff.setText(Integer.toString(dffCounter));
        esc.setText(Integer.toString(escCounter));
        train.setText(Integer.toString(trainCounter));
        auto.setText(Integer.toString(autoCounter));
        special.setText(Integer.toString(specialCounter));
        total_off.setText(Integer.toString(totalo));
        lunch.setText(Integer.toString(lunchCounter));
        onbreak.setText(Integer.toString(breakCounter));
        TotalOnShift.setText(Integer.toString(overAll));


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
