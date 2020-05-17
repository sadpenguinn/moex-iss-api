package ru.badrudin.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import ru.badrudin.api.model.Engine;

public class MoexApi {
    public ArrayList<Engine> getEngines() throws MoexApiException {
        ArrayList<Engine> result = new ArrayList<>();
        URL request = null;
        try {
            request = new RequestBuilder().buildGetEngines();
        } catch (MalformedURLException e) {
            throw new MoexApiException(e);
        }
        String read = null;
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

    private String readURL(URL request) throws IOException {
        Scanner scanner = new Scanner(request.openStream());
        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            input.append(scanner.nextLine());
        }
        return input.toString();
    }
}
