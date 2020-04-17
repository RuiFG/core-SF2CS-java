package in.bugr.management.controller;

import in.bugr.entity.Gather;
import in.bugr.management.entity.Attendance;
import in.bugr.management.service.GatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/4 下午1:36
 **/
@RestController
@RequestMapping("/gather")
@RequiredArgsConstructor
public class GatherController {
    @NotNull
    private final GatherService gatherService;

    @GetMapping("{id}/attendance/{attendanceId}")
    public Object queryPersonLog(@PathVariable Long id, @PathVariable Long attendanceId) {
        return null;
    }

    @PutMapping("{id}")
    public Gather updateGather(@PathVariable Long id, @RequestBody Gather gather) {
        gather.setId(id);
        return gatherService.updateGather(gather);
    }

    @GetMapping("{id}/attendance")
    public List<Attendance> findAttendance(@PathVariable Long id) {
        return null;
    }
}
