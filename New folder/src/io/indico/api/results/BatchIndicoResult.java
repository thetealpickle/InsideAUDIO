package io.indico.api.results;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.indico.api.Api;
import io.indico.api.image.FacialEmotion;
import io.indico.api.text.Category;
import io.indico.api.text.Language;
import io.indico.api.text.PoliticalClass;
import io.indico.api.text.TextTag;
import io.indico.api.utils.EnumParser;
import io.indico.api.utils.ImageUtils;
import io.indico.api.utils.IndicoException;

/**
 * Created by Chris on 6/23/15.
 */
public class BatchIndicoResult {
    Map<Api, Object> results;

    @SuppressWarnings("unchecked")
    public BatchIndicoResult(Api api, Map<String, ?> response) throws IndicoException {
        this.results = new HashMap<>();
        if (api != Api.MultiImage && api != Api.MultiText)
            results.put(api, response.get("results"));
        else {
            if (response.containsKey("error")) {
                throw new IndicoException(api + " failed with error " + response.get("error"));
            }
            Map<String, ?> responses = (Map<String, ?>) response.get("results");
            for (Api res : Api.values()) {
                if (!responses.containsKey(res.toString()))
                    continue;
                Map<String, ?> apiResponse = (Map<String, ?>) responses.get(res.toString());
                if (apiResponse.containsKey("error"))
                    throw new IndicoException(res + " failed with error " + apiResponse.get("error"));
                results.put(res, apiResponse.get("results"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Double> getSentiment() throws IndicoException {
        return (List<Double>) get(Api.Sentiment);
    }

    @SuppressWarnings("unchecked")
    public List<Double> getSentimentHQ() throws IndicoException {
        return (List<Double>) get(Api.SentimentHQ);
    }

    @SuppressWarnings("unchecked")
    public List<Map<PoliticalClass, Double>> getPolitical() throws IndicoException {
        return EnumParser.parse(PoliticalClass.class, ((List<Map<String, Double>>) get(Api.Political)));
    }

    @SuppressWarnings("unchecked")
    public List<Map<Language, Double>> getLanguage() throws IndicoException {
        return EnumParser.parse(Language.class, ((List<Map<String, Double>>) get(Api.Language)));
    }

    @SuppressWarnings("unchecked")
    public List<Map<TextTag, Double>> getTextTags() throws IndicoException {
        return EnumParser.parse(TextTag.class, ((List<Map<String, Double>>) get(Api.TextTags)));
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Map<Category, Double>>> getNamedEntities() throws IndicoException {
        List<Map<String, Map<Category, Double>>> result = new ArrayList<>();

        List<Map<String, Map<String, Object>>> responses = (List<Map<String, Map<String, Object>>>) get(Api.NamedEntities);
        for (Map<String, Map<String, Object>> response : responses) {
            Map<String, Map<Category, Double>> each = new HashMap<>();
            for (Map.Entry<String, Map<String, Object>> entry : response.entrySet()) {
                Map<String, Double> res = new HashMap<>();

                res.putAll((Map<String, Double>) entry.getValue().remove("categories"));
                res.put("confidence", (Double) entry.getValue().get("confidence"));
                each.put(entry.getKey(), EnumParser.parse(Category.class, res));
            }
            result.add(each);
        }

        return result;
    }


    @SuppressWarnings("unchecked")
    public List<Map<FacialEmotion, Double>> getFer() throws IndicoException {
        return EnumParser.parse(FacialEmotion.class, ((List<Map<String, Double>>) get(Api.FER)));
    }

    @SuppressWarnings("unchecked")
    public List<Map<Rectangle, Map<FacialEmotion, Double>>> getLocalizedFer() throws IndicoException {
        List<Map<Rectangle, Map<FacialEmotion, Double>>> ret = new ArrayList<>();

        try {
            List<List<Map<String, Object>>> result = (List<List<Map<String, Object>>>) get(Api.FER);
            for (List<Map<String, Object>> res : result) {
                Map<Rectangle, Map<FacialEmotion, Double>> parsed = new HashMap<>();
                for (Map<String, Object> each : res) {
                    parsed.put(ImageUtils.getRectangle(
                            (Map<String, List<Double>>) each.get("location")),
                        EnumParser.parse(FacialEmotion.class, (Map<String, Double>) each.get("emotions"))
                    );
                }
                ret.add(parsed);
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    public List<List<Double>> getImageFeatures() throws IndicoException {
        return (List<List<Double>>) get(Api.ImageFeatures);
    }

    @SuppressWarnings("unchecked")
    public List<List<Double>> getFacialFeatures() throws IndicoException {
        return (List<List<Double>>) get(Api.FacialFeatures);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Double>> getKeywords() throws IndicoException {
        return (List<Map<String, Double>>) get(Api.Keywords);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Double>> getImageRecognition() throws IndicoException {
        return (List<Map<String, Double>>) get(Api.ImageRecognition);
    }

    @SuppressWarnings("unchecked")
    public List<Double> getContentFiltering() throws IndicoException {
        return (List<Double>) get(Api.ContentFiltering);
    }

    @SuppressWarnings("unchecked")
    public List<Double> getTwitterEngagement() throws IndicoException {
        return (List<Double>) get(Api.TwitterEngagement);
    }

    @SuppressWarnings("unchecked")
    public List<List<Rectangle>> getFacialLocalization() throws IndicoException {
        List<List<Rectangle>> images = new ArrayList<>();

        for (List<Map<String, List<Double>>> each : (List<List<Map<String, List<Double>>>>) get(Api.FacialLocalization)) {
            List<Rectangle> rectangles = new ArrayList<>();
            for (Map<String, List<Double>> face : each) {
                rectangles.add(ImageUtils.getRectangle(face));
            }
            images.add(rectangles);
        }

        return images;
    }


    @SuppressWarnings("unchecked")
    public Map<String, Map<String, Map<String, Double>>> getIntersections() throws IndicoException {
        return (Map<String, Map<String, Map<String, Double>>>) get(Api.Intersections);
    }

    private Object get(Api api) throws IndicoException {
        if (!results.containsKey(api))
            throw new IndicoException(api.toString() + " was not included in the request");
        return results.get(api);
    }
}

