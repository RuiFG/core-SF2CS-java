package in.bugr.service;

import in.bugr.entity.dto.StudentAttendanceInfo;

import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/20 下午9:43
 **/
public interface StudentService {
    List<StudentAttendanceInfo> getHistory();
}
