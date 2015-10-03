package io.insideaud.insideaudio;

/**
 * Created by Jess on 10/3/15.
 */

import android.app.Activity;
import android.provider.MediaStore;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.view.View;
import android.widget.Button;

public class Camera_S2 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_s2);

    }

//
//    static final int REQUEST_IMAGE_CAPTURE = 1;
//    private ImageView mImageView;
//
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);
//        }
//    }



}
