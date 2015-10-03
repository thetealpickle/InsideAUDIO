package io.indico.test;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.indico.Indico;
import io.indico.api.utils.IndicoException;

import static org.junit.Assert.assertTrue;

/**
 * Created by Chris on 9/3/15.
 */
public class TestImageRecognition {
    @Test
    public void testSingle() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));

        File lena = new File("bin/lena.png");

        Map<String, Double> results = test.imageRecognition.predict(lena, new HashMap<String, Object>() {{
            put("top_n", 3);
        }}).getImageRecognition();

        assertTrue(results.size() == 3);
    }

    @Test
    public void testBatch() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));

        File lena = new File("bin/lena.png");

        List<Map<String, Double>> results = test.imageRecognition.predict(new File[] {lena, lena}, new HashMap<String, Object>() {{
            put("top_n", 3);
        }}).getImageRecognition();

        assertTrue(results.size() == 2);
        assertTrue(results.get(0).size() == 3);
    }
}
