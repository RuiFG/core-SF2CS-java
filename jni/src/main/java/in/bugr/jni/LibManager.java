package in.bugr.jni;

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author BugRui
 * @date 2020/2/9 下午3:01
 **/
class LibManager {

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

    private final String libPath;

    private final Version version;

    private static volatile FaceEngine engine;


    public LibManager(Version version, String libPath) {
        this.version = version;
        this.libPath = libPath;
    }

    /**
     * 加载dll
     */
    private void loadLibrary() {

        for (String filename : this.version.getLibFiles()) {
            // 仅load dll
            if (filename.toLowerCase().endsWith(".dll") || filename.toLowerCase().endsWith(".so") || filename.toLowerCase().endsWith("d80ecca")) {
                try {
                    loadLibrary(libPath, filename);
                } catch (IOException e) {
                    e.printStackTrace();
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
    private void loadLibrary(String path, String file) throws IOException {
        File libFile = new File(path, file);
        System.load(libFile.getCanonicalPath());
    }

    public FaceEngine init() {
        if (engine == null) {
            synchronized (LibManager.class) {
                if (engine == null) {
                    loadLibrary();
                    engine = new FaceEngine();
                }
            }
        }
        return engine;
    }


}
