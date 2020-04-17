package in.bugr.gateway;

import com.google.gson.JsonObject;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/15 上午12:15
 **/
@Component
public class AuthorizeGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizeGatewayFilterFactory.Config> {


    private AuthFeignClient authFeignClient;
    private static final String ENABLE_KEY;
    private static final JsonObject ERROR_JSON;

    static {
        ENABLE_KEY = "isEnable";
        ERROR_JSON = new JsonObject();
        ERROR_JSON.addProperty("error", "未授权");
        ERROR_JSON.addProperty("description", "描述授权");
    }

    public AuthorizeGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList(ENABLE_KEY);
    }

    @Autowired
    public void setAuthFeignClient(AuthFeignClient authFeignClient) {
        this.authFeignClient = authFeignClient;
    }

    public AuthFeignClient getAuthFeignClient() {
        if (ObjectUtils.isEmpty(authFeignClient)) {
            throw new Error("authFeignClient is null");
        }
        return authFeignClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (config.isEnable) {
                ServerHttpRequest request = exchange.getRequest();
                HttpHeaders headers = request.getHeaders();
                List<String> list = headers.get(HttpHeaders.AUTHORIZATION);
                if (ObjectUtils.isEmpty(list)) {
                    return Mono.empty();
                }
                String token = list.get(0);
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {
        final boolean isEnable;

        public Config(boolean isEnable) {
            this.isEnable = isEnable;
        }
    }
}
