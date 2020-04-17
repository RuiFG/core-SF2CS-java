package in.bugr.authorization.component;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author BugRui
 * @date 2020/4/12 下午4:52
 **/
public class BearerRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        Assert.notNull(attributes, "attributes nut null！");
        HttpServletRequest request = attributes.getRequest();
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        requestTemplate.header(HttpHeaders.AUTHORIZATION, bearer);
    }
}