package ru.badrudin.api;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MoexApiTests {

    URLReader urlReader = Mockito.mock(URLReader.class);
    JSONReader jsonReader = Mockito.mock(JSONReader.class);

    private final MoexApi api = new MoexApi(urlReader, jsonReader);

    @Test
    public void testGetPrice() {
        try {
            var engine = "stock";
            var market = "shares";
            var board = "TQBR";

            setUpApi(engine, market, board);
            URL request = RequestBuilder.buildGetPrice(engine, market, board);

            var response = "response";
            Double expected = 1.5;
            Map<String, Double> tickers = new HashMap<String, Double>() {
                {
                    put("FGGD", 5.7);
                    put("EFWE", 3.6);
                    put("LKOH", expected);
                }
            };
            var data = buildJSONDataArray(tickers);
            Mockito.doReturn(response).when(urlReader).read(request);
            Mockito.doReturn(data).when(jsonReader).readSingleField(response, "marketdata");

            Double res = api.getPrice("LKOH");
            Assert.assertEquals(res, expected);
        } catch (ParseException | IOException | MoexApiException e) {
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
}
