package in.bugr.authorization.controller;

import in.bugr.authorization.service.AuthService;
import in.bugr.entity.Person;
import in.bugr.entity.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author BugRui
 * @date 2020/4/12 下午7:01
 **/
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("oauth/code/{code}")
    public User oauthCode(@PathVariable String code) {
        return authService.code(code);
    }

    @GetMapping("oauth/check_token")
    public User checkToken() {
        return authService.checkToken();
    }

    @GetMapping("auth/me")
    public Person getMe() {
        return authService.getMe();
    }

    @PutMapping("auth/{id}")
    public Person updatePerson(@PathVariable Long id, @RequestBody Person person) {
        person.setId(id);
        return authService.updatePerson(person);
    }

}
