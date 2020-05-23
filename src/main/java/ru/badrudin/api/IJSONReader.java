package ru.badrudin.api;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

public interface IJSONReader {
    JSONArray readSingleField(String read, String fieldName) throws ParseException;
}
