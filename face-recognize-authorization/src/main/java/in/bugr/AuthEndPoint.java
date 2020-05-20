package in.bugr;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import in.bugr.common.entity.User;
import in.bugr.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author BugRui
 * @date 2020/4/12 下午7:01
 **/
@RestController
@RequiredArgsConstructor
public class AuthEndPoint {
    private final AuthService authService;

    @PostMapping("/login")
    public HashMap<String, ?> oauthCode(@RequestBody HashMap<String, String> requestMap) {
        return OauthProcessor.login(requestMap);
    }

    @GetMapping("/logout")
    public void logout() {
        authService.logout();
    }

    @GetMapping("/me")
    public User getMe() {
        return authService.getMe();
    }

    @GetMapping("/permission")
    public Map<String, Object> hasPermission(@RequestParam("method") String method, @RequestParam("url") String url) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("access", authService.hasPermission(method, url));
        map.put("token", authService.refreshToken());
        return map;
    }


}
