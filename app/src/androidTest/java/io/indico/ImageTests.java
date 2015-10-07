package io.indico;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.InstrumentationTestCase;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import io.indico.api.Api;
import io.indico.enums.FacialEmotion;
import io.indico.network.IndicoCallback;
import io.indico.results.IndicoResult;
import io.indico.utils.BitmapUtils;
import io.indico.utils.IndicoException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ImageTests extends InstrumentationTestCase {
    String apiKey;
    String image_example;

    @Override protected void setUp() throws Exception {
        super.setUp();
        try {
            Properties props = new Properties();
            InputStream inputStream =
                getInstrumentation().getTargetContext().getAssets().open("indico.properties");
            props.load(inputStream);
            apiKey = props.getProperty("indico_key");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (apiKey == null) {
            Log.e("Indico Testing", "ApiKey was not provided in local.properties");
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getInstrumentation().getTargetContext().getResources(), R.drawable.test_image);
        image_example = BitmapUtils.toBase64(bitmap);
    }


    public void test_fer_localized() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.fer.predict(image_example, new HashMap<String, Object>() {{
            put("detect", true);
        }}, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                assertTrue(result.getLocalizedFer().size() > 0);
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_fer() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.fer.predict(image_example, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                assertTrue(result.getFer().size() == FacialEmotion.values().length);
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_facialFeatures() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.facialFeatures.predict(image_example, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                assertTrue(result.getFacialFeatures().size() == 48);
                signal.countDown();
            }
        });
        signal.await();
    }
    public void test_imageFeatures() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.imageFeatures.predict(image_example, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                assertTrue(result.getImageFeatures().size() == 2048);
                signal.countDown();
            }
        });
        signal.await();
    }
    public void test_contentFiltering() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.contentFiltering.predict(image_example, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                assertTrue(result.getContentFiltering() <= 1);
                assertTrue(result.getContentFiltering() >= 0);
                signal.countDown();
            }
        });
        signal.await();
    }
    public void test_facialLocalization() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.facialLocalization.predict(image_example, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                assertTrue(result.getFacialLocalization().size() > 0);
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_image_recognition() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.imageRecognition.predict(image_example, new HashMap<String, Object>() {{
            put("top_n", 3);
        }}, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                assertTrue(result.getImageRecognition().size() == 3);
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_image() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.image.predict(image_example, new HashMap<String, Object>() {{
            put("apis", new Api[]{ Api.FER, Api.FacialFeatures });
        }}, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                assertTrue(result.getFacialFeatures().size() == 48);
                assertTrue(result.getFer().size() == FacialEmotion.values().length);
                signal.countDown();
            }
        });
        signal.await();
    }
}