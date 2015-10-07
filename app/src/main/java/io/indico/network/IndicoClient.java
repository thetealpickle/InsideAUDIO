package io.indico.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Chris on 6/10/15.
 */
public class IndicoClient {
    public static IndicoService api;

    public static void init(String cloud) {
        String address = (cloud == null || cloud.isEmpty()) ?
            "https://apiv2.indico.io" :
            "https://" + cloud + ".indico.domains";

        Gson gson = new GsonBuilder()
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setEndpoint(address)
            .setConverter(new GsonConverter(gson))
            .build();

        api = restAdapter.create(IndicoService.class);
    }
}
