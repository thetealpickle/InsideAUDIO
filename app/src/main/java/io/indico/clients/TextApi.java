package io.indico.clients;

import android.content.Context;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.indico.Indico;
import io.indico.api.Api;
import io.indico.network.IndicoCallback;
import io.indico.results.BatchIndicoResult;
import io.indico.results.IndicoResult;
import io.indico.utils.IndicoException;

/**
 * Created by Chris on 6/26/15.
 */
public class TextApi extends ApiClient {
    Api api;

    public TextApi(Context context, Api api, String apiKey) {
        super(context, apiKey);
        this.api = api;
    }

    public void predict(String data, IndicoCallback<IndicoResult> callback)
        throws UnsupportedOperationException, IOException, IndicoException {
        predict(data, null, callback);
    }

    public void predict(String data, Map<String, Object> params, final IndicoCallback<IndicoResult> callback)
        throws UnsupportedOperationException, IOException, IndicoException {
        call(api, data, false, params, new IndicoCallback<Object>() {
            @Override public void handle(Object result) throws IndicoException {
                callback.handle(new IndicoResult(api, result));
            }
        });
    }

    public void predict(List<String> data, IndicoCallback<BatchIndicoResult> callback)
        throws IOException, IndicoException {
        predict(data, null, callback);
    }

    public void predict(String[] data, IndicoCallback<BatchIndicoResult> callback)
        throws IOException, IndicoException {
        predict(Arrays.asList(data), null, callback);
    }

    public void predict(String[] data, Map<String, Object> params, IndicoCallback<BatchIndicoResult> callback)
        throws IOException, IndicoException {
        predict(Arrays.asList(data), params, callback);
    }

    public void predict(Object data, IndicoCallback callback) throws IOException, IndicoException {
        predict(data, null, callback);
    }

    public void predict(Object data, Map<String, Object> params, IndicoCallback callback) throws IOException, IndicoException {
        call(api, data, false, params, callback);
    }

    public void predict(Map<String, Object> data, Map<String, Object> params, final IndicoCallback<BatchIndicoResult> callback) throws IOException, IndicoException {
        call(api, data, true, params, new IndicoCallback<Object>() {
            @Override public void handle(Object result) throws IndicoException {
                callback.handle(new BatchIndicoResult(api, result));
            }
        });
    }

    public void predict(List<String> data, Map<String, Object> params, final IndicoCallback<BatchIndicoResult> callback)
        throws IOException, IndicoException {
        call(api, data, true, params, new IndicoCallback<Object>() {
            @Override public void handle(Object result) throws IndicoException {
                callback.handle(new BatchIndicoResult(api, result));
            }
        });
    }

}
