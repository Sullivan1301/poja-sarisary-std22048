package hei.school.sary.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.hei.sary.component.BucketComponent;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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
        // Méthode de transformation de l'image en noir et blanc
    }
}

