package ru.badrudin.api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import ru.badrudin.api.model.Board;
import ru.badrudin.api.model.Engine;
import ru.badrudin.api.model.Market;

public class MoexApi {

    private String engineName;
    private String marketName;
    private String boardName;

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
                "securities",
                RequestBuilder.class.getMethod("buildGetLotSize", String.class, String.class, String.class),
                null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new MoexApiException(e);
        }
    }

    public String getShortName(String tickerName) throws MoexApiException {
        try {
            return getData(
                tickerName,
                "securities",
                RequestBuilder.class.getMethod("buildGetShortName", String.class, String.class, String.class),
                null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new MoexApiException(e);
        }
    }

    public String getFullName(String tickerName) throws MoexApiException {
        try {
            return getData(
                tickerName,
                "securities",
                RequestBuilder.class.getMethod("buildGetFullName", String.class, String.class, String.class),
                null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new MoexApiException(e);
        }
    }

    /// Metadata

    public Double getPrice(String tickerName) throws MoexApiException {

        try {
            return getData(
                tickerName,
                "marketdata",
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

    public static ArrayList<Engine> getEngines() throws MoexApiException {
        ArrayList<Engine> result = new ArrayList<>();
        URL request;
        String read;
        try {
            request = RequestBuilder.buildGetEngines();
            read = readURL(request);

            JSONArray data = getDataFromJSON(read, "engines");
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

    public static ArrayList<Market> getMarkets(String engine) throws MoexApiException {
        ArrayList<Market> result = new ArrayList<>();
        URL request;
        String read;
        try {
            request = RequestBuilder.buildGetMarkets(engine);
            read = readURL(request);

            JSONArray data = getDataFromJSON(read, "markets");
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

    public static ArrayList<Market> getMarkets(Engine engine) throws MoexApiException {
        return getMarkets(engine.name);
    }

    public static ArrayList<Board> getBoards(String engine, String market) throws MoexApiException {
        ArrayList<Board> result = new ArrayList<>();
        URL request;
        String read;
        try {
            request = RequestBuilder.buildGetBoards(engine, market);
            read = readURL(request);

            JSONArray data = getDataFromJSON(read, "boards");
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

    public static ArrayList<Board> getBoards(Engine engine, Market market) throws MoexApiException {
        return getBoards(engine.name, market.name);
    }

    ///*****************************************************************************************************************
    /// Utils
    ///*****************************************************************************************************************

    interface FieldGetterLambda<TResult> {
        TResult operate(JSONArray fields);
    }

    @SuppressWarnings("unchecked")
    private <TResult> TResult getData(String tickerName, String table, Method urlGetter, FieldGetterLambda<TResult> fieldGetter) throws MoexApiException, InvocationTargetException, IllegalAccessException {
        TResult result = null;
        URL request;
        String read;
        try {
            Object[] parameters = new Object[3];
            parameters[0] = engineName;
            parameters[1] = marketName;
            parameters[2] = boardName;
            request = (URL) urlGetter.invoke(null, parameters);
            read = readURL(request);

            JSONArray data = getDataFromJSON(read, table);
            HashMap<String, TResult> tickers = new HashMap<>();
            for (Object datum : data) {
                JSONArray fields = (JSONArray) datum;
                if (fields.size() != 2) {
                    throw new MoexApiException("Invalid syntax");
                }
                TResult res;
                if (fieldGetter != null) {
                    res = fieldGetter.operate(fields);
                } else {
                    res = (TResult) fields.get(1);
                }
                tickers.put((String) fields.get(0), res);
            }
            result = tickers.get(tickerName);
        } catch (IOException | ParseException e) {
            throw new MoexApiException(e);
        }
        return result;
    }

    private static JSONArray getDataFromJSON(String read, String fieldName) throws ParseException {
        JSONObject json = (JSONObject) new JSONParser().parse(read);
        JSONObject dataByFields = (JSONObject) json.get(fieldName);
        return (JSONArray) dataByFields.get("data");
    }

    private static String readURL(URL request) throws IOException {
        Scanner scanner = new Scanner(request.openStream());
        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            input.append(scanner.nextLine());
        }
        return input.toString();
    }
}
