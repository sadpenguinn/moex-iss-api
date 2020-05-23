package ru.badrudin;

import java.util.ArrayList;

import ru.badrudin.api.JSONReader;
import ru.badrudin.api.MoexApi;
import ru.badrudin.api.MoexApiException;
import ru.badrudin.api.URLReader;
import ru.badrudin.api.model.Board;
import ru.badrudin.api.model.Engine;
import ru.badrudin.api.model.Market;

public class Main {

    public static void main(String[] args) {
        System.out.println();
        try {
            URLReader urlReader = new URLReader();
            JSONReader jsonReader = new JSONReader();
            MoexApi api = new MoexApi<URLReader, JSONReader>(urlReader, jsonReader).setEngine("stock").setMarket("shares").setBoard("TQBR");


            var price = api.getPrice("LKOH");
            System.out.println(price);

            var lot = api.getLotSize("LKOH");
            System.out.println(lot);

            var shortName = api.getShortName("LKOH");
            System.out.println(shortName);

            var name = api.getFullName("LKOH");
            System.out.println(name);

        } catch (MoexApiException e) {
            e.printStackTrace();
            System.out.println( e.getMessage());
            return;
        }
    }
}
