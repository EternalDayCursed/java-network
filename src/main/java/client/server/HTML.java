package client.server;
import java.io.IOException;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class HTML {

    public static void connectURL(String http) throws IOException {
        URLConnection connection = new URL(http).openConnection();
        Scanner scanner = new Scanner(connection.getInputStream());
        scanner.useDelimiter("\\Z");
        System.out.println(scanner.next());
    }
}
