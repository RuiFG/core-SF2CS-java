package in.bugr.management.component;

import in.bugr.component.ContextSpringConfigurator;
import in.bugr.management.consistent.PublisherResource;
import in.bugr.message.RecognitionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author BugRui
 * @date 2020/4/4 下午9:11
 **/
@Component
@Slf4j
@RequiredArgsConstructor
@ServerEndpoint(value = "/publish/{attendanceId}", configurator = ContextSpringConfigurator.class)
public class PublisherSocketHandler {

    @NotNull
    private final PublisherResource publisherResource;

    @OnOpen
    public void onOpen(@PathParam("attendanceId") Long attendanceId, Session session) {
        PublisherResource.@NotNull SessionManagement sessionManagement = publisherResource.getSessionManagement();
        log.info("建立连接");
        sessionManagement.put(attendanceId, session);

        RecognitionResult recognitionResult = new RecognitionResult();
        recognitionResult.setId(1);
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
            recognitionResult.add(1L,0.75f,byteBuffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }

        recognitionResult.add(1L,0.75f,new byte[]{0,3});
        recognitionResult.add(2L,0.75f,new byte[]{0,3});
        sessionManagement.send(session,recognitionResult);
    }

    @OnClose
    public void onClose(@PathParam("attendanceId") Long attendanceId, Session session) {
        PublisherResource.@NotNull SessionManagement sessionManagement = publisherResource.getSessionManagement();
        sessionManagement.remove(attendanceId, session);
    }
}
