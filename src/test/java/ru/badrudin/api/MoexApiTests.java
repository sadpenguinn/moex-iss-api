package ru.badrudin.api;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MoexApiTests {

    private final URLReader urlReader = Mockito.mock(URLReader.class);
    private final JSONReader jsonReader = Mockito.mock(JSONReader.class);
    private final MoexApi api = new MoexApi(urlReader, jsonReader);

    @Test
    public void testGetPrice() {
        try {
            Double expected = 6.7;
            Map<String, Double> tickers = new HashMap<String, Double>() {
                {
                    put("TIC1", 1.2);
                    put("TIC2", 2.3);
                    put("LKOH", expected);
                    put("TIC3", 3.4);
                }
            };
            doTestGetData("LKOH", tickers, expected, "marketdata", MoexApi.class.getMethod("getPrice", String.class));
        } catch (NoSuchMethodException e) {
            Assert.fail("Unexpected exception: " + e.toString());
        }
    }

    // methods
    private void setUpApi(String engine, String market, String board) {
        api.setEngine(engine).setMarket(market).setBoard(board);
    }

    private <TResult> JSONArray buildJSONDataArray(Map<String, TResult> tickers) {
        JSONArray data = new JSONArray();
        for (Map.Entry<String, TResult> ticker : tickers.entrySet()) {
            JSONArray datum = new JSONArray() {
                {
                    add(ticker.getKey());
                    add(ticker.getValue());
                }
            };
            data.add(datum);
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    private <TType> void doTestGetData(String ticker, Map<String, TType> tickers, TType expected, String table, Method methodToTest) {
        try {
            setUpApi(engine, market, board);
            URL request = RequestBuilder.buildGetPrice(engine, market, board);
            var data = buildJSONDataArray(tickers);
            Mockito.doReturn(response).when(urlReader).read(request);
            Mockito.doReturn(data).when(jsonReader).readSingleField(response, table);
            TType res = (TType) methodToTest.invoke(api, ticker);
            Assert.assertEquals(res, expected);
        } catch (ParseException | IOException | IllegalAccessException | InvocationTargetException e) {
            Assert.fail("Unexpected exception: " + e.toString());
        }
    }

    // data
    private final String engine = "stock";
    private final String market = "shares";
    private final String board = "TQBR";
    private final String response = "response";
}
