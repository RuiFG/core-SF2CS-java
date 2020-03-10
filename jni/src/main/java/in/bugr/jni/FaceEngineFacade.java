package in.bugr.jni;

import in.bugr.jni.model.FaceDataBaseData;
import in.bugr.jni.model.FaceInfo;
import in.bugr.jni.model.FaceInfoArray;
import in.bugr.jni.model.ImageData;
import in.bugr.jni.model.PointFloatArray;
import in.bugr.jni.model.QueryResult;
import in.bugr.jni.model.QueryResultArray;
import in.bugr.jni.model.Rect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 线程不安全
 *
 * @author BugRui
 * @date 2020/3/10 下午8:28
 **/
@RequiredArgsConstructor
public class FaceEngineFacade {
    final FaceEngine faceEngine;


    @Getter
    @AllArgsConstructor
    enum Device {
        /**
         * AUTO
         */
        AUTO(0),
        /**
         * CPU
         */
        CPU(1),
        /**
         * GPU
         */
        GPU(2);

        private final int device;
    }

    @Setter
    @Accessors(chain = true)
    static class Builder {
        private LibManager.Version version = LibManager.Version.LINUX_X64;
        private String libPath = "jni/src/main/resources/";
        private Device device = Device.AUTO;
        private int deviceId = 0;
        private String modelPath = "jni/src/main/resources/model";

        FaceEngineFacade build() throws IllegalAccessException {
            FaceEngine faceEngine = new LibManager(version, libPath).init();
            FaceEngineFacade faceEngineFacade = new FaceEngineFacade(faceEngine);
            if (!faceEngineFacade.init(modelPath, device, deviceId)) {
                throw new IllegalAccessException(String.format("初始化失败:ModelPath:%s,Device:%s,DeviceId:%s", modelPath, device.name(), deviceId));
            }
            return faceEngineFacade;
        }
    }

    static Builder builder() {
        return new Builder();
    }

    private boolean init(String modelPath, Device device, int id) {
        return faceEngine.init(modelPath, device.getDevice(), id);
    }

    FaceInfoArray detectFace(ImageData img) {
        return faceEngine.detectFace(img);
    }

    FaceInfoArray detectFace(BufferedImage bufferedImage) {
        byte[] bytes = ImageHelper.getMatrixBGR(bufferedImage);
        ImageData imageData = new ImageData();
        imageData.channels = 3;
        imageData.width = bufferedImage.getWidth();
        imageData.height = bufferedImage.getHeight();
        imageData.data = bytes;
        return faceEngine.detectFace(imageData);
    }

    FaceInfoArray detectFace(byte[] data) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
        return detectFace(image);
    }

    PointFloatArray detectPoints(ImageData img, Rect rect) {
        return faceEngine.detectPoints(img, rect);
    }

    PointFloatArray detectPoints(ImageData img, FaceInfo faceInfo) {
        Rect rect = faceInfo.rect;
        return faceEngine.detectPoints(img, rect);
    }

    float compare(ImageData img1, Rect rect1, ImageData img2, Rect rect2) {
        return faceEngine.compare(img1, rect1, img2, rect2);
    }

    float compare(ImageData img1, FaceInfo faceInfo1, ImageData img2, FaceInfo faceInfo2) {
        return compare(img1, faceInfo1.rect, img2, faceInfo2.rect);
    }

    float compareByCroppedFace(ImageData img1, ImageData img2) {
        return faceEngine.compareByCroppedFace(img1, img2);
    }

    ImageData crop(ImageData img, PointFloatArray pointFloatArray) {
        return faceEngine.crop(img, pointFloatArray);
    }

    int registerByCroppedFace(ImageData img) {
        return faceEngine.registerByCroppedFace(img);
    }

    QueryResult queryByCroppedFace(ImageData faceImg) {
        return faceEngine.queryByCroppedFace(faceImg);
    }

    QueryResultArray queryTopByCroppedFace(ImageData faceImg, int n) {
        return faceEngine.queryTopByCroppedFace(faceImg, n);
    }

    QueryResultArray queryAboveByCroppedFace(ImageData faceImg, float threshold, int n) {
        return faceEngine.queryAboveByCroppedFace(faceImg, threshold, n);
    }

    int count() {
        return faceEngine.count();
    }


    void clear() {
        faceEngine.clear();
    }

    FaceDataBaseData save() {
        return faceEngine.save();
    }

    boolean load(FaceDataBaseData data) {
        return faceEngine.load(data);
    }

}
