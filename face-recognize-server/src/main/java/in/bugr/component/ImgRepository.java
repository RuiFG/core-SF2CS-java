package in.bugr.component;

import in.bugr.common.exception.CommonException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author BugRui
 * @date 2020/4/19 下午1:28
 **/
public class ImgRepository {
    private static final String AVATAR_PATH = "/home/bugrui/avatar/";
    private static final String FACE_PATH = "/home/bugrui/face/";
    private static final String FORMATS = ".png";

    private static String write(BufferedImage bufferedImage, String path) throws IOException {
        //generate random UUID file Name .png
        String randomFileName = UUID.randomUUID().toString();
        File file = new File(path + randomFileName + FORMATS);

        if (!ImageIO.write(bufferedImage, "png", file)) {
            throw new CommonException(500, "写入文件错误");
        }
        return randomFileName;
    }

    private static BufferedImage read(String fileName, String path) throws IOException {
        File file = new File(path + fileName + FORMATS);
        return ImageIO.read(file);
    }

    public static String writeAvatar(BufferedImage bufferedImage) throws IOException {
        return write(bufferedImage, AVATAR_PATH);
    }

    public static BufferedImage readAvatar(String avatarFileName) throws IOException {
        return read(avatarFileName, AVATAR_PATH);
    }

    public static String writeFace(BufferedImage bufferedImage) throws IOException {
        return write(bufferedImage, FACE_PATH);
    }

    public static BufferedImage readFace(String faceFileName) throws IOException {
        return read(faceFileName, FACE_PATH);
    }
}
