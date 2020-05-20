package in.bugr;

import com.google.gson.JsonObject;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author BugRui
 * @date 2020/4/16 下午8:26
 **/
@Component
public class WebSocketAuthorizeGatewayFilterFactory extends AbstractGatewayFilterFactory<WebSocketAuthorizeGatewayFilterFactory.Config> {
    private static final String ENABLE_KEY;
    private static final JsonObject ERROR_JSON;

    static {
        ENABLE_KEY = "isEnable";
        ERROR_JSON = new JsonObject();
        ERROR_JSON.addProperty("error", "未授权");
        ERROR_JSON.addProperty("description", "描述授权");
    }

    public WebSocketAuthorizeGatewayFilterFactory() {
        super(WebSocketAuthorizeGatewayFilterFactory.Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList(ENABLE_KEY);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            //TODO 从Query参数中取出Token并验证
            return chain.filter(exchange);
        });
    }

    public static class Config {
        boolean isEnable;

        public Config(boolean isEnable) {
            this.isEnable = isEnable;
        }
    }
}
