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
import io.indico.results.IndicoResult;
import io.indico.utils.IndicoException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class TextTests extends InstrumentationTestCase {
    String apiKey;
    String text_example = "Indico Data Solutions is absolutely the self proclaimed best startup in Boston";

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


    public void test_sentiment() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);

        final CountDownLatch signal = new CountDownLatch(1);
        Indico.sentiment.predict(text_example, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                Double score = result.getSentiment();
                assertTrue(score > 0.5);
                assertTrue(score < 1);
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_sentimentHQ() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.sentimentHQ.predict(text_example, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                Double score = result.getSentimentHQ();
                assertTrue(score > 0.5);
                assertTrue(score < 1);
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_political() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.political.predict(text_example, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                assertTrue(result.getPolitical().size() == Political.values().length);
                signal.countDown();
            }
        });
        signal.await();
    }


    public void test_language() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.language.predict(text_example, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                assertTrue(result.getLanguage().size() == Language.values().length);
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_textTags() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.textTags.predict(text_example, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                assertTrue(result.getTextTags().size() == TextTag.values().length);
                signal.countDown();
            }
        });
        signal.await();
    }


    public void test_namedEntities() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.namedEntities.predict(text_example, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult resObj) throws IndicoException {
                Map<String, Map<Category, Double>> result = resObj.getNamedEntities();
                for (Map.Entry<String, Map<Category, Double>> entry : result.entrySet()) {
                    assertTrue(entry.getValue().keySet().containsAll(Arrays.asList(Category.values())));
                }
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_text() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.text.predict(text_example, new HashMap<String, Object>() {{
            put("apis", new Api[]{Api.Sentiment, Api.Language});
        }}, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                assertTrue(result.getSentiment() > .5);
                assertTrue(result.getLanguage().size() == Language.values().length);
                signal.countDown();
            }
        });
        signal.await();
    }


    public void test_intersections() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);

        List<String> examples = new ArrayList<String>() {{
            add(text_example);
            add("Another example of this is terrible.");
            add("Is this even going to help? Why am I even here.");
        }};

        Indico.intersections.predict(examples, new HashMap<String, Object>() {{
            put("apis", new Api[] { Api.Sentiment, Api.Language });
        }}, new IndicoCallback<BatchIndicoResult>() {
            @Override public void handle(BatchIndicoResult result) throws IndicoException {
                Map<String, Map<String, Map<String, Double>>> results = result.getIntersections();

                assertTrue(results.containsKey(Api.Sentiment.toString()));
                assertTrue(results.get(Api.Sentiment.toString())
                    .containsKey(Language.English.toString()));
                assertTrue(results.get(Api.Sentiment.toString()).get(Language.English.toString())
                    .containsKey("correlation"));
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_intersections_historic() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);

        List<String> examples = new ArrayList<String>() {{
            add(text_example);
            add("Another example of this is terrible.");
            add("Is this even going to help? Why am I even here.");
        }};

        final Map<String, Object> historicData = new HashMap<>();
        Indico.text.predict(examples, new HashMap<String, Object>() {{
            put("apis", new Api[]{ Api.Language, Api.Sentiment });
        }}, new IndicoCallback<BatchIndicoResult>() {
            @Override public void handle(BatchIndicoResult result) throws IndicoException {
                historicData.put(Api.Sentiment.toString(), result.getSentiment());
                historicData.put(Api.Language.toString(), result.getLanguage());
                signal.countDown();
            }
        });
        signal.await();

        final CountDownLatch signal2 = new CountDownLatch(1);
        Indico.intersections.predict(historicData, new HashMap<String, Object>() {{
            put("apis", new Api[]{ Api.Language, Api.Sentiment });
        }}, new IndicoCallback<BatchIndicoResult>() {
            @Override public void handle(BatchIndicoResult result) throws IndicoException {
                Map<String, Map<String, Map<String, Double>>> historicResults = result.getIntersections();
                assertTrue(historicResults.containsKey(Language.English.toString()));
                assertTrue(historicResults.get(Language.English.toString())
                    .containsKey(Api.Sentiment.toString()));
                assertTrue(historicResults.get(Language.English.toString()).get(Api.Sentiment.toString())
                    .containsKey("correlation"));
                signal2.countDown();
            }
        });
        signal2.await();
    }

    public void test_keywords() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.keywords.predict(text_example, new HashMap<String, Object>() {{
            put("language", "detect");
        }}, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                Set<String> words = new HashSet<>();
                Collections.addAll(words, text_example.toLowerCase().split(" "));
                assertTrue(words.containsAll(result.getKeywords().keySet()));
                signal.countDown();
            }
        });
        signal.await();
    }

    public void test_twitterEngagement() throws Exception {
        Indico.init(getInstrumentation().getContext(), apiKey, null);
        final CountDownLatch signal = new CountDownLatch(1);
        Indico.twitterEngagement.predict(text_example, new IndicoCallback<IndicoResult>() {
            @Override public void handle(IndicoResult result) throws IndicoException {
                Double score = result.getTwitterEngagement();
                assertTrue(score >= 0);
                assertTrue(score <= 1);
                signal.countDown();
            }
        });
        signal.await();
    }
}