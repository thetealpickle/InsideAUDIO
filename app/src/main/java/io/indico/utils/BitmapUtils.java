package io.indico.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by chris on 2/20/15.
 */
public class BitmapUtils {
    /**
     * SCALE DOWN A BITMAP BEFORE LOADING
     */
    public static String loadScaledBitmap(Context context, Uri uri, int width, int height, boolean minResize) throws IndicoException {
        if (width <= 0 || height <= 0)
            Log.e("BitmapHelper", "Height or Width is 0! " + width + "x" + height);
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(streamFromUri(context, uri), null, bmOptions);

        // Determine how much to scale down the image
        int scaleFactor = (int) calculateInSampleSize(bmOptions, width, height);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        Bitmap bitmap =  BitmapFactory.decodeStream(streamFromUri(context, uri), null, bmOptions);
        if (bitmap == null) {
            throw new IndicoException("Could not find bitmap at resource id " + uri.toString());
        }
        if (!minResize) {
            bitmap = resize(bitmap, width, height);
        }
        return toBase64(bitmap);

    }

    public static String loadScaledBitmap(Context context, int resource, int width, int height, boolean minResize) throws IndicoException {
        if (width <= 0 || height <= 0)
            Log.e("BitmapHelper", "Height or Width is 0! " + width + "x" + height);
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resource, bmOptions);

        // Determine how much to scale down the image
        int scaleFactor = (int) calculateInSampleSize(bmOptions, width, height);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource, bmOptions);

        if (bitmap == null) {
            throw new IndicoException("Could not find bitmap at resource id " + resource);
        }

        if (!minResize)
            bitmap = resize(bitmap, width, height);

        return toBase64(bitmap);
    }

    public static Bitmap resize(Bitmap bitmap, int width, int height) throws IndicoException {
        if (bitmap == null) {
            throw new IndicoException("Provided bitmap is null.");
        }
        if (width == -1) {
            return bitmap;
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public static Bitmap resize(Bitmap bitmap, int square) throws IndicoException {
        if (bitmap == null) {
            throw new IndicoException("Provided bitmap is null.");
        }
        return resize(bitmap, square, square);
    }

    public static Bitmap resize(Bitmap bitmap, int square, boolean minResize) throws IndicoException {
        if (bitmap == null) {
            throw new IndicoException("Provided bitmap is null.");
        }

        int width = square,
            height = square;

        if (minResize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outWidth = bitmap.getWidth();
            options.outHeight = bitmap.getHeight();
            float divider = calculateInSampleSize(options, square, square);
            width = (int) (square / divider);
            height = (int) (square / divider);

        }
        return resize(bitmap, width, height);
    }

    private static java.io.InputStream streamFromUri(Context context, Uri uri) {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Rect getRectangle(Map<String, List<Double>> res) {
        List<Double> topLeft = res.get("top_left_corner");
        List<Double> bottomRight = res.get("bottom_right_corner");

        int top = topLeft.get(1).intValue();
        int left = topLeft.get(0).intValue();
        int bottom = bottomRight.get(1).intValue();
        int right = bottomRight.get(0).intValue();

        return new Rect(left, top, right, bottom);
    }

    public static String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    // Bitmap Loading Helpers
    private static float calculateInSampleSize(
        BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0)
            Log.e("BitmapHelpers", "calculateInSampleSize was given 0 width or height");
        final int width = options.outWidth;
        final int height = options.outHeight;
        float inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = (float) height / (float) reqHeight;
            } else {
                inSampleSize = (float) width / (float) reqWidth;
            }
        }

        return inSampleSize;
    }

    public static Bitmap loadBitmap(Context context, Uri uri) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(streamFromUri(context, uri), null, bmOptions);
    }

    // For if we want to support pictures via camera
    public static Bitmap rotateBitmapIfNecessary(Bitmap source, Uri file) {
        int orientation = 0;
        try {
            switch (new ExifInterface(file.getPath()).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    orientation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    orientation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    orientation = 270;
                    break;
                // etc.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
