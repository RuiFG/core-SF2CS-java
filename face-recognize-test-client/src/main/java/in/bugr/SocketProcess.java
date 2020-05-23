package in.bugr;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author BugRui
 * @date 2020/5/17 下午4:11
 **/
public class SocketProcess {
    String detectHost = "http://127.0.0.1:8888/detect/1";
    String collectHost = "http://127.0.0.1:8080/collect/1/fast-one";
    private DetectClient detectClient;
    private CollectClient collectClient;

    public SocketProcess() throws URISyntaxException {
        collectClient = new CollectClient(new URI(collectHost));
        detectClient = new DetectClient(new URI(detectHost));
        detectClient.addObServer(collectClient);
    }

    public void connect() {
        detectClient.connect();
        collectClient.connect();
    }

    public void sendImage(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean flag = ImageIO.write(bufferedImage, "jpg", out);
        byte[] b = out.toByteArray();
        detectClient.send(b);
    }

    public void close() {
        collectClient.close();
        detectClient.close();
    }

    public static void main(String[] args) throws URISyntaxException {
        SocketProcess socketProcess = new SocketProcess();
        socketProcess.connect();
        String videoPath = ResourceUtils.CLASSPATH_URL_PREFIX + "video.mp4";
        try {
            File video = ResourceUtils.getFile(videoPath);
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(video);
            ff.start();
            // 截取中间帧图片(具体依实际情况而定)
            int i = 0;
            int length = ff.getLengthInFrames();
            Frame frame;
            while (i < length) {
                frame = ff.grabFrame();
                if (i % 5 == 0 && frame.image != null) {
                    // 截取的帧图片
                    Java2DFrameConverter converter = new Java2DFrameConverter();
                    BufferedImage srcImage = converter.getBufferedImage(frame);
                    socketProcess.sendImage(srcImage);
                }
                i++;
            }
            ff.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        } finally {
//            socketProcess.exit();
//        }
    }
}
