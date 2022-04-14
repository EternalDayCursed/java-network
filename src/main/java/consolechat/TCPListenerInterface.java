package consolechat;

public interface TCPListenerInterface {
    void connectionReady(TCPConnection tcpConnection);
    void receiveString(TCPConnection tcpConnection, String value);
    void disconnection(TCPConnection tcpConnection);
    void exception(TCPConnection tcpConnection, Exception e);
}
