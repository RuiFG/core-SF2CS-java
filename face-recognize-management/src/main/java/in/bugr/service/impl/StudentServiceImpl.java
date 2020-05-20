package in.bugr.service.impl;

import in.bugr.common.entity.GatherPerson;
import in.bugr.common.entity.Person;
import in.bugr.common.entity.User;
import in.bugr.common.exception.CommonException;
import in.bugr.common.repository.GatherPersonRepository;
import in.bugr.common.repository.PersonRepository;
import in.bugr.common.repository.UserRepository;
import in.bugr.entity.Attendance;
import in.bugr.entity.AttendanceDetail;
import in.bugr.entity.dto.StudentAttendanceInfo;
import in.bugr.repository.AttendanceDetailRepository;
import in.bugr.repository.AttendanceRepository;
import in.bugr.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author BugRui
 * @date 2020/4/20 下午9:43
 **/
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    @NotNull
    private final UserRepository userRepository;
    @NotNull
    private final PersonRepository personRepository;

    @NotNull
    private final GatherPersonRepository gatherPersonRepository;
    @NotNull
    final AttendanceRepository attendanceRepository;
    @NotNull
    final AttendanceDetailRepository attendanceDetailRepository;

    @Override
    public List<StudentAttendanceInfo> getHistory() {
        List<StudentAttendanceInfo> result = new ArrayList<>();
        User user = userRepository.findBySessionKey().orElseThrow(() -> new CommonException(401, "未找到用户"));
        Person person = personRepository.findByOauthId(user.getOauthId())
                .orElseThrow(() -> new CommonException(401, "未找到person"));
        List<GatherPerson> gatherPersonList = gatherPersonRepository.findAllByPersonIdAndControl(person.getId(), false);
        List<Long> gatherIdList = gatherPersonList.stream().map(GatherPerson::getGatherId).collect(Collectors.toList());
        List<Attendance> attendanceList = attendanceRepository.findAllByFinishAndGatherIdIn(true, gatherIdList);

        for (Attendance attendance :
                attendanceList) {
            StudentAttendanceInfo studentAttendanceInfo = new StudentAttendanceInfo()
                    .setAttendanceName(attendance.getName())
                    .setGatherName(attendance.getGatherName());
            Optional<AttendanceDetail> attendanceDetailOptional = attendanceDetailRepository.findByPersonIdAndAttendanceId(person.getId(), attendance.getId());
            if (attendanceDetailOptional.isEmpty()) {
                studentAttendanceInfo.setFace("default")
                        .setScore(0.0f);
            } else {
                AttendanceDetail attendanceDetail = attendanceDetailOptional.get();
                studentAttendanceInfo.setFace(attendanceDetail.getFace())
                        .setScore(attendanceDetail.getScore());
            }
            result.add(studentAttendanceInfo);
        }
        return result;
    }
}
