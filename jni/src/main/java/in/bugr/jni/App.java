package in.bugr.jni;

import in.bugr.jni.model.FaceInfoArray;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author BugRui
 * @date 2020/3/9 下午1:50
 **/
public class App {
    public static void main(String[] args) throws IOException, IllegalAccessException {
        FaceEngineFacade faceEngineFacade = FaceEngineFacade.builder().setVersion(LibManager.Version.LINUX_X64)
                .setDevice(FaceEngineFacade.Device.GPU).build();
        BufferedImage bufferedImage=ImageIO.read(new File("/home/bugrui/SF2CS/core-SF2CS-java/jni/src/main/resources/img/4.jpg"));
        FaceInfoArray faceInfoArray = faceEngineFacade.detectFace(bufferedImage);
        System.out.println(faceInfoArray.size);
    }
}
