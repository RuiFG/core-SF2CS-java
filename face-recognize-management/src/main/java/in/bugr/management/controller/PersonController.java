package in.bugr.management.controller;

import in.bugr.entity.Person;
import in.bugr.management.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @author BugRui
 * @date 2020/4/4 下午1:35
 **/
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    @NotNull
    private final PersonService personService;

    @GetMapping("{id}/attendance/{attendanceId}")
    public Object findAttendance(@PathVariable Long id, @PathVariable Long attendanceId) {
        return null;
    }

}
