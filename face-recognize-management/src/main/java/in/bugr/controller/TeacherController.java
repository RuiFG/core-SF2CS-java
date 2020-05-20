package in.bugr.controller;

import in.bugr.entity.Attendance;
import in.bugr.service.RecognitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/20 下午9:12
 **/
@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    @NotNull
    private final RecognitionService recognitionService;
}
