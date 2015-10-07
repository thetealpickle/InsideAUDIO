package io.indico;

import android.test.InstrumentationTestCase;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import io.indico.api.Api;
import io.indico.enums.Category;
import io.indico.enums.Language;
import io.indico.enums.Political;
import io.indico.enums.TextTag;
import io.indico.network.IndicoCallback;
import io.indico.results.BatchIndicoResult;
import io.indico.utils.IndicoException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class BatchTextTests extends InstrumentationTestCase {
    String apiKey;
    List<String> text_example = new ArrayList<String>() {{
       add("Indico Data Solutions is absolutely the self proclaimed best startup in Boston");
       add("Indico Data Solutions is absolutely the self proclaimed best startup in Boston");
    }};

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
            fail("ApiKey was not provided in src/main/assets/indico.properties");
        }
    }


    public void test_batch_sentiment() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);

        final CountDownLatch signal = new CountDownLatch(1);
        Indico.sentiment.predict(text_example, new IndicoCallback<BatchIndicoResult>() {
            @Override public void handle(BatchIndicoResult result) throws IndicoException {
                Double score = result.getSentiment().get(0);
                assertTrue(score > 0.5);
                assertTrue(score < 1);
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_batch_sentimentHQ() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.sentimentHQ.predict(text_example, new IndicoCallback<BatchIndicoResult>() {
            @Override public void handle(BatchIndicoResult result) throws IndicoException {
                Double score = result.getSentimentHQ().get(0);
                assertTrue(score > 0.5);
                assertTrue(score < 1);
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_batch_political() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.political.predict(text_example, new IndicoCallback<BatchIndicoResult>() {
            @Override public void handle(BatchIndicoResult result) throws IndicoException {
                assertTrue(result.getPolitical().get(0).size() == Political.values().length);
                signal.countDown();
            }
        });
        signal.await();
    }


    public void test_batch_language() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.language.predict(text_example, new IndicoCallback<BatchIndicoResult>() {
            @Override public void handle(BatchIndicoResult result) throws IndicoException {
                assertTrue(result.getLanguage().get(0).size() == Language.values().length);
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_batch_textTags() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.textTags.predict(text_example, new IndicoCallback<BatchIndicoResult>() {
            @Override public void handle(BatchIndicoResult result) throws IndicoException {
                assertTrue(result.getTextTags().get(0).size() == TextTag.values().length);
                signal.countDown();
            }
        });
        signal.await();
    }


    public void test_batch_namedEntities() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.namedEntities.predict(text_example, new IndicoCallback<BatchIndicoResult>() {
            @Override public void handle(BatchIndicoResult resObj) throws IndicoException {
                Map<String, Map<Category, Double>> result = resObj.getNamedEntities().get(0);
                for (Map.Entry<String, Map<Category, Double>> entry : result.entrySet()) {
                    assertTrue(entry.getValue().keySet().containsAll(Arrays.asList(Category.values())));
                }
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_batch_text() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.text.predict(text_example, new HashMap<String, Object>() {{
            put("apis", new Api[]{Api.Sentiment, Api.Language});
        }}, new IndicoCallback<BatchIndicoResult>() {
            @Override public void handle(BatchIndicoResult result) throws IndicoException {
                assertTrue(result.getSentiment().get(0) > .5);
                assertTrue(result.getLanguage().get(0).size() == Language.values().length);
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_batch_keywords() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.keywords.predict(text_example, new HashMap<String, Object>() {{
            put("language", "detect");
        }}, new IndicoCallback<BatchIndicoResult>() {
            @Override public void handle(BatchIndicoResult result) throws IndicoException {
                Set<String> words = new HashSet<>();
                Collections.addAll(words, text_example.get(0).toLowerCase().split(" "));
                assertTrue(words.containsAll(result.getKeywords().get(0).keySet()));
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_batch_twitterEngagement() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.twitterEngagement.predict(text_example, new IndicoCallback<BatchIndicoResult>() {
            @Override public void handle(BatchIndicoResult result) throws IndicoException {
                Double score = result.getTwitterEngagement().get(0);
                assertTrue(score >= 0);
                assertTrue(score <= 1);
                signal.countDown();
            }
        });
        signal.await();
    }
}