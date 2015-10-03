package io.indico.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.indico.api.results.BatchIndicoResult;
import io.indico.api.results.IndicoResult;
import io.indico.api.utils.ImageUtils;
import io.indico.api.utils.IndicoException;

/**
 * Created by Chris on 6/26/15.
 */
public class ImageApi extends ApiClient {
    Api api;

    public ImageApi(Api api, String apiKey, String privateCloud) throws IndicoException {
        super(apiKey, privateCloud);
        this.api = api;
    }

    public IndicoResult predict(String filePath, HashMap<String, Object> params)
        throws UnsupportedOperationException, IOException, IndicoException {
        return predict(new File(filePath), params);
    }

    public IndicoResult predict(File imageFile, HashMap<String, Object> params)
        throws UnsupportedOperationException, IOException, IndicoException {
        return predict(ImageUtils.convertToImage(imageFile, api.getSize(params), (Boolean) api.get("minResize")), ImageUtils.grabType(imageFile), params);
    }

    public IndicoResult predict(BufferedImage image, String type, HashMap<String, Object> params)
        throws UnsupportedOperationException, IOException, IndicoException {
        return call(api, image, type, params);
    }

    public BatchIndicoResult predict(List<?> images, HashMap<String, Object> params)
        throws UnsupportedOperationException, IOException, IndicoException {
        return predict(ImageUtils.convertToImage(images, api.getSize(params), (Boolean) api.get("minResize")), ImageUtils.grabType(images), params);
    }

    public BatchIndicoResult predict(List<BufferedImage> images, String type, HashMap<String, Object> params)
        throws UnsupportedOperationException, IOException, IndicoException {
        return call(api, images, type, params);
    }

    public BatchIndicoResult predict(String[] images, HashMap<String, Object> params)
        throws UnsupportedOperationException, IOException, IndicoException {
        return predict(Arrays.asList(images), params);
    }

    public BatchIndicoResult predict(File[] images, HashMap<String, Object> params)
        throws UnsupportedOperationException, IOException, IndicoException {
        return predict(Arrays.asList(images), params);
    }

    public IndicoResult predict(String filePath)
        throws UnsupportedOperationException, IOException, IndicoException {
        return predict(new File(filePath), null);
    }

    public IndicoResult predict(File imageFile)
        throws UnsupportedOperationException, IOException, IndicoException {
        return predict(ImageUtils.convertToImage(imageFile, (Integer) api.get("size"), (Boolean) api.get("minResize")), ImageUtils.grabType(imageFile), null);
    }

    public IndicoResult predict(BufferedImage image, String type)
        throws UnsupportedOperationException, IOException, IndicoException {
        return call(api, image, type, null);
    }

    public BatchIndicoResult predict(List<?> images)
        throws UnsupportedOperationException, IOException, IndicoException {
        return predict(ImageUtils.convertToImage(images, (Integer) api.get("size"), (Boolean) api.get("minResize")), ImageUtils.grabType(images), null);
    }

    public BatchIndicoResult predict(List<BufferedImage> images, String type)
        throws UnsupportedOperationException, IOException, IndicoException {
        return call(api, images, type, null);
    }

    public BatchIndicoResult predict(String[] images)
        throws UnsupportedOperationException, IOException, IndicoException {
        return predict(Arrays.asList(images), null);
    }

    public BatchIndicoResult predict(File[] images)
        throws UnsupportedOperationException, IOException, IndicoException {
        return predict(Arrays.asList(images), null);
    }

}
