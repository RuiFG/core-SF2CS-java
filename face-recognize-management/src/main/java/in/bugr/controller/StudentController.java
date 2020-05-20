package in.bugr.controller;

import in.bugr.entity.dto.StudentAttendanceInfo;
import in.bugr.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/20 下午9:11
 **/
@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    @NotNull
    private final StudentService studentService;

    @GetMapping("/attendance")
    List<StudentAttendanceInfo> getHistory() {
        return studentService.getHistory();
    }
}
