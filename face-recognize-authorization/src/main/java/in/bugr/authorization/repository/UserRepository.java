package in.bugr.authorization.repository;

import com.google.gson.JsonObject;
import in.bugr.authorization.component.BearerRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author BugRui
 * @date 2020/4/12 下午4:49
 **/
@FeignClient(configuration = BearerRequestInterceptor.class, name = "user",
        url = "http://118.24.1.170:8888")
public interface UserRepository {
    /**
     * 获取登录用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/auth/me")
    JsonObject me();

}
