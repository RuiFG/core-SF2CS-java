package in.bugr.test;

import com.google.gson.Gson;
import in.bugr.message.RecognitionResult;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author BugRui
 * @date 2020/4/8 下午5:00
 **/
public class TestPublishClient {
    private static final Gson GSON = new Gson();

    static class PublishClient extends WebSocketClient {

        public PublishClient(URI serverUri) {
            super(serverUri);
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            System.out.println("打开连接");
        }

        @Override
        public void onMessage(String s) {
            RecognitionResult.Detail detail = GSON.fromJson(s, RecognitionResult.Detail.class);
            System.out.println("收到消息" + detail.toString());
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
        PublishClient client = new PublishClient(new URI("http://127.0.0.1:9999/management/publish/2"));
        client.connect();
    }
}
