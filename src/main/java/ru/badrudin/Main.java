package ru.badrudin;

import java.util.ArrayList;

import ru.badrudin.api.MoexApi;
import ru.badrudin.api.MoexApiException;
import ru.badrudin.api.model.Board;
import ru.badrudin.api.model.Engine;
import ru.badrudin.api.model.Market;

public class Main {

    public static void main(String[] args) {
        ArrayList<Engine> engines = null;
        try {
            engines = MoexApi.getEngines();
        } catch (MoexApiException e) {
            e.printStackTrace();
            System.out.println( e.getMessage());
            return;
        }
        for (var engine : engines) {
            System.out.println(engine);
        }

        System.out.println();
        ArrayList<Market> markets = null;
        try {
            markets = MoexApi.getMarkets("stock");
        } catch (MoexApiException e) {
            e.printStackTrace();
            System.out.println( e.getMessage());
            return;
        }
        for (var market : markets) {
            System.out.println(market);
        }

        System.out.println();
        ArrayList<Board> boards = null;
        try {
            boards = MoexApi.getBoards("stock", "shares");
        } catch (MoexApiException e) {
            e.printStackTrace();
            System.out.println( e.getMessage());
            return;
        }
        for (var board : boards) {
            System.out.println(board);
        }

        System.out.println();
        try {
            MoexApi api = new MoexApi().setEngine("stock").setMarket("shares").setBoard("TQBR");


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
