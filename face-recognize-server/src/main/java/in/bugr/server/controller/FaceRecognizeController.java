package in.bugr.server.controller;

import in.bugr.server.service.FaceRecognizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author BugRui
 * @date 2020/3/12 下午1:45
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
public class FaceRecognizeController {
    @NotNull
    private final FaceRecognizeService faceRecognizeService;

    @PostMapping("/detect")
    public String detect(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] imgBytes = file.getBytes();
        faceRecognizeService.detect(imgBytes);
        return "检测人脸通过";
    }

    @PostMapping("/person/{id}/compare")
    public String detectPerson(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id) throws IOException {
        byte[] imgBytes = file.getBytes();
        faceRecognizeService.compare(id, imgBytes);
        return "人脸匹配成功";
    }

    @PostMapping("gather/{gatherId}/person/{personId}")
    public String register(@PathVariable Long gatherId, @PathVariable Long personId) {
        faceRecognizeService.register(personId, gatherId);
        return "注册人脸成功";
    }

    @DeleteMapping("gather/{gatherId}/person/{personId}")
    public String delete(@PathVariable Long gatherId, @PathVariable Long personId) {
        faceRecognizeService.deletePersonFromGather(personId, gatherId);
        return "删除人脸成功";
    }

}
