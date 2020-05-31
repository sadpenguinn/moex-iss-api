package ru.badrudin.api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import ru.badrudin.api.model.Board;
import ru.badrudin.api.model.Engine;
import ru.badrudin.api.model.Market;

public class MoexApi {

    private String engineName;
    private String marketName;
    private String boardName;

    private final URLReader urlReader;
    private final JSONReader jsonReader;

    public MoexApi(URLReader urlReader, JSONReader jsonReader) {
        this.urlReader = urlReader;
        this.jsonReader = jsonReader;
    }

    ///*****************************************************************************************************************
    /// API
    ///*****************************************************************************************************************

    public MoexApi setEngine(String engineName) {
        this.engineName = engineName;
        return this;
    }

    public MoexApi setMarket(String marketName) {
        this.marketName = marketName;
        return this;
    }

    public MoexApi setBoard(String boardName) {
        this.boardName = boardName;
        return this;
    }

    /// Securities

    public Long getLotSize(String tickerName) throws MoexApiException {
        try {
            return getData(
                tickerName,
                ApiFields.SECURITIES,
                RequestBuilder.class.getMethod("buildGetLotSize", String.class, String.class, String.class),
                (fields) -> (Long) fields.get(1));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new MoexApiException(e);
        }
    }

    public String getShortName(String tickerName) throws MoexApiException {
        try {
            return getData(
                tickerName,
                ApiFields.SECURITIES,
                RequestBuilder.class.getMethod("buildGetShortName", String.class, String.class, String.class),
                (fields) -> (String) fields.get(1));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new MoexApiException(e);
        }
    }

    public String getFullName(String tickerName) throws MoexApiException {
        try {
            return getData(
                tickerName,
                ApiFields.SECURITIES,
                RequestBuilder.class.getMethod("buildGetFullName", String.class, String.class, String.class),
                (fields) -> (String) fields.get(1));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new MoexApiException(e);
        }
    }

    /// Metadata

    public Double getPrice(String tickerName) throws MoexApiException {

        try {
            return getData(
                tickerName,
                ApiFields.MARKETDATA,
                RequestBuilder.class.getMethod("buildGetPrice", String.class, String.class, String.class),
                (fields) -> {
                    Double price;
                    try {
                        price = (Double) fields.get(1);
                    } catch (ClassCastException e) {
                        Long priceLong = (Long) fields.get(1);
                        price = priceLong.doubleValue();
                    }
                    return price != null ? price : 0;
                });
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new MoexApiException(e);
        }
    }

    public ArrayList<Engine> getEngines() throws MoexApiException {
        ArrayList<Engine> result = new ArrayList<>();
        URL request;
        String read;
        try {
            request = RequestBuilder.buildGetEngines();
            read = urlReader.read(request);

            JSONArray data = jsonReader.readSingleField(read, "engines");
            for (Object datum : data) {
                JSONArray fields = (JSONArray) datum;
                if (fields.size() != 3) {
                    throw new MoexApiException("Invalid syntax");
                }
                result.add(new Engine(
                    (Long) fields.get(0),
                    (String) fields.get(1),
                    (String) fields.get(2)));
            }
        } catch (IOException | ParseException e) {
            throw new MoexApiException(e);
        }
        return result;
    }

    public ArrayList<Market> getMarkets(String engine) throws MoexApiException {
        ArrayList<Market> result = new ArrayList<>();
        URL request;
        String read;
        try {
            request = RequestBuilder.buildGetMarkets(engine);
            read = urlReader.read(request);

            JSONArray data = jsonReader.readSingleField(read, "markets");
            for (Object datum : data) {
                JSONArray fields = (JSONArray) datum;
                if (fields.size() != 3) {
                    throw new MoexApiException("Invalid syntax");
                }
                result.add(new Market(
                    (Long) fields.get(0),
                    (String) fields.get(1),
                    (String) fields.get(2)));
            }
        } catch (IOException | ParseException e) {
            throw new MoexApiException(e);
        }
        return result;
    }

    public ArrayList<Market> getMarkets(Engine engine) throws MoexApiException {
        return getMarkets(engine.name);
    }

    public ArrayList<Board> getBoards(String engine, String market) throws MoexApiException {
        ArrayList<Board> result = new ArrayList<>();
        URL request;
        String read;
        try {
            request = RequestBuilder.buildGetBoards(engine, market);
            read = urlReader.read(request);

            JSONArray data = jsonReader.readSingleField(read, "boards");
            for (Object datum : data) {
                JSONArray fields = (JSONArray) datum;
                if (fields.size() != 5) {
                    throw new MoexApiException("Invalid syntax");
                }
                var isTradedLong = (Long) fields.get(4);
                result.add(new Board(
                    (Long) fields.get(0),
                    (Long) fields.get(1),
                    (String) fields.get(2),
                    (String) fields.get(3),
                    isTradedLong == 1));
            }

        } catch (IOException | ParseException e) {
            throw new MoexApiException(e);
        }
        return result;
    }

    public ArrayList<Board> getBoards(Engine engine, Market market) throws MoexApiException {
        return getBoards(engine.name, market.name);
    }

    ///*****************************************************************************************************************
    /// Utils
    ///*****************************************************************************************************************

    interface FieldGetterLambda<TResult> {
        TResult operate(JSONArray fields);
    }

    private <TResult> TResult getData(String tickerName, String table, Method urlGetter, FieldGetterLambda<TResult> fieldGetter) throws MoexApiException, InvocationTargetException, IllegalAccessException {
        TResult result = null;
        URL request;
        String read;
        try {
            request = (URL) urlGetter.invoke(null, engineName, marketName, boardName);
            read = urlReader.read(request);
            JSONArray data = jsonReader.readSingleField(read, table);
            HashMap<String, TResult> tickers = new HashMap<>();
            for (Object datum : data) {
                JSONArray fields = (JSONArray) datum;
                if (fields.size() != 2) {
                    throw new MoexApiException("Invalid syntax");
                }
                TResult res = fieldGetter.operate(fields);
                tickers.put((String) fields.get(0), res);
            }
            result = tickers.get(tickerName);
        } catch (IOException | ParseException e) {
            throw new MoexApiException(e);
        }
        return result;
    }
}
