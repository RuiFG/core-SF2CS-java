package in.bugr.management.component;

import com.google.gson.Gson;
import in.bugr.component.ContextSpringConfigurator;
import in.bugr.entity.Gather;
import in.bugr.entity.GatherPerson;
import in.bugr.management.consistent.CollectorResource;
import in.bugr.management.consistent.PublisherResource;
import in.bugr.management.entity.Attendance;
import in.bugr.management.entity.AttendanceDetail;
import in.bugr.management.repository.AttendanceDetailRepository;
import in.bugr.management.repository.AttendanceRepository;
import in.bugr.message.Constant;
import in.bugr.message.RecognitionResult;
import in.bugr.repository.GatherPersonRepository;
import in.bugr.repository.GatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author BugRui
 * @date 2020/3/31 下午1:32
 **/
@Component
@RequiredArgsConstructor
@Slf4j
@ServerEndpoint(value = "/collect/{gatherId}/{attendanceName}", configurator = ContextSpringConfigurator.class)
public class CollectorSocketHandler {
    private static final Gson GSON = new Gson();

    @NotNull
    private final GatherPersonRepository gatherPersonRepository;

    @NotNull
    private final GatherRepository gatherRepository;

    @NotNull
    private final AttendanceRepository attendanceRepository;

    @NotNull
    private final AttendanceDetailRepository attendanceDetailRepository;

    @NotNull
    private final CollectorResource collectorResource;

    @NotNull
    private final PublisherResource publisherResource;


    @OnOpen
    public void onOpen(@PathParam("gatherId") Long gatherId, @PathParam("attendanceName") String attendanceName, Session session) throws IOException {
        CollectorResource.SessionManagement sessionManagement = collectorResource.getSessionManagement();
        CollectorResource.AttendanceManagement attendanceManagement = collectorResource.getAttendanceManagement();
        if (sessionManagement.contains(gatherId)) {
            log.info("重复连接,关闭连接");
            session.close();
            return;
        }
        Gather gather = gatherRepository.findById(gatherId).orElse(null);
        if (ObjectUtils.isEmpty(gather)) {
            log.info("未找到gather,关闭连接");
            session.close();
            return;
        }
        List<GatherPerson> gatherPersonList = gatherPersonRepository.findAllByGatherId(gatherId);
        CopyOnWriteArrayList<Long> personIds = gatherPersonList.stream().map(GatherPerson::getPersonId).collect(Collectors.toCollection(CopyOnWriteArrayList::new));
        Attendance attendance = new Attendance()
                .setName(attendanceName)
                .setFinish(false)
                .setGatherId(gatherId)
                .setNotDetectPersonIds(personIds)
                .setGatherName(gather.getName());
        attendance = attendanceRepository.save(attendance);
        attendanceManagement.put(gatherId, attendance);
        sessionManagement.put(gatherId, session);
    }

    @OnClose
    public void onClose(@PathParam("gatherId") Long gatherId) {
        collectorResource.getSessionManagement().remove(gatherId);
        Attendance attendance = collectorResource.getAttendanceManagement().remove(gatherId);
        attendance.setFinish(true);
        attendanceRepository.save(attendance);

    }

    @OnError
    public void onError(@PathParam("gatherId") Long gatherId, Throwable error) {
        log.error("websocket出现错误");
        Attendance attendance = collectorResource.getAttendanceManagement().remove(gatherId);
        attendanceRepository.delete(attendance);
        attendanceDetailRepository.deleteAllByAttendanceId(attendance.getId());
        error.printStackTrace();
    }

    /**
     * The method receiving recognition's json,
     * analyze and send realtime message to publisher
     *
     * @param gatherId        gather id
     * @param recognitionJson json
     * @param session         current client
     */
    @OnMessage(maxMessageSize = Constant.MAX_MESSAGE_SIZE)
    public void onMessage(@PathParam("gatherId") Long gatherId, String recognitionJson, Session session) {
        RecognitionResult recognitionResult = GSON.fromJson(recognitionJson, RecognitionResult.class);
        CollectorResource.AttendanceManagement attendanceManagement = collectorResource.getAttendanceManagement();
        PublisherResource.@NotNull SessionManagement sessionManagement = publisherResource.getSessionManagement();
        recognitionResult.getPersons().forEach(detail -> {
            //If the person is already in memory, no action is taken
            //TODO If a person does not work for a long time,maybe we can refresh the publisher
            if (attendanceManagement.addDetectPerson(gatherId, detail.getPersonId())) {
                Attendance attendance = attendanceManagement.get(gatherId);
                AttendanceDetail attendanceDetail = new AttendanceDetail()
                        .setAttendanceId(attendance.getId())
                        .setImgData(detail.getFaceImgData())
                        .setPersonId(detail.getPersonId())
                        .setScore(detail.getCompareScore())
                        .setTime(LocalDateTime.now());
                attendanceDetailRepository.save(attendanceDetail);
                //send attendance's detail to publisher
                sessionManagement.sendAll(attendance.getId(), detail);
            }
        });
    }

}
