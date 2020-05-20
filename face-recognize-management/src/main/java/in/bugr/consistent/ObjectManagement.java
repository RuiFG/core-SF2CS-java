package in.bugr.consistent;

import java.util.List;

/**
 * @author BugRui
 * @date 2020/5/20 下午6:12
 **/
public interface ObjectManagement<ID, T> {

    T put(ID id, T o);

    T get(ID id);

    List<T> getAll();

    T remove(ID id);

    Boolean contains(ID id);
}
