package ru.badrudin.api;

import java.net.MalformedURLException;
import java.net.URL;

public class RequestBuilder {
    static public URL buildGetEngines() throws MalformedURLException {
        return new URL(ApiFields.baseURL + ApiFields.ENGINES + ApiFields.jsonFormat);
    }

    static public URL buildGetMarkets(String engine) throws MalformedURLException {
        return new URL(ApiFields.baseURL + ApiFields.ENGINES + "/" + engine + ApiFields.MARKETS + ApiFields.jsonFormat);
    }

    static public URL buildGetBoards(String engine, String market) throws MalformedURLException {
        return new URL(ApiFields.baseURL + ApiFields.ENGINES + "/" + engine + ApiFields.MARKETS + "/" + market + ApiFields.BOARDS + ApiFields.jsonFormat);
    }

    static public URL buildGetPrice(String engine, String market, String board) throws MalformedURLException {
        return new URL(ApiFields.baseURL + ApiFields.ENGINES + "/" + engine + ApiFields.MARKETS + "/" + market + ApiFields.BOARDS + "/" + board + "/securities" + ApiFields.jsonFormat + "?iss.meta=off&iss.only=marketdata&marketdata.columns=SECID,LAST");
    }

    static public URL buildGetLotSize(String engine, String market, String board) throws MalformedURLException {
        return new URL(ApiFields.baseURL + ApiFields.ENGINES + "/" + engine + ApiFields.MARKETS + "/" + market + ApiFields.BOARDS + "/" + board + "/securities" + ApiFields.jsonFormat + "?iss.meta=off&iss.only=securities&securities.columns=SECID,LOTSIZE");
    }

    static public URL buildGetShortName(String engine, String market, String board) throws MalformedURLException {
        return new URL(ApiFields.baseURL + ApiFields.ENGINES + "/" + engine + ApiFields.MARKETS + "/" + market + ApiFields.BOARDS + "/" + board + "/securities" + ApiFields.jsonFormat + "?iss.meta=off&iss.only=securities&securities.columns=SECID,SHORTNAME");
    }

    static public URL buildGetFullName(String engine, String market, String board) throws MalformedURLException {
        return new URL(ApiFields.baseURL + ApiFields.ENGINES + "/" + engine + ApiFields.MARKETS + "/" + market + ApiFields.BOARDS + "/" + board + "/securities" + ApiFields.jsonFormat + "?iss.meta=off&iss.only=securities&securities.columns=SECID,SECNAME");
    }
}