package io.indico;

/**
 * Created by Chris on 8/14/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.test.InstrumentationTestCase;
import android.util.Base64;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import io.indico.utils.BitmapUtils;
import io.indico.utils.IndicoException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class UtilsTests extends InstrumentationTestCase {
    Context  appContext;
    Pattern b64_valid;

    @Override protected void setUp() throws Exception {
        super.setUp();
        appContext = getInstrumentation().getTargetContext();
        b64_valid = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");
    }

    /**
     * Bitmap Utils
     */
    public void test_load_bitmap_from_resource() throws IndicoException {
        String result = BitmapUtils.loadScaledBitmap(appContext, R.drawable.test_image, 48, 48, false);
        Bitmap bm = toImage(result);
        assertEquals(bm.getWidth(), 48);
        assertEquals(bm.getHeight(), 48);
    }
    
    public void test_load_bitmap_from_source() throws IndicoException {
        String result = BitmapUtils.loadScaledBitmap(
            appContext,
            Uri.parse("android.resource://io.indico/drawable/test_image"),
            48, 48, false
        );

        Bitmap bm = toImage(result);
        assertEquals(bm.getWidth(), 48);
        assertEquals(bm.getHeight(), 48);
    }

    public void test_get_rectangle() {
        Map<String, List<Double>> points = new HashMap<String, List<Double>>() {{
            put("top_left_corner", Arrays.asList(1.0, 2.0));
            put("bottom_right_corner", Arrays.asList(3.0, 4.0));
        }};

        Rect rect = BitmapUtils.getRectangle(points);
        assertEquals(rect.centerX(), 2);
        assertEquals(rect.centerY(), 3);
    }

    public void test_b64() {
        Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        String result = BitmapUtils.toBase64(bm);

        Bitmap bm2 = toImage(result);
        assertEquals(bm2.getWidth(), 100);
        assertEquals(bm2.getHeight(), 100);
    }

    private Bitmap toImage(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
