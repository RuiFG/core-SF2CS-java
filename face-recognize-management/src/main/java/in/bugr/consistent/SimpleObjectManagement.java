package in.bugr.consistent;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author BugRui
 * @date 2020/5/20 下午6:18
 **/
public abstract class SimpleObjectManagement<ID, T> implements ObjectManagement<ID, T> {
    private final Map<ID, T> map = new ConcurrentHashMap<>(16);

    public Map<ID, T> getMap() {
        return map;
    }

    @Override
    public T put(ID id, T o) {
        return map.put(id, o);
    }

    @Override
    public T get(ID id) {
        return map.getOrDefault(id, null);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public T remove(ID id) {
        return map.remove(id);
    }

    @Override
    public Boolean contains(ID id) {
        return map.containsKey(id);
    }
}
