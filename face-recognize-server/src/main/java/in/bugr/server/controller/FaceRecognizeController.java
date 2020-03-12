package in.bugr.server.controller;

import in.bugr.server.config.FaceRecognizeProperty;
import in.bugr.server.service.FaceRecognizeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

/**
 * @author BugRui
 * @date 2020/3/12 下午1:45
 **/
@RestController
@AllArgsConstructor
@Slf4j
public class FaceRecognizeController {


    @NotNull
    private final Registration registration;

    @NotNull
    private final FaceRecognizeProperty faceRecognizeProperty;

    @NotNull
    private final FaceRecognizeService faceRecognizeService;

    @GetMapping("info")
    public String info() {
        HashMap<String, String> result = new HashMap<>();
        result.put("info", "ok");
        result.put("uri", registration.getUri().toString());
        result.put("face-recognize.number", String.valueOf(faceRecognizeProperty.getCorePoolSize()));
        return result.toString();
    }
}
