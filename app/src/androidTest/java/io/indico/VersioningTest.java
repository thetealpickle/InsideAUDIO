package io.indico;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.InstrumentationTestCase;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import io.indico.network.IndicoCallback;
import io.indico.results.BatchIndicoResult;
import io.indico.results.IndicoResult;
import io.indico.utils.BitmapUtils;
import io.indico.utils.IndicoException;

/**
 * Created by Chris on 9/22/15.
 */
public class VersioningTest  extends InstrumentationTestCase {
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

    public void test_imageFeatures_v2() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.imageFeatures.predict(image_example, new HashMap<String, Object>() {{
            put("version", 2);
        }}, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                assertTrue(result.getImageFeatures().size() == 4096);
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_imageFeatures_v2_batch() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);

        List<String> data = new ArrayList<String>() {{
            add(image_example);
        }};

        Indico.imageFeatures.predict(data, new HashMap<String, Object>() {{
            put("version", 2);
        }}, new IndicoCallback<BatchIndicoResult>() {
            @Override public void handle(BatchIndicoResult result) throws IndicoException {
                assertTrue(result.getImageFeatures().get(0).size() == 4096);
                signal.countDown();
            }
        });
        signal.await();
    }
}
