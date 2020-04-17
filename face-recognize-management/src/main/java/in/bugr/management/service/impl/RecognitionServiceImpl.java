package in.bugr.management.service.impl;

import com.google.gson.JsonObject;
import in.bugr.management.consistent.CollectorResource;
import in.bugr.management.entity.Attendance;
import in.bugr.management.service.RecognitionService;
import in.bugr.repository.GatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/16 下午4:43
 **/
@Service
@RequiredArgsConstructor
public class RecognitionServiceImpl implements RecognitionService {
    @NotNull
    private final CollectorResource collectorResource;

    @NotNull
    private final GatherRepository gatherRepository;

    @Override
    public List<Attendance> getOnline() {
        CollectorResource.@NotNull AttendanceManagement attendanceManagement = collectorResource.getAttendanceManagement();
        return attendanceManagement.getAll();
    }
}
