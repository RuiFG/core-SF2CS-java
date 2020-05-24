package in.bugr;

import com.google.gson.JsonObject;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
                ServerHttpResponse response = exchange.getResponse();
                HttpHeaders headers = request.getHeaders();
                List<String> list = headers.get(HttpHeaders.AUTHORIZATION);
                if (!ObjectUtils.isEmpty(list)) {
                    HttpMethod method = request.getMethod();
                    String url = request.getPath().value();
                    Map<String, Object> map = authFeignClient.hasPermission(String.valueOf(method), url, list.get(0));
                    boolean access = (boolean) map.getOrDefault("access", false);
                    String token = (String) map.getOrDefault("token", "");

                    if (access) {
                        String value = ServerWebExchangeUtils.expand(exchange, token);
                        exchange.getResponse().getHeaders().add("refreshToken", value);
                        return chain.filter(exchange);
                    }
                }
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
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
