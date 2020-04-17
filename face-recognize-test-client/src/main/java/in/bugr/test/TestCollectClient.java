package in.bugr.test;

import com.google.gson.Gson;
import in.bugr.message.RecognitionResult;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author BugRui
 * @date 2020/4/7 下午9:26
 **/
public class TestCollectClient {
    private static Gson GSON = new Gson();

    static class CollectClient extends WebSocketClient {

        public CollectClient(URI serverUri) {
            super(serverUri);
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            System.out.println("打开连接");
            RecognitionResult recognitionResult = new RecognitionResult();
            recognitionResult.add(1L, 0.75f, new byte[]{0});
            this.send(GSON.toJson(recognitionResult));
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
    }

    public static void main(String[] args) throws URISyntaxException {
        CollectClient collectClient = new CollectClient(new URI("http://127.0.0.1:8080/collect/1/4faf"));
        collectClient.connect();

    }
}
