package client.server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SerSocket {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new MyServer(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

class MyServer implements Runnable {
    Socket socket;

    public MyServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(socket.getInputStream());
             PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true)) {
            while (scanner.hasNextLine()) {
                String str = scanner.next();
                if (str.equals("exit")) {
                    printWriter.println("BYE BYE!");
                    break;
                } else if (str.equals("html")) {
                    Scanner scan = new Scanner(socket.getInputStream());
                    String request = scan.nextLine();
                    HTML.connectURL(request);

                }
                printWriter.println("you write: " + str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}