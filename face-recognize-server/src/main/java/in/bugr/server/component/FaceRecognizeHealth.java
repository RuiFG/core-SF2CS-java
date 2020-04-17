package in.bugr.server.component;

import com.sun.istack.NotNull;
import in.bugr.server.config.FaceRecognizeProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * @author BugRui
 * @date 2020/3/31 下午12:37
 **/
@Component
@RequiredArgsConstructor
public class FaceRecognizeHealth implements HealthIndicator {
    @NotNull
    private final FaceEngineFacedPool faceEngineFacedPool;

    @NotNull
    private final FaceRecognizeProperty faceRecognizeProperty;

    private boolean isBusy() {
        return faceEngineFacedPool.getActive() >=
                (faceRecognizeProperty.getCorePoolSize() +
                        (faceRecognizeProperty.getMaxPoolSize() - faceRecognizeProperty.getCorePoolSize()) / 2);
    }

    @Override
    public Health health() {
        Health.Builder builder;
        if (isBusy()) {
            builder = Health.down();
        } else {
            builder = Health.up();
        }
        builder.withDetail("face-recognize.number", faceRecognizeProperty.getCorePoolSize());
        builder.withDetail("face-recognize.active", faceEngineFacedPool.getActive());
        builder.withDetail("face.recognize.idle", faceEngineFacedPool.getIdle());
        return builder.build();
    }
}
