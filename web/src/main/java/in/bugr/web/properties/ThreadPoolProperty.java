package in.bugr.web.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author BugRui
 * @date 2020/1/19 下午2:18
 **/
@Data
public class ThreadPoolProperty {

    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer keepAliveSeconds;

    @Component
    @ConfigurationProperties(prefix = "application.task-pool.common")
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Common extends ThreadPoolProperty {

    }

}
