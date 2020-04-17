package in.bugr.management.service;

import com.google.gson.JsonObject;
import in.bugr.management.entity.Attendance;

import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/16 下午4:43
 **/
public interface RecognitionService {
    List<Attendance> getOnline();
}
