package ru.badrudin.api;

import java.io.IOException;
import java.net.MalformedURLException;
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

import javax.xml.parsers.DocumentBuilder;

public class MoexApi {

    private String engineName;
    private String marketName;
    private String boardName;

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

    public Double getPrice(String tickerName) throws MoexApiException {
        Double result = null;
        URL request;
        try {
            request = new RequestBuilder().buildGetPrice(engineName, marketName, boardName);
        } catch (MalformedURLException e) {
            throw new MoexApiException(e);
        }
        String read;
        try {
            read = readURL(request);
        } catch (IOException e) {
            throw new MoexApiException(e);
        }
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(read);
            JSONObject marketdata = (JSONObject) json.get("marketdata");
            JSONArray data = (JSONArray) marketdata.get("data");

            HashMap<String, Double> tickers = new HashMap<>();
            for (Object datum : data) {
                JSONArray fields = (JSONArray) datum;
                String ticker = null;
                Double price = null;

                int iteration = 1;
                if (fields.size() != 2) {
                    throw new MoexApiException("");
                }
                for (Object field : fields) {
                    switch (iteration) {
                        case 1:
                            ticker = (String) field;
                            break;
                        case 2:
                            try {
                                price = (Double) field;
                            } catch (ClassCastException e) {
                                Long priceLong = (Long) field;
                                price = priceLong.doubleValue();
                            }
                            break;
                        default:
                            throw new MoexApiException("");
                    }
                    ++iteration;
                }
                tickers.put(ticker, price);
            }
            result = tickers.get(tickerName);

        } catch (ParseException e) {
            throw new MoexApiException(e);
        }
        return result;
    }

    public static ArrayList<Engine> getEngines() throws MoexApiException {
        ArrayList<Engine> result = new ArrayList<>();
        URL request;
        try {
            request = new RequestBuilder().buildGetEngines();
        } catch (MalformedURLException e) {
            throw new MoexApiException(e);
        }
        String read;
        try {
            read = readURL(request);
        } catch (IOException e) {
            throw new MoexApiException(e);
        }
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(read);
            JSONObject engines = (JSONObject) json.get("engines");
            JSONArray data = (JSONArray) engines.get("data");

            for (Object datum : data) {
                JSONArray fields = (JSONArray) datum;
                Long id = null;
                String name = null;
                String desc = null;

                int iteration = 1;
                if (fields.size() != 3) {
                    throw new MoexApiException("");
                }
                for (Object field : fields) {
                    switch (iteration) {
                        case 1:
                            id = (Long) field;
                            break;
                        case 2:
                            name = (String) field;
                            break;
                        case 3:
                            desc = (String) field;
                            break;
                        default:
                            throw new MoexApiException("");
                    }
                    ++iteration;
                }
                result.add(new Engine(id, name, desc));
            }

        } catch (ParseException e) {
            throw new MoexApiException(e);
        }
        return result;
    }

    public static ArrayList<Market> getMarkets(String engine) throws MoexApiException {
        ArrayList<Market> result = new ArrayList<>();
        URL request;
        try {
            request = new RequestBuilder().buildGetMarkets(engine);
        } catch (MalformedURLException e) {
            throw new MoexApiException(e);
        }
        String read;
        try {
            read = readURL(request);
        } catch (IOException e) {
            throw new MoexApiException(e);
        }
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(read);
            JSONObject markets = (JSONObject) json.get("markets");
            JSONArray data = (JSONArray) markets.get("data");

            for (Object datum : data) {
                JSONArray fields = (JSONArray) datum;
                Long id = null;
                String name = null;
                String desc = null;

                int iteration = 1;
                if (fields.size() != 3) {
                    throw new MoexApiException("");
                }
                for (Object field : fields) {
                    switch (iteration) {
                        case 1:
                            id = (Long) field;
                            break;
                        case 2:
                            name = (String) field;
                            break;
                        case 3:
                            desc = (String) field;
                            break;
                        default:
                            throw new MoexApiException("");
                    }
                    ++iteration;
                }
                result.add(new Market(id, name, desc));
            }

        } catch (ParseException e) {
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
        try {
            request = new RequestBuilder().buildGetBoards(engine, market);
        } catch (MalformedURLException e) {
            throw new MoexApiException(e);
        }
        String read;
        try {
            read = readURL(request);
        } catch (IOException e) {
            throw new MoexApiException(e);
        }
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(read);
            JSONObject markets = (JSONObject) json.get("boards");
            JSONArray data = (JSONArray) markets.get("data");

            for (Object datum : data) {
                JSONArray fields = (JSONArray) datum;
                Long id = null;
                Long boardGroupId = null;
                String boardId = null;
                String description = null;
                Boolean isTraded = false;

                int iteration = 1;
                if (fields.size() != 5) {
                    throw new MoexApiException("");
                }
                for (Object field : fields) {
                    switch (iteration) {
                        case 1:
                            id = (Long) field;
                            break;
                        case 2:
                            boardGroupId = (Long) field;
                            break;
                        case 3:
                            boardId = (String) field;
                            break;
                        case 4:
                            description = (String) field;
                            break;
                        case 5:
                            var isTradedLong = (Long) field;
                            isTraded = isTradedLong == 1;
                            break;
                        default:
                            throw new MoexApiException("");
                    }
                    ++iteration;
                }
                result.add(new Board(id, boardGroupId, boardId, description, isTraded));
            }

        } catch (ParseException e) {
            throw new MoexApiException(e);
        }
        return result;
    }

    public static ArrayList<Board> getBoards(Engine engine, Market market) throws MoexApiException {
        return getBoards(engine.name, market.name);
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
