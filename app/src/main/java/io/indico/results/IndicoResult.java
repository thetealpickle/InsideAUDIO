package io.indico.results;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.indico.api.Api;
import io.indico.api.ApiType;
import io.indico.enums.Category;
import io.indico.enums.FacialEmotion;
import io.indico.enums.Language;
import io.indico.enums.Political;
import io.indico.enums.TextTag;
import io.indico.utils.BitmapUtils;
import io.indico.utils.EnumParser;
import io.indico.utils.IndicoException;

/**
 * Created by Chris on 6/22/15.
 */
public class IndicoResult {
    Map<Api, Object> results;

    @SuppressWarnings("unchecked")
    public IndicoResult(Api api, Object response) throws IndicoException {
        this.results = new HashMap<>();
        if (api.type != ApiType.Multi)
            results.put(api, response);
        else {
            Map<String, ?> responses = (Map<String, ?>) response;
            for (Api res : Api.values()) {
                Map<String, ?> apiResponse = (Map<String, ?>) responses.get(res.toString());
                if (apiResponse == null)
                    continue;
                if (apiResponse.containsKey("error"))
                    throw new IndicoException(api + " failed with error " + apiResponse.get("error"));
                results.put(res, apiResponse.get("results"));
            }
        }
    }

    public Double getSentiment() throws IndicoException {
        return (Double) get(Api.Sentiment);
    }

    private Object get(Api api) throws IndicoException {
        if (!results.containsKey(api))
            throw new IndicoException(api.toString() + " was not included in the request");
        return results.get(api);
    }

    public Double getSentimentHQ() throws IndicoException {
        return (Double) get(Api.SentimentHQ);
    }

    @SuppressWarnings("unchecked")
    public Map<Political, Double> getPolitical() throws IndicoException {
        return EnumParser.parse(Political.class, (Map<String, Double>) get(Api.Political));
    }

    @SuppressWarnings("unchecked")
    public Map<Language, Double> getLanguage() throws IndicoException {
        return EnumParser.parse(Language.class, (Map<String, Double>) get(Api.Language));
    }

    @SuppressWarnings("unchecked")
    public Map<TextTag, Double> getTextTags() throws IndicoException {
        return EnumParser.parse(TextTag.class, (Map<String, Double>) get(Api.TextTags));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Map<Category, Double>> getNamedEntities() throws IndicoException {
        Map<String, Map<Category, Double>> result = new HashMap<>();
        Map<String, Map<String, Object>> response = (Map<String, Map<String, Object>>) get(Api.NamedEntities);
        for (Map.Entry<String, Map<String, Object>> entry : response.entrySet()) {
            Map<String, Double> res = new HashMap<>();

            res.putAll((Map<String, Double>) entry.getValue().remove("categories"));
            res.put("confidence", (Double) entry.getValue().get("confidence"));
            result.put(entry.getKey(), EnumParser.parse(Category.class, res));
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public Map<FacialEmotion, Double> getFer() throws IndicoException {
        return EnumParser.parse(FacialEmotion.class, (Map<String, Double>) get(Api.FER));
    }

    @SuppressWarnings("unchecked")
    public Map<Rect, Map<FacialEmotion, Double>> getLocalizedFer() throws IndicoException {
        Map<Rect, Map<FacialEmotion, Double>> ret = new HashMap<>();

        try {
            List<Map<String, Object>> result = (List<Map<String, Object>>) get(Api.FER);
            for (Map<String, Object> res : result) {
                ret.put(BitmapUtils.getRectangle((Map<String, List<Double>>) res.get("location")),
                    EnumParser.parse(FacialEmotion.class, (Map<String, Double>) res.get("emotions"))
                );
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    public List<Double> getImageFeatures() throws IndicoException {
        return (List<Double>) get(Api.ImageFeatures);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Double> getImageRecognition() throws IndicoException {
        return (Map<String, Double>) get(Api.ImageRecognition);
    }

    @SuppressWarnings("unchecked")
    public List<Double> getFacialFeatures() throws IndicoException {
        return (List<Double>) get(Api.FacialFeatures);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Double> getKeywords() throws IndicoException {
        return (Map<String, Double>) get(Api.Keywords);
    }

    @SuppressWarnings("unchecked")
    public Double getContentFiltering() throws IndicoException {
        return (Double) get(Api.ContentFiltering);
    }

    @SuppressWarnings("unchecked")
    public Double getTwitterEngagement() throws IndicoException {
        return (Double) get(Api.TwitterEngagement);
    }

    @SuppressWarnings("unchecked")
    public List<Rect> getFacialLocalization() throws IndicoException {
        List<Rect> rectangles = new ArrayList<>();
        for (Map<String, List<Double>> each : (List<Map<String, List<Double>>>) get(Api.FacialLocalization)) {
            rectangles.add(BitmapUtils.getRectangle(each));
        }

        return rectangles;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Map<String, Map<String, Double>>>> getIntersections() throws IndicoException {
        throw new IndicoException("Intersections Api should be called with more than 1 example as a Batch Request");
    }
}
