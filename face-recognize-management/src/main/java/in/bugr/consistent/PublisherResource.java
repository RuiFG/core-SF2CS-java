package in.bugr.consistent;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
    public static class SessionManagement extends SimpleObjectManagement<Long, List<Session>> {
        private static final Gson GSON = new Gson();

        public void remove(Long id, Session session) {
            List<Session> sessions = get(id);
            if (CollectionUtils.isNotEmpty(sessions)) {
                sessions.remove(session);
            }
        }

        public void put(Long id, Session session) {
            List<Session> sessions = getMap().getOrDefault(id, new CopyOnWriteArrayList<>());
            sessions.add(session);
            put(id, sessions);
        }


        public void sendAll(Long id, Object o) {
            sendAll(id, GSON.toJson(o));
        }

        public void sendAll(Long id, String text) {
            List<Session> sessions = getMap().getOrDefault(id, Collections.emptyList());
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
