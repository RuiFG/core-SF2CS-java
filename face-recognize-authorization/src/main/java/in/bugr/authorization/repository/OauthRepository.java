package in.bugr.authorization.repository;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.bugr.authorization.component.BasicRequestInterceptor;
import in.bugr.authorization.repository.fallback.OauthFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @author BugRui
 * @date 2020/4/12 下午4:58
 **/
@FeignClient(name = "oauth", url = "http://118.24.1.170:8888",
        configuration = BasicRequestInterceptor.class,
        fallback = OauthFallback.class)
public interface OauthRepository {
    /**
     * 请求 token
     *
     * @param parameters 参数
     * @return 结果
     */
    @PostMapping("/oauth/token")
    HashMap<String, ?> postAccessToken(@RequestParam Map<String, String> parameters);

    /**
     * 检查 token
     *
     * @param value 参数
     * @return 结果
     */
    @GetMapping(value = "/oauth/check_token")
    HashMap<String, ?> checkToken(@RequestParam("token") String value);
}
