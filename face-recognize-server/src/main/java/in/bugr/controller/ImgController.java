package in.bugr.controller;

import in.bugr.component.ImgRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author BugRui
 * @date 2020/4/19 下午1:26
 **/
@RestController
@RequestMapping("/img")
public class ImgController {

    @GetMapping(value = "/avatar/{avatar}", produces = MediaType.IMAGE_JPEG_VALUE)
    public BufferedImage getAvatar(@PathVariable String avatar) throws IOException {
        return ImgRepository.readAvatar(avatar);
    }

    @GetMapping(value = "/face/{face}", produces = MediaType.IMAGE_JPEG_VALUE)
    public BufferedImage getFace(@PathVariable String face) throws IOException {
        return ImgRepository.readFace(face);
    }

}
