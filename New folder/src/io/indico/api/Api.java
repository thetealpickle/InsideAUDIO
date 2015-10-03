package io.indico.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 6/22/15.
 */
public enum Api {
    // TEXT APIS
    Sentiment(ApiType.Text, "sentiment"),
    SentimentHQ(ApiType.Text, "sentimenthq"),
    Political(ApiType.Text, "political"),
    Language(ApiType.Text, "language"),
    TextTags(ApiType.Text, "texttags"),
    NamedEntities(ApiType.Text, "namedentities"),
    Keywords(ApiType.Text, "keywords"),
    TwitterEngagement(ApiType.Text, "twitterengagement"),

    // IMAGE APIS
    FER(ApiType.Image, "fer", "size", 64, "minResize", false),
    ImageFeatures(ApiType.Image, "imagefeatures", "size", 144, "minResize", true),
    ImageRecognition(ApiType.Image, "imagerecognition", "size", 144, "minResize", true),
    FacialFeatures(ApiType.Image, "facialfeatures", "size", 64, "minResize", false),
    ContentFiltering(ApiType.Image, "contentfiltering", "size", 128, "minResize", true),
    FacialLocalization(ApiType.Image, "faciallocalization", "size", -1, "minResize", false),

    // MULTI APIS
    Intersections(ApiType.Multi, "intersections", "type", ApiType.Text),
    MultiText(ApiType.Multi, "multiapi", "type", ApiType.Text),
    MultiImage(ApiType.Multi, "multiapi", "type", ApiType.Image, "size", 64, "minResize", false);

    public ApiType type;
    String endpoint;
    Map<String, Object> params;

    Api(ApiType type, String endpoint, Object... params) {
        this.type = type;
        this.endpoint = endpoint;

        assert params.length % 2 == 0;
        List<Object> paramList = Arrays.asList(params);
        this.params = new HashMap<>();
        for (int i = 0; i < paramList.size(); i += 2) {
            this.params.put(
                String.valueOf(paramList.get(i)),
                paramList.get(i + 1)
            );
        }
    }

    @Override
    public String toString() {
        return endpoint;
    }

    public Object get(String key) {
        return params != null ? params.get(key) : null;
    }

    public int getSize(Map<String, Object> userParams) {
        if (this == Api.FER && userParams != null && userParams.get("detect") == true) {
            return -1;
        }

        return (Integer) get("size");
    }
}
