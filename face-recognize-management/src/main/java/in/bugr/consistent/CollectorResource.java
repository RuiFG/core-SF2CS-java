package in.bugr.consistent;

import in.bugr.common.message.RecognitionResult;
import in.bugr.entity.Attendance;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author BugRui
 * @date 2020/4/7 下午9:56
 **/
@Getter
@Slf4j
@Component
public class CollectorResource {

    public static class SessionManagement extends SimpleObjectManagement<Long, Session> {
    }

    public static class AttendanceManagement extends SimpleObjectManagement<Long, Attendance> {
        public boolean addDetectPerson(Long gatherId, RecognitionResult.Detail detail) {
            Attendance attendance = getMap().get(gatherId);
            if (ObjectUtils.isEmpty(attendance)) {
                log.error("该gather考勤不存在");
                return false;
            }
            List<Long> notDetectPersonIds = attendance.getNotDetectPersonIds();
            List<Long> detectPersonIds = attendance.getDetectPersonIds();
            if (detail.getCompareScore() <= 0.6f) {
                return false;
            }
            long personId = detail.getPersonId();
            if (BooleanUtils.and(new boolean[]{notDetectPersonIds.contains(personId),
                    detectPersonIds.contains(personId)})) {
                log.error("该person不存在");
                return false;
            }
            if (notDetectPersonIds.contains(personId)) {
                notDetectPersonIds.remove(personId);
                detectPersonIds.add(personId);
            }
            return true;
        }
    }

    @NotNull
    private final SessionManagement sessionManagement = new SessionManagement();

    @NotNull
    private final AttendanceManagement attendanceManagement = new AttendanceManagement();

}
