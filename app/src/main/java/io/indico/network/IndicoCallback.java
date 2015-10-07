package io.indico.network;

import android.util.Log;

import io.indico.utils.IndicoException;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Chris on 6/13/15.
 */
public abstract class IndicoCallback<T> implements Callback<T> {
    @Override
    public void success(T t, Response response) {
        Log.w("Indico Retrofit Data", response != null ? response.getBody().toString() : "WARNING: Response was null");
        try {
            handle(t);
        } catch (IndicoException e) {
            Log.e("Indico Retrofit Error", "Please see stack trace below");
            e.printStackTrace();
        }
    }

    @Override
    public void failure(RetrofitError error) {
        try {
            Log.e("Retrofit Failed", error.getUrl());
            Log.e("         Failed", "\t" + (error.getBody() != null ? error.getBody().toString() : " body was null"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        error.printStackTrace();
    }

    public abstract void handle(T result) throws IndicoException;
}