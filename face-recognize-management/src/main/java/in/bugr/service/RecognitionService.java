package in.bugr.service;

import in.bugr.entity.Attendance;
import in.bugr.entity.AttendanceDetail;

import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/16 下午4:43
 **/
public interface RecognitionService {

    List<Attendance> getOnline();

    List<Attendance> history();

    List<AttendanceDetail> historyDetail(Long id);


    AttendanceDetail addHistoryDetail(Long id, AttendanceDetail attendanceDetail);
}
