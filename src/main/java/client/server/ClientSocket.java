package client.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocket {
    public static void main(String[] args) {
        try (Socket clientSocket = new Socket("127.0.0.1", 8000)) {
            Scanner scanner = new Scanner(clientSocket.getInputStream());
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
