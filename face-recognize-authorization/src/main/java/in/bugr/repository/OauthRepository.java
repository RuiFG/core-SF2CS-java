package in.bugr.repository;

import in.bugr.BasicRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
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
        fallback = OauthRepository.OauthFallback.class)
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

    @Component
    class OauthFallback implements OauthRepository {
        @Override
        public HashMap<String, ?> postAccessToken(Map<String, String> parameters) {
            System.out.println("降级");
            return null;
        }

        @Override
        public HashMap<String, ?> checkToken(String value) {
            return null;
        }
    }
}

