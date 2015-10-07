package io.indico.clients;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.indico.api.Api;
import io.indico.api.ApiType;
import io.indico.network.IndicoCallback;
import io.indico.network.IndicoClient;
import io.indico.utils.IndicoException;

public class ApiClient {
    Context context;
    String apiKey;

    ApiClient(Context context, String apiKey) {
        this.context = context;
        this.apiKey = apiKey;
    }


    void call(final Api api, Object data, boolean batch, Map<String, Object> extraParams, final IndicoCallback<Object> callback)
        throws UnsupportedOperationException, IOException, IndicoException {
        Map<String, Object> postData = new HashMap<>();

        // Data input checking
        if (data instanceof List) {
            Type dataType = new TypeToken<List<String>>() {}.getType();
            postData.put("data", new Gson().toJsonTree(data, dataType));
        } else if (data instanceof Map){
            Type dataType = new TypeToken<Map<String, Object>>() {}.getType();
            postData.put("data", new Gson().toJsonTree(data, dataType));
        } else {
            postData.put("data", data);
        }

        Integer version = null;
        if (extraParams != null) {
            version = extraParams.containsKey("version") ? (Integer) extraParams.get("version") : null;
            postData.putAll(extraParams);
        }

        // Build the callback
        IndicoCallback<Map<String, Object>> parent_callback = new IndicoCallback<Map<String, Object>>() {
            @Override public void handle(Map<String, Object> result) throws IndicoException {
                if (!result.containsKey("results")) {
                    Log.e("API Exception",
                        api.name()
                            + " " + result.get("error")
                    );
                }

                callback.handle(result.get("results"));
            }
        };

        if (api.type == ApiType.Multi) {
            if (!postData.containsKey("apis")) {
                throw new IndicoException("Api requests that involve multi apis must include apis argument");
            }
            Api[] apis = ((Api[]) postData.remove("apis"));
            StringBuilder sb = new StringBuilder();
            for (Api _api : apis) {
                sb.append(_api.toString());
                sb.append(",");
            }
            if (batch)
                IndicoClient.api.multi_batch_call(api.toString(), postData, sb.substring(0, sb.length() - 1), apiKey, parent_callback);
            else
                IndicoClient.api.multi_call(api.toString(), postData, sb.substring(0, sb.length() - 1), apiKey, parent_callback);
        } else {
            if (batch)
                IndicoClient.api.batch_call(api.toString(), postData, apiKey, version, parent_callback);
            else
                IndicoClient.api.call(api.toString(), postData, apiKey, version, parent_callback);
        }
    }
}
