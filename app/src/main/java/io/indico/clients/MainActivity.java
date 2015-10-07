package io.indico.clients;

import android.app.Activity;

import io.indico.Indico;
import io.indico.network.IndicoCallback;
import io.indico.results.IndicoResult;
import io.indico.utils.IndicoException;
import android.os.Bundle;
import io.indico.R;
import android.util.Log;

import java.io.IOException;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Indico.init(this, getString(R.string.indico_api_key), null);

        try {
            Indico.sentiment.predict("indico is so easy to use!", new IndicoCallback<IndicoResult>() {
                @Override public void handle(IndicoResult result) throws IndicoException {
                    Log.i("Indico Sentiment", "sentiment of: " + result.getSentiment());
                }
            });
        } catch (IOException | IndicoException e) {
            e.printStackTrace();
        }
    }
}
