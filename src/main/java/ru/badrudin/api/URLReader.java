package ru.badrudin.api;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class URLReader {
    public String read(URL request) throws IOException {
        Scanner scanner = new Scanner(request.openStream());
        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            input.append(scanner.nextLine());
        }
        return input.toString();
    }
}
