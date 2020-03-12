package in.bugr.jni;

import in.bugr.jni.model.FaceInfoArray;
import in.bugr.jni.model.ImageData;

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
                .setDevice(FaceEngineFacade.Device.AUTO)
                .setPoint(FaceEngineFacade.Point.MODEL_81).
                        build();
        System.out.println(faceEngineFacade.get(FaceEngineFacade.Property.PROPERTY_MIN_FACE_SIZE));
        System.out.println(faceEngineFacade.get(FaceEngineFacade.Property.PROPERTY_THRESHOLD1));
        System.out.println(faceEngineFacade.get(FaceEngineFacade.Property.PROPERTY_THRESHOLD2));
        System.out.println(faceEngineFacade.get(FaceEngineFacade.Property.PROPERTY_THRESHOLD3));
        System.out.println(faceEngineFacade.get(FaceEngineFacade.Property.PROPERTY_VIDEO_STABLE));
        faceEngineFacade.set(FaceEngineFacade.Property.PROPERTY_MIN_FACE_SIZE, 0.6);
        System.out.println(faceEngineFacade.get(FaceEngineFacade.Property.PROPERTY_MIN_FACE_SIZE));
        BufferedImage bufferedImage = ImageIO.read(new File("/home/bugrui/SF2CS/core-SF2CS-java/face-recognize-jni/src/main/resources/img/999.jpg"));
        ImageData imageData = ImageData.ModelMapper.toImageData(bufferedImage);
        FaceInfoArray faceInfoArray = faceEngineFacade.detectFace(imageData);

        BufferedImage bufferedImage1 = ImageIO.read(new File("/home/bugrui/SF2CS/core-SF2CS-java/face-recognize-jni/src/main/resources/img/5.jpg"));
        ImageData imageData1 = ImageData.ModelMapper.toImageData(bufferedImage1);
        FaceInfoArray faceInfoArray1 = faceEngineFacade.detectFace(imageData1);
        System.out.println(faceEngineFacade.compare(imageData,faceInfoArray.faceInfos[0],imageData1,faceInfoArray1.faceInfos[0]));
    }
}
