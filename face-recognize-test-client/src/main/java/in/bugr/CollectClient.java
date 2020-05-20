package in.bugr;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * @author BugRui
 * @date 2020/5/17 下午4:04
 **/
public class CollectClient extends WebSocketClient implements SocketObServer {
    private boolean ready = false;

    public CollectClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("打开连接");
        ready = true;
    }

    @Override
    public void onMessage(String s) {
        System.out.println("收到消息");
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("关闭");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("异常");
    }

    public boolean isReady() {
        return ready;
    }

    @Override
    public void notify(String s) {
        this.send(s);
    }
}
