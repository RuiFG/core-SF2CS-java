package in.bugr.component;

import in.bugr.common.component.ContextSpringConfigurator;
import in.bugr.consistent.CollectorResource;
import in.bugr.consistent.PublisherResource;
import in.bugr.common.message.RecognitionResult;
import in.bugr.entity.AttendanceDetail;
import in.bugr.repository.AttendanceDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.websocket.CloseReason;
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
import java.util.List;

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

    @NotNull
    private final CollectorResource collectorResource;

    @NotNull
    private final AttendanceDetailRepository attendanceDetailRepository;

    @OnOpen
    public void onOpen(@PathParam("attendanceId") Long attendanceId, Session session) throws IOException {
        PublisherResource.@NotNull SessionManagement sessionManagement = publisherResource.getSessionManagement();
        log.info("建立连接");
        sessionManagement.put(attendanceId, session);
        CollectorResource.@NotNull AttendanceManagement attendanceManagement = collectorResource.getAttendanceManagement();
        //如果没有收集任务

        if (attendanceManagement.getAll()
                .stream()
                .filter(attendance -> attendance.getId().equals(attendanceId))
                .count() != 1) {
            session.close(new CloseReason(CloseReason.CloseCodes.NO_STATUS_CODE,
                    "没有当前任务"));
            return;
        }
        RecognitionResult recognitionResult = new RecognitionResult();
        List<AttendanceDetail> attendanceDetails = attendanceDetailRepository.findAllByAttendanceId(attendanceId);
        attendanceDetails.forEach(attendanceDetail -> {
            recognitionResult.add(attendanceDetail.getPersonId(),
                    attendanceDetail.getScore(), attendanceDetail.getFace());
        });
        sessionManagement.send(session, recognitionResult);
    }

    @OnClose
    public void onClose(@PathParam("attendanceId") Long attendanceId, Session session) {
        PublisherResource.@NotNull SessionManagement sessionManagement = publisherResource.getSessionManagement();
        sessionManagement.remove(attendanceId, session);
    }
}
