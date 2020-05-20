package in.bugr;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BugRui
 * @date 2020/5/17 下午4:07
 **/
public class DetectClient extends WebSocketClient {
    private Boolean ready = false;
    List<SocketObServer> socketObServerList = new ArrayList<>();

    public DetectClient(URI serverUri) {
        super(serverUri);
    }

    void addObServer(SocketObServer socketObServer) {
        socketObServerList.add(socketObServer);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        ready = true;
    }

    @Override
    public void onMessage(String s) {
        for (SocketObServer socketObServer : socketObServerList) {
            socketObServer.notify(s);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }

    public Boolean isReady() {
        return ready;
    }
}
