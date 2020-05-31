package ru.badrudin.api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class MoexApiTests {

    private final URLReader urlReader = Mockito.mock(URLReader.class);
    private final JSONReader jsonReader = Mockito.mock(JSONReader.class);
    private final MoexApi api = new MoexApi(urlReader, jsonReader);

    @Test
    public void testGetLotSizeSuccess() {
        try {
            Long expected = 10L;
            Map<String, Long> tickers = Map.of(
                    "TIC1", 1L,
                    "TIC2", 2L,
                    "LKOH", expected,
                    "TIC3", 3L
            );
            URL request = RequestBuilder.buildGetLotSize(engine, market, board);
            doTestGetData("LKOH", tickers, expected, ApiFields.SECURITIES, request, MoexApi.class.getMethod("getLotSize", String.class));
        } catch (NoSuchMethodException | MalformedURLException e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testGetShortNameSuccess() {
        try {
            String expected = "Lukoil";
            Map<String, String> tickers = Map.of(
                    "TIC1", "TIC1ShortName",
                    "TIC2", "TIC2ShortName",
                    "LKOH", expected,
                    "TIC3", "TIC3ShortName"
            );
            URL request = RequestBuilder.buildGetShortName(engine, market, board);
            doTestGetData("LKOH", tickers, expected, ApiFields.SECURITIES, request, MoexApi.class.getMethod("getShortName", String.class));
        } catch (NoSuchMethodException | MalformedURLException e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testGetFullNameSuccess() {
        try {
            String expected = "Lukoil";
            Map<String, String> tickers = Map.of(
                    "TIC1", "TIC1FullName",
                    "TIC2", "TIC2FullName",
                    "LKOH", expected,
                    "TIC3", "TIC3FullName"
            );
            URL request = RequestBuilder.buildGetFullName(engine, market, board);
            doTestGetData("LKOH", tickers, expected, ApiFields.SECURITIES, request, MoexApi.class.getMethod("getFullName", String.class));
        } catch (NoSuchMethodException | MalformedURLException e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testGetPriceSuccess() {
        try {
            Double expected = 6.7;
            Map<String, Double> tickers = Map.of(
                    "TIC1", 1.2,
                    "TIC2", 2.3,
                    "LKOH", expected,
                    "TIC3", 3.4
            );
            URL request = RequestBuilder.buildGetPrice(engine, market, board);
            doTestGetData("LKOH", tickers, expected, ApiFields.MARKETDATA, request, MoexApi.class.getMethod("getPrice", String.class));
        } catch (NoSuchMethodException | MalformedURLException e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testGetPriceRejected() {
        try {
            Double expected = null;
            Map<String, Double> tickers = Map.of(
                "TIC1", 1.2,
                "TIC2", 2.3,
                "TIC3", 3.4
            );
            URL request = RequestBuilder.buildGetPrice(engine, market, board);
            doTestGetData("LKOH", tickers, expected, ApiFields.MARKETDATA, request, MoexApi.class.getMethod("getPrice", String.class));
        } catch (NoSuchMethodException | MalformedURLException e) {
            Assert.fail(e.toString());
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
    private <TType> void doTestGetData(String ticker, Map<String, TType> tickers, TType expected, String table, URL request, Method methodToTest) {
        try {
            setUpApi(engine, market, board);
            var data = buildJSONDataArray(tickers);
            Mockito.doReturn(response).when(urlReader).read(request);
            Mockito.doReturn(data).when(jsonReader).readSingleField(response, table);
            TType res = (TType) methodToTest.invoke(api, ticker);
            Assert.assertEquals(res, expected);
        } catch (ParseException | IOException | IllegalAccessException | InvocationTargetException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    // data
    // engines
    private final String engine = "stock";

    private final String market = "shares";
    private final String board = "TQBR";
    private final String response = "response";
}
