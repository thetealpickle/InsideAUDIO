package io.indico;

import android.content.Context;

import io.indico.api.Api;
import io.indico.clients.ImageApi;
import io.indico.clients.TextApi;
import io.indico.network.IndicoClient;

/**
 * Created by Chris on 8/13/15.
 */
public class Indico {
    public static TextApi sentiment, sentimentHQ, political, language, textTags, keywords, namedEntities, twitterEngagement, intersections, text;
    public static ImageApi fer, facialFeatures, imageFeatures, imageRecognition, contentFiltering, facialLocalization, image;

    public static Indico init(Context context, String apiKey, String cloud) {
        Indico indico = new Indico();
        IndicoClient.init(cloud);

        sentiment = new TextApi(context, Api.Sentiment, apiKey);
        sentimentHQ = new TextApi(context, Api.SentimentHQ, apiKey);
        political = new TextApi(context, Api.Political, apiKey);
        language = new TextApi(context, Api.Language, apiKey);
        textTags = new TextApi(context, Api.TextTags, apiKey);
        namedEntities = new TextApi(context, Api.NamedEntities, apiKey);
        text = new TextApi(context, Api.MultiText, apiKey);
        intersections = new TextApi(context, Api.Intersections, apiKey);
        keywords = new TextApi(context, Api.Keywords, apiKey);
        twitterEngagement = new TextApi(context, Api.TwitterEngagement, apiKey);

        fer = new ImageApi(context, Api.FER, apiKey);
        facialFeatures = new ImageApi(context, Api.FacialFeatures, apiKey);
        imageFeatures = new ImageApi(context, Api.ImageFeatures, apiKey);
        imageRecognition = new ImageApi(context, Api.ImageRecognition, apiKey);
        contentFiltering = new ImageApi(context, Api.ContentFiltering, apiKey);
        facialLocalization = new ImageApi(context, Api.FacialLocalization, apiKey);
        image = new ImageApi(context, Api.MultiImage, apiKey);

        return indico;
    }
}
