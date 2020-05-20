package in.bugr.controller;

import in.bugr.entity.Attendance;
import in.bugr.entity.AttendanceDetail;
import in.bugr.service.RecognitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/16 下午4:41
 **/
@RestController
@RequestMapping("/recognition")
@RequiredArgsConstructor
public class RecognitionController {
    @NotNull
    private final RecognitionService recognitionService;

    @GetMapping("/online")
    public List<Attendance> online() {
        return recognitionService.getOnline();
    }

    @GetMapping("/history")
    public List<Attendance> history() {
        return recognitionService.history();
    }

    @GetMapping("/history/{id}")
    public List<AttendanceDetail> historyDetail(@PathVariable Long id) {
        return recognitionService.historyDetail(id);
    }

    @PostMapping("/history/{id}")
    public AttendanceDetail addHistoryDetail(@PathVariable Long id, @RequestBody AttendanceDetail attendanceDetail) {
        return recognitionService.addHistoryDetail(id, attendanceDetail);
    }

}
