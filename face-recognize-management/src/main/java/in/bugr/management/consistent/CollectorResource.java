package in.bugr.management.consistent;

import com.google.gson.Gson;
import in.bugr.management.entity.Attendance;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author BugRui
 * @date 2020/4/7 下午9:56
 **/
@Getter
@Slf4j
@Component
public class CollectorResource {

    public static class SessionManagement {
        private final Map<Long, Session> sessionMap = new ConcurrentHashMap<>(16);

        public Session put(Long id, Session session) {
            return sessionMap.put(id, session);
        }

        public Session get(Long id) {
            return sessionMap.get(id);
        }

        public Session remove(Long id) {
            return sessionMap.remove(id);
        }

        public Boolean contains(Long id) {
            return sessionMap.containsKey(id);
        }
    }

    public static class AttendanceManagement {
        private Map<Long, Attendance> attendanceMap = new ConcurrentHashMap<>(16);

        public Attendance get(Long gatherId) {
            return attendanceMap.get(gatherId);
        }

        public List<Attendance> getAll() {
            return new ArrayList<>(attendanceMap.values());
        }

        public void put(Long gatherId, Attendance attendance) {
            attendanceMap.put(gatherId, attendance);
        }

        public Attendance remove(Long gatherId) {
            return attendanceMap.remove(gatherId);
        }

        public boolean addDetectPerson(Long gatherId, Long personId) {
            Attendance attendance = attendanceMap.get(gatherId);
            if (ObjectUtils.isEmpty(attendance)) {
                log.error("该gather考勤不存在");
                return false;
            }
            List<Long> notDetectPersonIds = attendance.getNotDetectPersonIds();
            List<Long> detectPersonIds = attendance.getDetectPersonIds();
            if (BooleanUtils.and(new boolean[]{notDetectPersonIds.contains(personId), detectPersonIds.contains(personId)})) {
                log.error("该person不存在");
                return false;
            }
            notDetectPersonIds.remove(personId);
            detectPersonIds.add(personId);
            return true;
        }
    }

    @NotNull
    private final SessionManagement sessionManagement = new SessionManagement();

    @NotNull
    private final AttendanceManagement attendanceManagement = new AttendanceManagement();

}
