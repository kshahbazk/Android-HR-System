package momenify.proconnect.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import momenify.proconnect.adapter.OrionUniversalAdapter;
import momenify.proconnect.navigationviewpagerliveo.NavigationMain;
import momenify.proconnect.navigationviewpagerliveo.R;

public class OrionCameraActivity extends ActionBarActivity {

    private Camera camera;
    private SurfaceView surfaceView;
    private ParseFile photoFile;
    private ImageButton photoButton;
    ProgressDialog mProgressDialog;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView pic;
    String mCurrentPhotoPath;
    public static final String TAG = "CameraFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orion_camera);
        Parse.initialize(this, "dAs3XsEolRwzDpAhf0L7ZF2kXbId851ir48Pwoe2", "5XinGADJUqC7AEpOvdDoeR3FAmGZmgfo3n8QWDB4");
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        photoButton = (ImageButton) findViewById(R.id.camera_photo_button);

//        if (camera == null) {
//            try {
//                camera = Camera.open();
//                photoButton.setEnabled(true);
//            } catch (Exception e) {
//                Log.e(TAG, "No camera with exception: " + e.getMessage());
//                photoButton.setEnabled(false);
//                Toast.makeText(OrionCameraActivity.this, "No camera detected",
//                        Toast.LENGTH_LONG).show();
//            }
//        }
//
//        photoButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (camera == null)
//                    return;
//                camera.takePicture(new Camera.ShutterCallback() {
//
//                    @Override
//                    public void onShutter() {
//                        // nothing to do
//                    }
//
//                }, null, new Camera.PictureCallback() {
//
//                    @Override
//                    public void onPictureTaken(byte[] data, Camera camera) {
//                        new RemoteDataTask(data).execute();
//                    }
//
//                });
//
//            }
//        });

//        surfaceView = (SurfaceView) findViewById(R.id.camera_surface_view);
//        SurfaceHolder holder = surfaceView.getHolder();
//        holder.addCallback(new SurfaceHolder.Callback() {
//
//            public void surfaceCreated(SurfaceHolder holder) {
//                try {
//                    if (camera != null) {
//                        camera.setDisplayOrientation(90);
//                        camera.setPreviewDisplay(holder);
//                        camera.startPreview();
//                    }
//                } catch (IOException e) {
//                    Log.e(TAG, "Error setting up preview", e);
//                }
//            }
//
//            public void surfaceChanged(SurfaceHolder holder, int format,
//                                       int width, int height) {
//                // nothing to do here
//            }
//
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                // nothing here
//            }
//
//        });
    }




    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            pic.setImageBitmap(imageBitmap);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = pic.getWidth();
        int targetH = pic.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);


        pic.setImageBitmap(bitmap);
    }















    /*
     * ParseQueryAdapter loads ParseFiles into a ParseImageView at whatever size
     * they are saved. Since we never need a full-size image in our app, we'll
     * save a scaled one right away.
     */
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
                        Toast.makeText(OrionCameraActivity.this,
                                "Error saving: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        addPhotoToMealAndReturn(photoFile);
                        Toast.makeText(OrionCameraActivity.this, "Saved",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (OutOfMemoryError error)
        {
            Toast.makeText(OrionCameraActivity.this,
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
        Intent i = new Intent(OrionCameraActivity.this, NavigationMain.class);
        startActivity(i);
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 50;
        int targetHeight = 50;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
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
                Toast.makeText(OrionCameraActivity.this, "No camera detected",
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


    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        byte [] a;
        public RemoteDataTask(byte[] array)
        {
            a=array;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(OrionCameraActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Saving Image");
            // Set progressdialog message
            mProgressDialog.setMessage("Saving...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

         saveScaledPhoto(a);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            mProgressDialog.dismiss();
        }
    }
}
