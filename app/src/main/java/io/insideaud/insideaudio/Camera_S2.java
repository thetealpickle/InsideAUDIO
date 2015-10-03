package io.insideaud.insideaudio;

/**
 * Created by Jess on 10/3/15.
 */


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.content.Intent;
import android.provider.MediaStore;



public class Camera_S2 extends Activity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView mImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_s2);

        dispatchTakePictureIntent();

    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);
//        }
//    }
////



}

//import java.io.FileNotFoundException;
//        import java.io.FileOutputStream;
//        import java.io.IOException;
//
//        import android.app.Activity;
//        import android.hardware.Camera;
//        import android.hardware.Camera.PictureCallback;
//        import android.hardware.Camera.ShutterCallback;
//        import android.os.Bundle;
//        import android.util.Log;
//        import android.view.SurfaceHolder;
//        import android.view.SurfaceView;
//        import android.view.View;
//        import android.widget.TextView;
//        import android.widget.Toast;


//
//public class Camera_S2 extends Activity implements SurfaceHolder.Callback {
//    TextView testView;
//
//    Camera camera;
//    SurfaceView surfaceView;
//    SurfaceHolder surfaceHolder;
//
//    PictureCallback rawCallback;
//    ShutterCallback shutterCallback;
//    PictureCallback jpegCallback;
//
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.camera_s2);
//
//        surfaceView = (SurfaceView) findViewById(R.id.cameraView);
//        surfaceHolder = surfaceView.getHolder();
//
//        // Install a SurfaceHolder.Callback so we get notified when the
//        // underlying surface is created and destroyed.
//        surfaceHolder.addCallback(this);
//
//        // deprecated setting, but required on Android versions prior to 3.0
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//
//        jpegCallback = new PictureCallback() {
//            public void onPictureTaken(byte[] data, Camera camera) {
//                FileOutputStream outStream = null;
//                try {
//                    outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
//                    outStream.write(data);
//                    outStream.close();
//                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                }
//                Toast.makeText(getApplicationContext(), "Picture Saved", 2000).show();
//                refreshCamera();
//            }
//        };
//    }
//
//    public void captureImage(View v) throws IOException {
//        //take the picture
//        camera.takePicture(null, null, jpegCallback);
//    }
//
//    public void refreshCamera() {
//        if (surfaceHolder.getSurface() == null) {
//            // preview surface does not exist
//            return;
//        }
//
//        // stop preview before making changes
//        try {
//            camera.stopPreview();
//        } catch (Exception e) {
//            // ignore: tried to stop a non-existent preview
//        }
//
//        // set preview size and make any resize, rotate or
//        // reformatting changes here
//        // start preview with new settings
//        try {
//            camera.setPreviewDisplay(surfaceHolder);
//            camera.startPreview();
//        } catch (Exception e) {
//
//        }
//    }
//
//    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
//        // Now that the size is known, set up the camera parameters and begin
//        // the preview.
//        refreshCamera();
//    }
//
//    public void surfaceCreated(SurfaceHolder holder) {
//        try {
//            // open the camera
//            camera = Camera.open();
//        } catch (RuntimeException e) {
//            // check for exceptions
//            System.err.println(e);
//            return;
//        }
//        Camera.Parameters param;
//        param = camera.getParameters();
//
//        // modify parameter
//        param.setPreviewSize(352, 288);
//        camera.setParameters(param);
//        try {
//            // The Surface has been created, now tell the camera where to draw
//            // the preview.
//            camera.setPreviewDisplay(surfaceHolder);
//            camera.startPreview();
//        } catch (Exception e) {
//            // check for exceptions
//            System.err.println(e);
//            return;
//        }
//    }
//
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        // stop preview and release camera
//        camera.stopPreview();
//        camera.release();
//        camera = null;
//    }
//
//}