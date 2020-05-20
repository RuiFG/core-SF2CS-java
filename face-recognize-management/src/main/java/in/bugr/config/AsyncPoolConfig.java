package in.bugr.config;

import in.bugr.properties.ThreadPoolProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * @author BugRui
 * @date 2020/2/4 下午1:31
 **/
@EnableAsync
public class AsyncPoolConfig {
    /**
     * 公共异步线程池组件
     */
    @Component
    @RequiredArgsConstructor
    public static class CommonAsyncPool {
        public static final String NAME = "common-pool";
        private @NotNull ThreadPoolTaskExecutor executor;
        private @NotNull
        final ThreadPoolProperty.Common threadPoolProperty;

        @Bean(NAME)
        public ThreadPoolTaskExecutor create() {
            executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(threadPoolProperty.getCorePoolSize());
            executor.setMaxPoolSize(threadPoolProperty.getMaxPoolSize());
            executor.setKeepAliveSeconds(threadPoolProperty.getKeepAliveSeconds());
            return executor;
        }
    }
}
