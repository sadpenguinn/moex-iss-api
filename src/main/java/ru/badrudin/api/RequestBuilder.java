package ru.badrudin.api;

import java.net.MalformedURLException;
import java.net.URL;

public class RequestBuilder {
    public URL buildGetEngines() throws MalformedURLException {
        return new URL(ApiFields.baseURL + ApiFields.ENGINE + ApiFields.jsonFormat);
    }
}