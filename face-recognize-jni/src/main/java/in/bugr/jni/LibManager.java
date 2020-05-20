package in.bugr.jni;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author BugRui
 * @date 2020/2/9 下午3:01
 **/
public class LibManager {

    @Getter
    public enum Version {
        /**
         * Windows Lib内容
         */
        WINDOWS_X64("windows-x64", "Windows 调用sf2cs-interface x64位"),

        LINUX_X64("linux-x64", "Linux 调用sf2cs-interface x64位",
                "libSeetaFaceDetector.so.d80ecca", "libSeetaFaceRecognizer.so.d80ecca", "libSeetaFaceLandmarker.so.d80ecca",
                "libSeetaFaceTracker.so.d80ecca", "libSeetaQualityAssessor.so.d80ecca",
                "libSeetaFaceDetector.so", "libSeetaFaceRecognizer.so", "libSeetaFaceLandmarker.so",
                "libSeetaFaceTracker.so", "libSeetaQualityAssessor.so",
                "libSF2CS-Interface.so"),
        ;
        String description;
        String name;
        String[] libFiles;

        Version(String name, String description, String... libFiles) {
            this.name = name;
            this.description = description;
            this.libFiles = Objects.isNull(libFiles) ? new String[0] : libFiles;
        }
    }

    private static String LIB_PATH;

    private final static Version VERSION;

    static {
        String system = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("java.vm.name").toLowerCase();
        if (system.contains("windows")) {
            if (arch.contains("32")) {
                VERSION = Version.WINDOWS_X64;
            } else {
                VERSION = Version.WINDOWS_X64;
            }
        } else if (system.contains("linux")) {
            if (arch.contains("32")) {
                VERSION = Version.LINUX_X64;
            } else {
                VERSION = Version.LINUX_X64;
            }
        } else {
            throw new Error("System version error");
        }

    }

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private LibManager() {
    }

    /**
     * 加载dll
     */
    static void loadLibrary(String paramLibPath) {
        if (StringUtils.isBlank(LIB_PATH)) {
            synchronized (LibManager.class) {
                if (StringUtils.isBlank(LIB_PATH)) {
                    LIB_PATH = paramLibPath;
                    for (String filename : VERSION.getLibFiles()) {
                        // 仅load dll
                        if (filename.toLowerCase().endsWith(".dll") ||
                                filename.toLowerCase().endsWith(".so") ||
                                filename.toLowerCase().endsWith("d80ecca")) {
                            try {
                                loadLibrary(LIB_PATH, filename);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 加载dll
     *
     * @param path 路径
     * @param file 文件名
     * @throws IOException 异常
     */
    private static void loadLibrary(String path, String file) throws IOException {
        File libFile = new File(path, file);
        System.load(libFile.getCanonicalPath());
    }

    static FaceEngine instance() {
        return new FaceEngine(COUNTER.addAndGet(1));
    }
}

