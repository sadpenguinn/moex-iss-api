package ru.badrudin;

import ru.badrudin.api.MoexApi;
import ru.badrudin.api.MoexApiException;
import ru.badrudin.api.model.Engine;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        MoexApi api = new MoexApi();
        ArrayList<Engine> engines = null;

        try {
            engines = api.getEngines();
        } catch (MoexApiException e) {
            e.printStackTrace();
            System.out.println( e.getMessage());
            return;
        }
        for (var engine : engines) {
            System.out.println(engine);
        }
    }
}
