package in.bugr.controller;

import in.bugr.common.entity.Gather;
import in.bugr.common.entity.Person;
import in.bugr.entity.AttendanceDetail;
import in.bugr.service.SharedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/4 下午1:35
 **/
@RestController
@RequiredArgsConstructor
public class SharedController {
    @NotNull
    private final SharedService sharedService;

    @GetMapping("/gather/{id}/not-in/person")
    public List<Person> getPersonByGatherIdNotIn(@PathVariable Long id) {
        return sharedService.getPeronByNotInGatherId(id);
    }

    @GetMapping("/gather/{id}/student")
    public List<Person> getStudentByGatherId(@PathVariable Long id) {
        return sharedService.getStudentByGatherId(id);
    }

    @GetMapping("/gather/{id}/teacher")
    public List<Person> getTeacherByGatherId(@PathVariable Long id) {
        return sharedService.getTeacherByGatherId(id);
    }


    @GetMapping("/me")
    public Person getMe() {
        return sharedService.getMe();
    }

    @PutMapping("/me")
    public Person setMe(@RequestBody Person person) {
        return sharedService.setMe(person);
    }


}
