package hei.school.sarisary.endpoint;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hei.school.sarisary.service.SarisaryService;

import java.io.IOException;
public class SarisaryController {
    private final SarisaryService sarisaryService;

    public SarisaryController(SarisaryService sarisaryService) {
        this.sarisaryService = sarisaryService;
    }

    @PutMapping("/black-and-white/{id}")
    public ResponseEntity<Void> uploadAndTransformImage(@PathVariable String id, @RequestBody byte[] imageBytes) throws IOException {
        sarisaryService.processImage(imageBytes, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/black-and-white/{id}")
    public ResponseEntity<?> getImageUrls(@PathVariable String id) {
        return ResponseEntity.ok(sarisaryService.getImageUrls(id));
    }
}
