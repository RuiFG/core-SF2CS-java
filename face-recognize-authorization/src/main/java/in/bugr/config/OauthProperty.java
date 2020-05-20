package in.bugr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author BugRui
 * @date 2020/4/12 下午5:08
 **/
@Component
@ConfigurationProperties(prefix = "application.oauth")
@Data
public class OauthProperty {
    private String authorizationServerUrl;
    private String tokenInfoUri;
    private String accessTokenUri;
    private String jwkSetUri;
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String scope;
    private String redirectUri;
}
