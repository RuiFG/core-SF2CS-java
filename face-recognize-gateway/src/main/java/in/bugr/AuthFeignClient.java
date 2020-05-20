package in.bugr;

import com.google.gson.JsonObject;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @author BugRui
 * @date 2020/4/14 下午11:30
 **/
@FeignClient(value = "authorization")
public interface AuthFeignClient {

    @GetMapping("check_token")
    HashMap<String, ?> checkToken(@RequestParam("token") String token);

    @GetMapping(value = "permission", headers = {"Authorization={token}"})
    Map<String, Object> hasPermission(@RequestParam("method") String method,
                                      @RequestParam("url") String url,
                                      @RequestHeader("token") String token);

}