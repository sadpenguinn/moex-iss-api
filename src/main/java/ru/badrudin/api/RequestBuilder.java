package ru.badrudin.api;

import java.net.MalformedURLException;
import java.net.URL;

public class RequestBuilder {
    public URL buildGetEngines() throws MalformedURLException {
        return new URL(ApiFields.baseURL + ApiFields.ENGINES + ApiFields.jsonFormat);
    }

    public URL buildGetMarkets(String engine) throws MalformedURLException {
        return new URL(ApiFields.baseURL + ApiFields.ENGINES + "/" + engine + ApiFields.MARKETS + ApiFields.jsonFormat);
    }

    public URL buildGetBoards(String engine, String market) throws MalformedURLException {
        return new URL(ApiFields.baseURL + ApiFields.ENGINES + "/" + engine + ApiFields.MARKETS + "/" + market + ApiFields.BOARDS + ApiFields.jsonFormat);
    }

    public URL buildGetPrice(String engine, String market, String board) throws MalformedURLException {
        return new URL(ApiFields.baseURL + ApiFields.ENGINES + "/" + engine + ApiFields.MARKETS + "/" + market + ApiFields.BOARDS + "/" + board + "/securities" + ApiFields.jsonFormat + "?iss.meta=off&iss.only=marketdata&marketdata.columns=SECID,LAST");
    }
}