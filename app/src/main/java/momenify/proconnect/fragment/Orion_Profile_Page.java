package momenify.proconnect.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.SaveCallback;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import momenify.proconnect.activities.ChangeStatusActivity;
import momenify.proconnect.activities.EditInfoActivity;
import momenify.proconnect.activities.SearchAgentActivity;
import momenify.proconnect.navigationviewpagerliveo.LoginActivity;
import momenify.proconnect.navigationviewpagerliveo.NavigationMain;
import momenify.proconnect.navigationviewpagerliveo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Orion_Profile_Page extends Fragment {

    private boolean mSearchCheck;
    private ParseUser currentUser;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView pic;
    private ParseFile photoFile;
    ParseObject myLunch;
    View v;
    ParseImageView my;
    ProgressDialog mProgressDialog;



    public Orion_Profile_Page() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.orion_profile_page, container, false);


        Parse.initialize(getActivity(), "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);


        //Fetch the current user object
        currentUser = ParseUser.getCurrentUser();

        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("lunchObject");
            query.whereEqualTo("agentObjectId", currentUser.getObjectId());
            myLunch = query.getFirst();
        }
        catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        if(myLunch!=null) {
            if (!myLunch.getBoolean("active")) {
                if (myLunch.getBoolean("accepted")) {
                    currentUser.put("lunch", myLunch.getString("time"));
                    myLunch.deleteInBackground();
                } else {
                    currentUser.put("lunch", "rejected please reselect");
                    myLunch.deleteInBackground();
                }
            }
        }

        currentUser.saveInBackground();

        // get all text views
        TextView fullname = (TextView) v.findViewById(R.id.nameLabel);
        TextView email = (TextView) v.findViewById(R.id.emailLabel);

        TextView currentStat = (TextView) v.findViewById(R.id.putStat);
        TextView lunch = (TextView) v.findViewById(R.id.putlunch);
        TextView timein = (TextView) v.findViewById(R.id.puttimein);
        TextView timeout = (TextView) v.findViewById(R.id.puttimeout);

        TextView role = (TextView) v.findViewById(R.id.putRole);
        TextView lead = (TextView) v.findViewById(R.id.putLead);
        TextView wave = (TextView) v.findViewById(R.id.putWave);
        TextView shift = (TextView) v.findViewById(R.id.putshift);

         my = (ParseImageView) v.findViewById(R.id.mys1);

        //get surfaceview



        // Set up Buttons
        RelativeLayout updateStatus = (RelativeLayout) v.findViewById(R.id.statusbutton);
        RelativeLayout editInfo = (RelativeLayout) v.findViewById(R.id.editInfo);

       // Populate textviews
        fullname.setText(currentUser.getString("name"));
        email.setText(currentUser.getEmail());

        currentStat.setText(currentUser.getString("status"));

        timein.setText(currentUser.getString("time_in"));
        timeout.setText(currentUser.getString("time_out"));

        role.setText(currentUser.getString("role"));
        wave.setText(currentUser.getString("wave"));
        shift.setText(currentUser.getString("shift"));

        lunch.setText(currentUser.getString("lunch"));
        lead.setText(currentUser.getString("team_lead"));


        ParseFile pic = currentUser.getParseFile("photo");

        if(pic!=null)
        {
            my.setParseFile(pic);
            my.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }





        // Go to Update Status Activity
        updateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChangeStatusActivity.class);
                startActivity(i);
            }
        });

        // Go to Edit Info Activity

        // Go to Update Status Activity
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditInfoActivity.class);
                startActivity(i);

            }
        });

        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });







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


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                byte [] image = convertBitmapToByteArray(imageBitmap);
                saveScaledPhoto(image);
               // my.setImageBitmap(imageBitmap);
            }
        }
    }


    private byte[] convertBitmapToByteArray(Bitmap b)
    {
        // Override Android default landscape orientation and save portrait
        Matrix matrix = new Matrix();
        matrix.postRotate(270);
        Bitmap rotatedScaledMealImage = Bitmap.createBitmap(b, 0,
                0, b.getWidth(), b.getHeight(),
                matrix, true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotatedScaledMealImage.compress(Bitmap.CompressFormat.JPEG, 70, bos);

        byte[] scaledData = bos.toByteArray();
        return scaledData;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    private void saveScaledPhoto(byte[] data) {

        try {


            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeByteArray(data, 0, data.length, options);

            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;



            options.inSampleSize = calculateInSampleSize(options, imageWidth, imageHeight);

            options.inJustDecodeBounds = false;


            Bitmap decoded = BitmapFactory.decodeByteArray(data, 0, data.length, options);



            // Override Android default landscape orientation and save portrait
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedScaledMealImage = Bitmap.createBitmap(decoded, 0,
                    0, decoded.getWidth(), decoded.getHeight(),
                    matrix, true);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            rotatedScaledMealImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            byte[] scaledData = bos.toByteArray();

            // Save the scaled image to Parse
            photoFile = new ParseFile("meal_photo.jpg", scaledData);
            photoFile.saveInBackground(new SaveCallback() {

                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(),
                                "Error saving: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        addPhotoToMealAndReturn(photoFile);
                        Toast.makeText(getActivity(), "Saved",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (OutOfMemoryError error)
        {
            Toast.makeText(getActivity(),
                    "Whoops hold on a sec",
                    Toast.LENGTH_LONG).show();
        }
    }





    private void addPhotoToMealAndReturn(ParseFile photoFile) {
        ParseUser.getCurrentUser().put("photo", photoFile);
        Intent i = new Intent(getActivity(), NavigationMain.class);
        startActivity(i);
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
            //        currentUser.put("lunch", "Off Shift");


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    String timeout = sdf.format(Calendar.getInstance().getTime());

             //       currentUser.put("time_in", "Off Shift");
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
