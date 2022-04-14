/*Поток диспетчеризации событий - это такой поток, который обрабатывает все события в GUI и управляет моим GUI Swing.
 * Он запускается под капотом Swing для простоты - мне не нужно управлять доп.потоками самостоятельно.
 * Также Swing не является потокобезопасным, как и большинство из набора инструментов.*/

/*Поскольку у Swing жесткие ограничения
по многопоточности (со Swing нельзя работать даже из главного потока, а только из EDT (поток диспетчеризации событий)),
то для решения данной проблемы используется конструкция SwingUtilities.invokeLater(new Runnable()...
Таким образом мы можем заставить код выполнится в потоке EDT*/

package consolechat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Client extends JFrame implements ActionListener, TCPListenerInterface {

    private static final String LOCAL_IP_ADDR = "localhost";
    private static final int PORT = 8180;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }

    private final JTextArea log = new JTextArea();
    private final JTextField nickname = new JTextField("Roman");
    private final JTextField inputField = new JTextField();

    private TCPConnection tcpConnection;

    private Client() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);

        add(inputField, BorderLayout.SOUTH);
        add(nickname, BorderLayout.NORTH);
        inputField.addActionListener(this);

        setVisible(true);

        try {
            tcpConnection = new TCPConnection(this, LOCAL_IP_ADDR, PORT);
        } catch (IOException e) {
            printMsg("Connection exception: " + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = inputField.getText();
        if(msg.equals("")) {return;}
        inputField.setText(null);
        tcpConnection.sendMessage(nickname.getText() + ": " + msg);
    }

    @Override
    public void connectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready!");
    }

    @Override
    public void receiveString(TCPConnection tcpConnection, String value) {
        printMsg(value);
    }

    @Override
    public void disconnection(TCPConnection tcpConnection) {
        printMsg("Connection close.");
    }

    @Override
    public void exception(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection exception: " + e);
    }

    private synchronized void printMsg(String msg) {
        SwingUtilities.invokeLater(() -> {
            log.append(msg + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        });
    }
}
