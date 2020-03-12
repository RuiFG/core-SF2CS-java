package in.bugr.server.config;

import in.bugr.jni.FaceEngineFacade;
import in.bugr.jni.LibManager;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author BugRui
 * @date 2020/3/12 下午3:33
 **/
@Data
@Component
@ConfigurationProperties(prefix = "application.face-recognize")
public class FaceRecognizeProperty {

    private int corePoolSize;
    private int maxPoolSize;
    private int keepAliveSeconds;
    /**
     * 最小人脸检测尺寸
     */
    private double minFaceSize = 30d;
    /**
     * 第一检测阈值
     */
    private double threshold1 = 0.7d;
    /**
     * 第二检测阈值
     */
    private double threshold2 = 0.8d;
    /**
     * 第三检测阈值
     */
    private double threshold3 = 0.9d;

    /**
     * 5点 or 81点
     */
    private FaceEngineFacade.Point point;
    /**
     * 运行平台
     */
    private LibManager.Version version;
    /**
     * 检测模型路径
     */
    private String modelPath;
    /**
     * 动态链接库路径
     */
    private String libPath = "face-recognize-jni/src/main/resources/";
    /**
     * 输入的图片宽度
     */
    private int coreWidth = 640;
    /**
     * 输入的图片高度
     */
    private int coreHeight = 480;
    /**
     * GPU CPU or AUTO
     */
    private FaceEngineFacade.Device device;
    /**
     * use Device index if  set GPU of device
     */
    private int deviceId = 0;


}
