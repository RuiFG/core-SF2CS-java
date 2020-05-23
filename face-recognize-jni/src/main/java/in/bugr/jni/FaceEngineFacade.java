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
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.ResourceUtils;

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
public class FaceEngineFacade {
    private final FaceEngine faceEngine;

    private boolean live = true;

    private FaceEngineFacade(FaceEngine faceEngine) {
        this.faceEngine = faceEngine;
    }


    public
    enum Device {
        /**
         * AUTO
         */
        AUTO,
        /**
         * CPU
         */
        CPU,
        /**
         * GPU
         */
        GPU

    }

    @Getter
    @AllArgsConstructor
    public
    enum Point {
        /**
         * 5点还是81点
         */
        MODEL_5("/seetaFace2/model5"),
        MODEL_81("/seetaFace2/model81");
        private final String path;
    }

    public enum Property {
        /**
         * Face Engine Properties
         */
        PROPERTY_MIN_FACE_SIZE,
        PROPERTY_THRESHOLD1,
        PROPERTY_THRESHOLD2,
        PROPERTY_THRESHOLD3,
        PROPERTY_VIDEO_STABLE,
    }

    @Setter
    @Accessors(chain = true)
    public static class Builder {
        private String libPath = "/seetaFace2";
        private Device device = Device.AUTO;
        private int deviceId = 0;
        private int width = 640;
        private int height = 480;
        private Point point = Point.MODEL_5;
        private String modelPath = null;

        public FaceEngineFacade build() throws IllegalAccessException {
            LibManager.loadLibrary(libPath);
            FaceEngine faceEngine = LibManager.instance();
            if (modelPath == null) {
                modelPath = point.getPath();
            }
            FaceEngineFacade faceEngineFacade = new FaceEngineFacade(faceEngine);
            if (!faceEngineFacade.init(modelPath, width, height, device, deviceId)) {
                throw new IllegalAccessException(String.format("初始化失败:ModelPath:%s,Device:%s,DeviceId:%s", modelPath, device.name(), deviceId));
            }
            return faceEngineFacade;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    private boolean init(String modelPath, int width, int height, Device device, int id) {
        return faceEngine.init(modelPath, width, height, device.ordinal(), id);
    }

    public FaceInfoArray detectFace(ImageData img) {
        return faceEngine.detectFace(img);
    }

    public FaceInfoArray detectFace(BufferedImage bufferedImage) {

        return faceEngine.detectFace(ImageData.ModelMapper.toImageData(bufferedImage));
    }

    public FaceInfoArray detectFace(byte[] data) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
        return detectFace(image);
    }

    public PointFloatArray detectPoints(ImageData img, Rect rect) {
        return faceEngine.detectPoints(img, rect);
    }

    public PointFloatArray detectPoints(ImageData img, FaceInfo faceInfo) {
        Rect rect = faceInfo.rect;
        return faceEngine.detectPoints(img, rect);
    }

    public float compare(ImageData img1, Rect rect1, ImageData img2, Rect rect2) {
        return faceEngine.compare(img1, rect1, img2, rect2);
    }

    public float compare(ImageData img1, FaceInfo faceInfo1, ImageData img2, FaceInfo faceInfo2) {
        return compare(img1, faceInfo1.rect, img2, faceInfo2.rect);
    }

    public float compareByCroppedFace(ImageData img1, ImageData img2) {
        return faceEngine.compareByCroppedFace(img1, img2);
    }

    public ImageData crop(ImageData img, PointFloatArray pointFloatArray) {
        return faceEngine.crop(img, pointFloatArray);
    }

    public ImageData crop(BufferedImage img, PointFloatArray pointFloatArray) {
        return faceEngine.crop((ImageData.ModelMapper.toImageData(img)), pointFloatArray);
    }

    public int registerByCroppedFace(ImageData img) {
        return faceEngine.registerByCroppedFace(img);
    }

    public boolean delete(int index) {
        return faceEngine.delete(index);
    }

    public QueryResult queryByCroppedFace(ImageData faceImg) {
        return faceEngine.queryByCroppedFace(faceImg);
    }

    public QueryResultArray queryTopByCroppedFace(ImageData faceImg, int n) {
        return faceEngine.queryTopByCroppedFace(faceImg, n);
    }

    public QueryResultArray queryAboveByCroppedFace(ImageData faceImg, float threshold, int n) {
        return faceEngine.queryAboveByCroppedFace(faceImg, threshold, n);
    }

    public int count() {
        return faceEngine.count();
    }


    public void clear() {
        faceEngine.clear();
    }

    public FaceDataBaseData save() {
        return faceEngine.save();
    }

    public boolean load(FaceDataBaseData data) {
        return faceEngine.load(data);
    }

    public void set(Property property, double value) {
        faceEngine.set(property.ordinal(), value);
    }

    public double get(Property property) {
        return faceEngine.get(property.ordinal());
    }


    public boolean isLive() {
        return live;
    }

    public void death() {
        live = false;
    }

    public void destroy() {
        faceEngine.destroy();
        live = false;
    }

}
