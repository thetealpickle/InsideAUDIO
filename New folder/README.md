# IndicoIo-Java

A wrapper for the [indico API](http://indico.io).
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
<b> Method 1 - Installing with Maven</b>

You can install the latest version of the indico module using Maven by including the following in your pom.xml file:
``` xml
<dependency>
    <groupId>io.indico</groupId>
    <artifactId>indico</artifactId>
    <version>3.1.0</version>
</dependency>
```
<b>Method 2 - Use the jar</b>

You can also install indicoio by adding our jar to your project. We release both a "batteries included" jar that includes all of the indico dependencies, and an a-la-carte jar that includes only indico itself.

Both are available for download on maven central, as well as the oss sonatype nexus. Instructions for using a jar in eclipse can be found here).

<b>Method 3 - Downloading from Github</b>

All of our client libraries are open source, so if you want the code itself feel free to grab it from github using the command below.
``` bash
git clone https://github.com/IndicoDataSolutions/IndicoIo-Java.git
```

 

Example:
------------
```java
import io.indico.Indico;
import io.indico.api.IndicoResult;
import io.indico.api.BatchIndicoResult;

// single example
Indico indico = new Indico('YOUR_API_KEY');
IndicoResult single = indico.sentiment.predict(
    "indico is so easy to use!"
);
Double result = single.getSentiment();
System.out.println(result);

// batch example
String[] example = {
    "indico is so easy to use!", 
    "everything is awesome!"
};
BatchIndicoResult multiple = indico.sentiment.predict(example);
List<Double> results = multiple.getSentiment();
System.out.println(results);
```
