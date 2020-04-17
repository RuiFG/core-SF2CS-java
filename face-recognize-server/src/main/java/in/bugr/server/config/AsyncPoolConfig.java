package in.bugr.server.config;

import in.bugr.jni.FaceEngineFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author BugRui
 * @date 2020/3/12 下午3:34
 **/
@EnableAsync
@Configuration
public class AsyncPoolConfig {

    /**
     * face recognize 线程池
     */
    @Component
    @RequiredArgsConstructor
    @Slf4j
    public static class FaceRecognizeAsyncPool {
        public static final String NAME = "face-recognize-pool";
        private @NotNull
        ThreadPoolTaskExecutor executor;
        private @NotNull
        final FaceRecognizeProperty faceRecognizeProperty;

        private @NotNull
        final ThreadLocal<FaceEngineFacade> THREAD_LOCAL = new ThreadLocal<>();


        @Bean(NAME)
        ThreadPoolTaskExecutor create() {
            executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(faceRecognizeProperty.getCorePoolSize());
            executor.setMaxPoolSize(faceRecognizeProperty.getMaxPoolSize());
            executor.setThreadNamePrefix(NAME);
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
            return executor;
        }

        @Async(NAME)
        public <T> CompletableFuture<T> start(FaceRecognizeTask<T> faceRecognizeTask) {
            return CompletableFuture.completedFuture(faceRecognizeTask.run(THREAD_LOCAL.get()));
        }

    }
}
