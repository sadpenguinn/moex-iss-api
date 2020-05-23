package ru.badrudin.api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONReader implements IJSONReader {
    public JSONArray readSingleField(String read, String fieldName) throws ParseException {
        JSONObject json = (JSONObject) new JSONParser().parse(read);
        JSONObject dataByFields = (JSONObject) json.get(fieldName);
        return (JSONArray) dataByFields.get("data");
    }
}
