package com.mello.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Serves the Mello buddy PNG from classpath or from the Maven source tree on disk
 * (so it works in IntelliJ even if the file is not under {@code static/} yet).
 */
@RestController
public class MelloBuddyImageController {

    private static final List<String> CLASSPATH_LOCATIONS = List.of(
        "static/mello_bud.png",
        "mello_bud.png",
        "static/images/mello_bud.png",
        "images/mello_bud.png"
    );

    @GetMapping("/mello_bud.png")
    public ResponseEntity<Resource> melloBudPng() {
        for (String location : CLASSPATH_LOCATIONS) {
            ClassPathResource resource = new ClassPathResource(location);
            if (resource.exists()) {
                return okPng(resource);
            }
        }

        Path base = Path.of(System.getProperty("user.dir", "."));
        List<Path> diskCandidates = List.of(
             base.resolve("public/mello_bud.png"),
             base.resolve("src/assets/images/mello_bud.png"),
             base.resolve("src/images/mello_bud.png")
         );
        for (Path path : diskCandidates) {
            if (Files.isRegularFile(path)) {
                return okPng(new FileSystemResource(path));
            }
        }

        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Resource> okPng(Resource body) {
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .cacheControl(CacheControl.maxAge(Duration.ofDays(7)).cachePublic())
            .body(body);
    }
}
