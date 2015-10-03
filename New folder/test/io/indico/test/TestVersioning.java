package io.indico.test;

import org.junit.Test;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.indico.Indico;
import io.indico.api.image.FacialEmotion;
import io.indico.api.utils.IndicoException;

import static org.junit.Assert.assertTrue;

/**
 * Created by Chris on 9/1/15.
 */
public class TestVersioning {
    @Test
    public void testWithVersion() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));

        File lena = new File("bin/lena.png");

        Map<Rectangle, Map<FacialEmotion, Double>> results = test.fer.predict(lena, new HashMap<String, Object>() {{
            put("detect", true);
            put("version", 1);
        }}).getLocalizedFer();

        assertTrue(results.size() == 1);
    }

    @Test
    public void testImageFeaturesVersion2() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));

        File lena = new File("bin/lena.png");

        List<Double> results = test.imageFeatures.predict(lena, new HashMap<String, Object>() {{
            put("detect", true);
            put("version", 2);
        }}).getImageFeatures();

        assertTrue(results.size() == 4096);
    }
}
