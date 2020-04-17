package in.bugr.management.controller;

import com.google.gson.JsonObject;
import in.bugr.management.consistent.CollectorResource;
import in.bugr.management.entity.Attendance;
import in.bugr.management.service.RecognitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
