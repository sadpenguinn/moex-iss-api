package ru.badrudin.api;

import java.io.IOException;
import java.net.URL;

public interface IURLReader {
    String read(URL request) throws IOException;
}
