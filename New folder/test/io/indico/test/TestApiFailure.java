package io.indico.test;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import io.indico.Indico;
import io.indico.api.Api;
import io.indico.api.results.BatchIndicoResult;
import io.indico.api.results.IndicoResult;
import io.indico.api.utils.IndicoException;

public class TestApiFailure {

    @Test(expected = IndicoException.class)
    public void testBadCall() throws UnsupportedOperationException, IOException, IndicoException {
        Indico test = new Indico("notanapikey");
        test.sentiment.predict("this is going to error!");
    }

    @Test(expected = IndicoException.class)
    public void testNoApiKey() throws UnsupportedOperationException, IOException, IndicoException {
        String emptyString = null;
        Indico test = new Indico(emptyString);
        test.sentiment.predict("this is going to error!");
    }

    @Test(expected = IndicoException.class)
    public void testBadEmptyApisBatchPredictText() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String[] example = {"this is going to break!", "This is not going to break"};

        test.text.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = -5404754545573764945L;

		{
            put("apis", new Api[0]);
        }});
    }

    @Test(expected = IndicoException.class)
    public void testBadResultsPredictLanguage() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String example = "this is going to break!";

        IndicoResult result = test.text.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = -2033432408136851511L;

		{
            put("apis", new Api[] { Api.Sentiment, Api.SentimentHQ, Api.TextTags, Api.Political });
        }});

        result.getLanguage();

    }
    
    @Test(expected = IndicoException.class)
    public void testBadResultsPredictSentiment() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String example = "this is going to break!";

        IndicoResult result = test.text.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 3642124118514503727L;

		{
            put("apis", new Api[] { Api.Language, Api.SentimentHQ, Api.TextTags, Api.Political });
        }});

        result.getSentiment();

    }
    
    @Test(expected = IndicoException.class)
    public void testBadResultsPredictSentimentHQ() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String example = "this is going to break!";

        IndicoResult result = test.text.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 950238601567614743L;

		{
            put("apis", new Api[] { Api.Sentiment, Api.Language, Api.TextTags, Api.Political });
        }});

        result.getSentimentHQ();

    }

    @Test(expected = IndicoException.class)
    public void testBadResultsPredictTextTags() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String example = "this is going to break!";

        IndicoResult result = test.text.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 5021357321907712351L;

		{
            put("apis", new Api[] { Api.Sentiment, Api.SentimentHQ, Api.Language, Api.Political });
        }});

        result.getTextTags();

    }
    
    @Test(expected = IndicoException.class)
    public void testBadResultsPredictPolitical() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String example = "this is going to break!";

        IndicoResult result = test.text.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 4567831686328242724L;

		{
            put("apis", new Api[] { Api.Sentiment, Api.SentimentHQ, Api.TextTags, Api.Language });
        }});

        result.getPolitical();

    }

    @Test(expected = IndicoException.class)
    public void testBadResultsBatchPredictLanguage() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String[] example = {"this is going to break!", "errr"};

        BatchIndicoResult result = test.text.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 6791392709149977087L;

		{
            put("apis", new Api[] { Api.Sentiment, Api.SentimentHQ, Api.TextTags, Api.Political });
        }});

        result.getLanguage();

    }
    
    @Test(expected = IndicoException.class)
    public void testBadResultsBatchPredictPolitical() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String[] example = {"this is going to break!", "errr"};

        BatchIndicoResult result = test.text.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 1942846454875138387L;

		{
            put("apis", new Api[] { Api.Sentiment, Api.SentimentHQ, Api.TextTags, Api.Language });
        }});

        result.getPolitical();

    }
    
    @Test(expected = IndicoException.class)
    public void testBadResultsBatchPredictSentiment() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String[] example = {"this is going to break!", "errr"};

        BatchIndicoResult result = test.text.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 4006889935546196902L;

		{
            put("apis", new Api[] { Api.Language, Api.SentimentHQ, Api.TextTags, Api.Political });
        }});

        result.getSentiment();

    }
    
    @Test(expected = IndicoException.class)
    public void testBadResultsBatchPredictSentimentHQ() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String[] example = {"this is going to break!", "errr"};

        BatchIndicoResult result = test.text.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = -2247297023901545181L;

		{
            put("apis", new Api[] { Api.Sentiment, Api.Language, Api.TextTags, Api.Political });
        }});

        result.getSentimentHQ();

    }
    
    @Test(expected = IndicoException.class)
    public void testBadResultsBatchPredictTextTags() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String[] example = {"this is going to break!", "errr"};

        BatchIndicoResult result = test.text.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 8096939989630825514L;

		{
            put("apis", new Api[] { Api.Sentiment, Api.SentimentHQ, Api.Language, Api.Political });
        }});

        result.getTextTags();

    }
    
    @Test(expected = IndicoException.class)
    public void testBadEmptyApisBatchPredictImage() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String[] example = {"bin/lena.png", "bin/lena.png"};

        test.image.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = -7202631630922829763L;

		{
            put("apis", new Api[0]);
        }});
    }

    @Test(expected = IndicoException.class)
    public void testBadApisBatchPredictImage() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String[] example = {"bin/lena.png", "bin/lena.png"};

        test.image.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 3330966369341206639L;

		{
            put("apis", new Api[] { Api.Sentiment });
        }});
    }

    @Test(expected = IndicoException.class)
    public void testBadResultsPredictImageFeatures() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String example = "bin/lena.png";

        IndicoResult result = test.image.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 7478113187774985493L;

		{
            put("apis", new Api[] { Api.FER, Api.FacialFeatures });
        }});

        result.getImageFeatures();
    }

    @Test(expected = IndicoException.class)
    public void testBadResultsPredictFER() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String example = "bin/lena.png";

        IndicoResult result = test.image.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 1244714569676887657L;

		{
            put("apis", new Api[] { Api.ImageFeatures, Api.FacialFeatures });
        }});

        result.getFer();
    }
    
    @Test(expected = IndicoException.class)
    public void testBadResultsPredictFacialFeatures() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String example = "bin/lena.png";

        IndicoResult result = test.image.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = -6552301190181211769L;

		{
            put("apis", new Api[] { Api.ImageFeatures, Api.FER });
        }});

        result.getFacialFeatures();
    }

    @Test(expected = IndicoException.class)
    public void testBadResultsBatchPredictImageFeatures() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String[] example = {"bin/lena.png", "bin/lena.png"};

        BatchIndicoResult result = test.image.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = -2511717095245136380L;

		{
            put("apis", new Api[] { Api.FER, Api.FacialFeatures });
        }});

        result.getImageFeatures();
    }

    @Test(expected = IndicoException.class)
    public void testBadResultsBatchPredictFER() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String[] example = {"bin/lena.png", "bin/lena.png"};

        BatchIndicoResult result = test.image.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = -2511717095245136380L;

		{
            put("apis", new Api[] { Api.ImageFeatures, Api.FacialFeatures });
        }});

        result.getFer();
    }
    
    @Test(expected = IndicoException.class)
    public void testBadResultsBatchPredictFacialFeatures() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String[] example = {"bin/lena.png", "bin/lena.png"};

        BatchIndicoResult result = test.image.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = -2511717095245136380L;

		{
            put("apis", new Api[] { Api.ImageFeatures, Api.FER });
        }});

        result.getFacialFeatures();
    }
}
