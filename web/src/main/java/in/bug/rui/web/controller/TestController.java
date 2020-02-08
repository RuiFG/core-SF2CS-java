package in.bug.rui.web.controller;

import in.bug.rui.web.service.TestBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @author BugRui
 * @date 2020/1/18 下午4:22
 **/
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private @NotNull
    final TestBusinessService testBusinessService;

    @GetMapping("hello")
    public String test() {
        return testBusinessService.test();
    }
}
