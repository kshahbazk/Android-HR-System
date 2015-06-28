package momenify.proconnect.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


import android.graphics.Matrix;

import android.view.SurfaceHolder.Callback;
import android.widget.ImageButton;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import com.parse.ParseUser;

import momenify.proconnect.navigationviewpagerliveo.NavigationMain;
import momenify.proconnect.navigationviewpagerliveo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrionCameraFragment extends ActionBarActivity {

    private Camera camera;
    private SurfaceView surfaceView;
    private ParseFile photoFile;
    private ImageButton photoButton;
    private ImageButton galleryButton;
    public static final String TAG = "CameraFragment";

    public OrionCameraFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orion_camera);
        Parse.initialize(this, "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        photoButton = (ImageButton) findViewById(R.id.camera_photo_button);
       // galleryButton= (ImageButton) findViewById(R.id.camera_photo_gallary);

        if (camera == null) {
            try {
                camera = Camera.open();
                photoButton.setEnabled(true);
            } catch (Exception e) {
                Log.e(TAG, "No camera with exception: " + e.getMessage());
                photoButton.setEnabled(false);
                Toast.makeText(OrionCameraFragment.this, "No camera detected",
                        Toast.LENGTH_LONG).show();
            }
        }

        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (camera == null)
                    return;
                camera.takePicture(new Camera.ShutterCallback() {

                    @Override
                    public void onShutter() {
                        // nothing to do
                    }

                }, null, new Camera.PictureCallback() {

                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        saveScaledPhoto(data);
                    }

                });

            }
        });

        surfaceView = (SurfaceView) findViewById(R.id.camera_surface_view);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new Callback() {

            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (camera != null) {
                        camera.setDisplayOrientation(90);
                        camera.setPreviewDisplay(holder);
                        camera.startPreview();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error setting up preview", e);
                }
            }

            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
                // nothing to do here
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                // nothing here
            }

        });
    }


    /*
     * ParseQueryAdapter loads ParseFiles into a ParseImageView at whatever size
     * they are saved. Since we never need a full-size image in our app, we'll
     * save a scaled one right away.
     */
    private void saveScaledPhoto(byte[] data) throws OutOfMemoryError{


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
            rotatedScaledMealImage.compress(Bitmap.CompressFormat.PNG, 100, bos);

            byte[] scaledData = bos.toByteArray();

            // Save the scaled image to Parse
            photoFile = new ParseFile("meal_photo.jpg", scaledData);
            photoFile.saveInBackground(new SaveCallback() {

                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(OrionCameraFragment.this,
                                "Error saving: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        addPhotoToMealAndReturn(photoFile);
                        Toast.makeText(OrionCameraFragment.this, "Saved",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (OutOfMemoryError error)
        {
            Toast.makeText(OrionCameraFragment.this,
                    "Whoops hold on a sec",
                    Toast.LENGTH_LONG).show();
        }
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



    /*
     * Once the photo has saved successfully, we're ready to return to the
     * NewMealFragment. When we added the CameraFragment to the back stack, we
     * named it "NewMealFragment". Now we'll pop fragments off the back stack
     * until we reach that Fragment.
     */
    private void addPhotoToMealAndReturn(ParseFile photoFile) {
        ParseUser.getCurrentUser().put("photo", photoFile);
        Intent i = new Intent(OrionCameraFragment.this, NavigationMain.class);
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (camera == null) {
            try {
                camera = Camera.open();
                photoButton.setEnabled(true);
            } catch (Exception e) {
                Log.i(TAG, "No camera: " + e.getMessage());
                photoButton.setEnabled(false);
                Toast.makeText(OrionCameraFragment.this, "No camera detected",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onPause() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
        super.onPause();
    }




}
