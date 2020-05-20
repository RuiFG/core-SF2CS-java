package in.bugr.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author BugRui
 * @date 2020/4/12 下午4:45
 **/
@Configuration
public class OauthRestTemplateConfig {

    @Bean
    Logger.Level feignLevel() {
        return Logger.Level.FULL;
    }

}


