package in.bugr;

import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.socket.server.RequestUpgradeStrategy;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @author BugRui
 * @date 2020/4/4 下午5:37
 **/
@Configuration
public class MyGatewayConfig {

    @Autowired
    public void customReactorMaxFramePayloadLength(WebSocketService webSocketService) {
        if (webSocketService instanceof HandshakeWebSocketService) {
            HandshakeWebSocketService tempHandshakeWebSocketService = (HandshakeWebSocketService) webSocketService;
            RequestUpgradeStrategy upgradeStrategy = tempHandshakeWebSocketService.getUpgradeStrategy();
            if (upgradeStrategy instanceof ReactorNettyRequestUpgradeStrategy) {
                ReactorNettyRequestUpgradeStrategy tempReactorNettyRequestUpgradeStrategy = (ReactorNettyRequestUpgradeStrategy) upgradeStrategy;
                tempReactorNettyRequestUpgradeStrategy.setMaxFramePayloadLength(10 * 1024 * 1024);
            }
        }
    }

    @Bean
    public Decoder feignDecoder() {

        ObjectFactory<HttpMessageConverters> messageConverters = () -> {
            return new HttpMessageConverters();
        };
        return new SpringDecoder(messageConverters);
    }
//    @Bean
//    public CorsWebFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedMethod("*");
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsWebFilter(source);
//    }
}
