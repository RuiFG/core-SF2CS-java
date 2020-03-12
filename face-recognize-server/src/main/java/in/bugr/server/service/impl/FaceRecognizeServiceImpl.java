package in.bugr.server.service.impl;

import in.bugr.entity.Student;
import in.bugr.jni.FaceEngineFacade;
import in.bugr.server.config.AsyncPoolConfig;
import in.bugr.server.service.FaceRecognizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author BugRui
 * @date 2020/3/12 下午3:55
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class FaceRecognizeServiceImpl implements FaceRecognizeService {

    private @NotNull
    final AsyncPoolConfig.FaceRecognizeAsyncPool faceRecognizeAsyncPool;

    @Override
    public boolean compare(Student student1, Student student2) {
        CompletableFuture<Integer> start = faceRecognizeAsyncPool.start(faceEngineFacade -> {
            System.out.println(faceEngineFacade.get(FaceEngineFacade.Property.PROPERTY_MIN_FACE_SIZE));
            faceEngineFacade.set(FaceEngineFacade.Property.PROPERTY_MIN_FACE_SIZE, 3);
            log.info("start");
            return 1;
        });
        try {
            System.out.println(start.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return true;
    }
}
