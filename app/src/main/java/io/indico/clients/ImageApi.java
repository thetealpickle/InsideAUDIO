package io.indico.clients;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.indico.api.Api;
import io.indico.network.IndicoCallback;
import io.indico.results.BatchIndicoResult;
import io.indico.results.IndicoResult;
import io.indico.utils.BitmapUtils;
import io.indico.utils.IndicoException;

/**
 * Created by Chris on 6/26/15.
 */
public class ImageApi extends ApiClient {
    Api api;
    boolean minResize;

    public ImageApi(Context context, Api api, String apiKey) {
        super(context, apiKey);
        assert context != null;
        this.api = api;
        this.context = context;
        this.minResize = (boolean) api.get("minResize");
    }


    public void predict(Bitmap image, HashMap<String, Object> params, IndicoCallback<IndicoResult> callback)
        throws UnsupportedOperationException, IOException, IndicoException {
        int size = api.getSize(params);
        predict(BitmapUtils.toBase64(BitmapUtils.resize(image, size, minResize)), params, callback);
    }

    public void predict(Uri image, HashMap<String, Object> params, IndicoCallback<IndicoResult> callback)
        throws UnsupportedOperationException, IndicoException, IOException {
        int size = api.getSize(params);
        predict(BitmapUtils.loadScaledBitmap(context, image, size, size, minResize), params, callback);
    }

    public void predict(Uri image, IndicoCallback<IndicoResult> callback)
        throws UnsupportedOperationException, IndicoException, IOException {
        int size = api.getSize(null);
        predict(BitmapUtils.loadScaledBitmap(context, image, size, size, minResize), null, callback);
    }

    public void predict(Bitmap image, IndicoCallback<IndicoResult> callback)
        throws UnsupportedOperationException, IOException, IndicoException {
        int size = api.getSize(null);
        predict(BitmapUtils.resize(image, size, minResize), null, callback);
    }

    public void predict(String image, IndicoCallback<IndicoResult> callback)
        throws UnsupportedOperationException, IOException, IndicoException {
        predict(image, null, callback);
    }

    public void predict(String image, HashMap<String, Object> params, final IndicoCallback<IndicoResult> callback)
        throws UnsupportedOperationException, IOException, IndicoException {
        call(api, image, false, params, new IndicoCallback<Object>() {
            @Override public void handle(Object result) throws IndicoException {
                callback.handle(new IndicoResult(api, result));
            }
        });
    }

    public void predict(List<?> images, IndicoCallback<BatchIndicoResult> callback)
        throws UnsupportedOperationException, IndicoException, IOException {
        predict(images, null, callback);
    }

    public void predict(List<?> images, Map<String, Object> params, final IndicoCallback<BatchIndicoResult> callback)
        throws UnsupportedOperationException, IndicoException, IOException {
        List<String> bitmaps = new ArrayList<>(images.size());
        int size = api.getSize(null);
        for (Object uri : images) {
            if (uri instanceof Uri)
                bitmaps.add(BitmapUtils.loadScaledBitmap(context, (Uri) uri, size, size, minResize));
            else if (uri instanceof Bitmap)
                bitmaps.add(BitmapUtils.toBase64(BitmapUtils.resize((Bitmap) uri, size)));
            else if (uri instanceof String)
                bitmaps.add((String) uri);
            else
                throw new IndicoException("Image api only allows inputs as Bitmap or Uri");
        }
        call(api, bitmaps, true, params, new IndicoCallback<Object>() {
            @Override public void handle(Object result) throws IndicoException {
                callback.handle(new BatchIndicoResult(api, result));
            }
        });
    }
}
