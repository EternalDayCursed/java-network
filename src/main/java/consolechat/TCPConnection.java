package consolechat;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPConnection {
    private final Socket socket;
    private final Thread smsThread;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;
    private final TCPListenerInterface eventListener;

    public TCPConnection(TCPListenerInterface eventListener, String ipAddress, int port) throws IOException {
        this(eventListener, new Socket(ipAddress, port));
    }

    public TCPConnection(TCPListenerInterface eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;

        bufferedReader = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));

        bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(
                        socket.getOutputStream()));

        smsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.connectionReady(TCPConnection.this);
                    while (!smsThread.isInterrupted()) {
                        eventListener.receiveString(TCPConnection.this, bufferedReader.readLine());
                    }

                } catch (IOException e) {
                    eventListener.exception(TCPConnection.this, e);
                } finally {
                    eventListener.disconnection(TCPConnection.this);
                }
            }
        });
        smsThread.start();
    }

    public synchronized void sendMessage(String msg) {
        try {
            bufferedWriter.write(msg + "\r\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            eventListener.exception(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        smsThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.exception(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}
