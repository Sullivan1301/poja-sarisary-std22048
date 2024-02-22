import hei.school.sarisary.file.BucketComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@Service
public class SarisaryService {

    private final BucketComponent bucketComponent;

    @Autowired
    public SarisaryService(BucketComponent bucketComponent) {
        this.bucketComponent = bucketComponent;
    }

    public void processImage(byte[] imageBytes, String id) throws IOException {
        String originalImageKey = "original/" + id;
        bucketComponent.upload(imageBytes, originalImageKey);

        byte[] transformedImageBytes = transformToBlackAndWhite(imageBytes);

        String transformedImageKey = "transformed/" + id;
        bucketComponent.upload(transformedImageBytes, transformedImageKey);
    }

    public Map<String, String> getImageUrls(String id) {
        String originalImageKey = "original/" + id;
        String transformedImageKey = "transformed/" + id;

        URL originalImageUrl = bucketComponent.presign(originalImageKey, Duration.ofMinutes(10));
        URL transformedImageUrl = bucketComponent.presign(transformedImageKey, Duration.ofMinutes(10));

        Map<String, String> urls = new HashMap<>();
        urls.put("original_url", originalImageUrl.toString());
        urls.put("transformed_url", transformedImageUrl.toString());

        return urls;
    }

    private byte[] transformToBlackAndWhite(byte[] imageBytes) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(inputStream);

            BufferedImage blackAndWhiteImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
            blackAndWhiteImage.getGraphics().drawImage(image,  0,  0, null);

            ImageIO.write(blackAndWhiteImage, "png", outputStream);

            return outputStream.toByteArray();
        }
    }
}
