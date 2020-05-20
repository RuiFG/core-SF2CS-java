package in.bugr.service.impl;

import in.bugr.common.entity.GatherPerson;
import in.bugr.common.entity.Person;
import in.bugr.common.entity.User;
import in.bugr.common.exception.CommonException;
import in.bugr.common.repository.GatherPersonRepository;
import in.bugr.common.repository.PersonRepository;
import in.bugr.common.repository.UserRepository;
import in.bugr.entity.Attendance;
import in.bugr.common.repository.GatherRepository;
import in.bugr.consistent.CollectorResource;
import in.bugr.entity.AttendanceDetail;
import in.bugr.repository.AttendanceDetailRepository;
import in.bugr.repository.AttendanceRepository;
import in.bugr.service.RecognitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author BugRui
 * @date 2020/4/16 下午4:43
 **/
@Service
@RequiredArgsConstructor
public class RecognitionServiceImpl implements RecognitionService {
    @NotNull
    private final GatherPersonRepository gatherPersonRepository;

    @NotNull
    private final AttendanceRepository attendanceRepository;

    @NotNull
    private final AttendanceDetailRepository attendanceDetailRepository;

    @NotNull
    private final UserRepository userRepository;

    @NotNull
    private final PersonRepository personRepository;

    @NotNull
    private final CollectorResource collectorResource;

    @Override
    public List<Attendance> getOnline() {
        CollectorResource.@NotNull AttendanceManagement attendanceManagement = collectorResource.getAttendanceManagement();
        User user = userRepository.findBySessionKey()
                .orElseThrow(() -> new CommonException(401, "未找到用户"));
        if (user.getAuthorities().contains(User.Role.ROLE_ADMIN)) {
            return attendanceManagement.getAll();
        } else {
            Person person = personRepository.findByOauthId(user.getOauthId())
                    .orElseThrow(() -> new CommonException(401, "未找到person"));
            List<GatherPerson> gatherPersonList = gatherPersonRepository.findAllByPersonIdAndControl(person.getId(), true);
            List<Long> gatherIds = gatherPersonList.stream().map(GatherPerson::getGatherId).collect(Collectors.toList());
            return attendanceManagement.getAll().stream().filter(attendance -> gatherIds.contains(attendance.getGatherId())).collect(Collectors.toList());
        }
    }

    @Override
    public List<Attendance> history() {
        User user = userRepository.findBySessionKey()
                .orElseThrow(() -> new CommonException(401, "未找到用户"));
        if (user.getAuthorities().contains(User.Role.ROLE_ADMIN)) {
            return attendanceRepository.findByFinish(true);
        } else {
            Person person = personRepository.findByOauthId(user.getOauthId())
                    .orElseThrow(() -> new CommonException(401, "未找到person"));
            List<GatherPerson> gatherPersonList = gatherPersonRepository.findAllByPersonIdAndControl(person.getId(), true);
            List<Long> gatherIds = gatherPersonList.stream().map(GatherPerson::getGatherId).collect(Collectors.toList());
            return attendanceRepository.findAllByFinishAndGatherIdIn(true, gatherIds);
        }
    }

    @Override
    public List<AttendanceDetail> historyDetail(Long id) {
        return attendanceDetailRepository.findAllByAttendanceId(id);
    }

    @Override
    public AttendanceDetail addHistoryDetail(Long id, AttendanceDetail attendanceDetail) {
        AttendanceDetail newAttendanceDetail = attendanceDetailRepository.findByPersonIdAndAttendanceId(attendanceDetail.getPersonId(), id)
                .orElse(new AttendanceDetail()
                        .setFace("default")
                        .setPersonId(attendanceDetail.getPersonId())
                        .setAttendanceId(id)
                        .setScore(1.0f)
                );
        return attendanceDetailRepository.save(newAttendanceDetail);

    }
}
