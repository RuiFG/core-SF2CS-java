package in.bugr.test;

import in.bugr.message.DetectTask;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author BugRui
 * @date 2020/4/7 下午3:15
 **/
public class TestDetectClient {
    static long begin;

    static class DetectClient extends WebSocketClient {

        public DetectClient(URI serverUri) {
            super(serverUri);
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            System.out.println("连接");
            File f = new File("/home/bugrui/SF2CS/core-SF2CS-java/face-recognize-test-client/src/main/resources/img/1.jpg");
            FileChannel channel;
            FileInputStream fs;
            try {
                fs = new FileInputStream(f);
                channel = fs.getChannel();
                ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
                while ((channel.read(byteBuffer)) > 0) {
                    // do nothing
                    // System.out.println("reading");
                }
                this.send(byteBuffer.array());
                ;
                begin = System.currentTimeMillis();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(String s) {
            System.out.println("收到消息:" + s);
            System.out.println(System.currentTimeMillis() - begin+"ms");
        }

        @Override
        public void onClose(int i, String s, boolean b) {

        }

        @Override
        public void onError(Exception e) {

        }
    }

    public static void main(String[] args) throws URISyntaxException, FileNotFoundException {
        String host = "http://127.0.0.1:9999/server/detect/1";
        WebSocketClient detectClient = new DetectClient(new URI(host));
        detectClient.connect();
    }
}
