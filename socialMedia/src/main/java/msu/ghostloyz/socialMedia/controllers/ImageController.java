package msu.ghostloyz.socialMedia.controllers;

import lombok.RequiredArgsConstructor;
import msu.ghostloyz.socialMedia.services.interfaces.PostService;
import msu.ghostloyz.socialMedia.services.interfaces.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Blob;
import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final PostService postService;
    private final UserService userService;

    @GetMapping("/userImages/{id}")
    public ResponseEntity<byte[]> getUserImageById(@PathVariable("id") Integer imageId) throws SQLException {
        Blob b = userService.getUserImageById(imageId);
        byte[] ba = b.getBytes(1L, (int) b.length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(ba);
    }

    @GetMapping("/postImages/{id}")
    public ResponseEntity<byte[]> getPostImageById(@PathVariable("id") Integer imageId) throws SQLException {
        Blob b = postService.getPostImageById(imageId);
        byte[] ba = b.getBytes(1L, (int) b.length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(ba);
    }

}
