package in.bugr.jni.model;

import in.bugr.common.entity.Person;
import in.bugr.common.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author bugrui
 */
public class ImageData {
    public static class ModelMapper {

        public static ImageData toImageData(BufferedImage bufferedImage) {
            byte[] bytes = ImageHelper.getMatrixBGR(bufferedImage);
            ImageData imageData = new ImageData();
            imageData.channels = 3;
            imageData.width = bufferedImage.getWidth();
            imageData.height = bufferedImage.getHeight();
            imageData.data = bytes;
            return imageData;
        }

        public static ImageData toImageData(byte[] data) throws IOException {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            BufferedImage image = ImageIO.read(in);
            return toImageData(image);
        }

        public static BufferedImage toBufferedImage(ImageData imageData) {
            return ImageHelper.bgrToBufferedImage(imageData.data, imageData.width, imageData.height);
        }
    }

    public byte[] data;
    public int width;
    public int height;
    public int channels;

    public ImageData() {
    }

    public ImageData(int width, int height, int channels) {
        this.data = new byte[width * height * channels];
        this.width = width;
        this.height = height;
        this.channels = channels;
    }

    public ImageData(int width, int height) {
        this(width, height, 3);
    }


}
