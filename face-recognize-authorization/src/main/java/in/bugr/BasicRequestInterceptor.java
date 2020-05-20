package in.bugr;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import in.bugr.config.OauthProperty;
import org.springframework.http.HttpHeaders;

/**
 * @author BugRui
 * @date 2020/4/12 下午5:00
 **/
public class BasicRequestInterceptor implements RequestInterceptor {
    private final String basicAuth;

    public BasicRequestInterceptor(OauthProperty oauthProperty) {
        basicAuth = HttpHeaders.encodeBasicAuth(oauthProperty.getClientId(),
                oauthProperty.getClientSecret(), null);
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(HttpHeaders.AUTHORIZATION,
                "Basic "+ basicAuth);
    }
}
