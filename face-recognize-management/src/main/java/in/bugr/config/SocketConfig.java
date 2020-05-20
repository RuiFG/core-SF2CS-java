package in.bugr.config;

import in.bugr.common.component.ContextSpringConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author BugRui
 * @date 2020/3/31 下午1:31
 **/
@Configuration
@EnableWebSocket
public class SocketConfig {
    @Bean
    public ContextSpringConfigurator customSpringConfigurator() {
        // This is just to get context
        return new ContextSpringConfigurator();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
