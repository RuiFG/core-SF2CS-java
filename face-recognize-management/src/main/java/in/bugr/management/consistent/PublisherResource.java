package in.bugr.management.consistent;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author BugRui
 * @date 2020/4/7 下午9:57
 **/
@Getter
@Slf4j
@Component
public class PublisherResource {
    public static class SessionManagement {
        private static final Gson GSON = new Gson();
        private final Map<Long, List<Session>> sessionMap = new ConcurrentHashMap<>();

        public Session remove(Long id, Session session) {
            List<Session> sessions = sessionMap.get(id);
            if (CollectionUtils.isNotEmpty(sessions)) {
                if (sessions.remove(session)) {
                    return session;
                }
            }
            return null;
        }

        public void put(Long id, Session session) {
            List<Session> sessions = sessionMap.getOrDefault(id, new CopyOnWriteArrayList<>());
            sessions.add(session);
            sessionMap.put(id, sessions);
        }

        public List<Session> get(Long id) {
            return sessionMap.getOrDefault(id, Collections.emptyList());
        }

        public void sendAll(Long id, Object o) {
            sendAll(id, GSON.toJson(o));
        }

        public void sendAll(Long id, String text) {
            List<Session> sessions = sessionMap.getOrDefault(id, Collections.emptyList());
            sessions.forEach(session -> {
                send(session, text);
            });
        }

        public void send(Session session, String text) {
            try {
                session.getBasicRemote().sendText(text);
            } catch (IOException e) {
                log.error("发送信息失败");
            }
        }

        public void send(Session session, Object o) {
            send(session, GSON.toJson(o));
        }
    }

    @NotNull
    private final SessionManagement sessionManagement = new SessionManagement();

}
