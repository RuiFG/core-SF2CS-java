package in.bugr.gateway;

import com.google.gson.JsonObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

/**
 * @author BugRui
 * @date 2020/4/14 下午11:30
 **/
@FeignClient(value = "authorization")
public interface AuthFeignClient {

    @GetMapping("check_token")
    HashMap<String, ?> checkToken(@RequestParam("token") String token);

}
