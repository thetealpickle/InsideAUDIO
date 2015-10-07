# IndicoIo-Android

An Android SDK for the [indico API](http://indico.io).
The indico API is free to use, and no training data is required.

Full Documentation + API Keys + Setup
----------------
For API key registration and setup and docs, checkout our [quickstart guide](https://indico.io/docs).

Supported APIs:
------------

- Positive/Negative Sentiment Analysis
- Political Sentiment Analysis
- Keywords Analysis
- Named Entities Recognition
- Image Feature Extraction
- Image Recognition
- Content Filtering Analysis
- Facial Emotion Recognition
- Facial Feature Extraction
- Language Detection
- Text Topic Tagging

Installation:
------------
Gradle - build.gradle file:
```groovy
compile 'io.indico:android:1.0.+'
```

Maven - latest jar:
```xml
<dependency>
  <groupId>io.indico</groupId>
  <artifactId>android</artifactId>
  <version>1.0.0</version>
</dependency>
```

Maven: 

Example:
------------
```java
import io.indico.Indico;
import io.indico.network.IndicoCallback;
import io.indico.results.IndicoResult;
import io.indico.utils.IndicoException;

public class MainActivity extends AppCompatActivity {
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
```
